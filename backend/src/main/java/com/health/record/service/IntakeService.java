package com.health.record.service;

import com.health.record.dto.IntakeRequest;
import com.health.record.entity.IntakeRecord;
import com.health.record.entity.Patient;
import com.health.record.entity.PatientIdentifier;
import com.health.record.entity.IdentifierType;
import com.health.record.entity.MatchResult;
import com.health.record.repository.IntakeRecordRepository;
import com.health.record.repository.PatientIdentifierRepository;
import com.health.record.repository.PatientRepository;
import com.health.record.util.IdentifierNormalizer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IntakeService {

    private final PatientRepository patientRepo;
    private final PatientIdentifierRepository identifierRepo;
    private final IntakeRecordRepository intakeRepo;
    private final MatchingService matchingService;

    public IntakeService(
            PatientRepository patientRepo,
            PatientIdentifierRepository identifierRepo,
            IntakeRecordRepository intakeRepo,
            MatchingService matchingService
    ) {
        this.patientRepo = patientRepo;
        this.identifierRepo = identifierRepo;
        this.intakeRepo = intakeRepo;
        this.matchingService = matchingService;
    }

    @Transactional
    public MatchResult process(IntakeRequest request, String userId) {

        List<Patient> candidates = patientRepo.findCandidates(
                request.firstName,
                request.lastName,
                request.dob
        );

        MatchResult finalResult = MatchResult.NO_MATCH;
        Patient resolvedPatient = null;

        for (Patient p : candidates) {
            List<PatientIdentifier> identifiers =
                    identifierRepo.findByPatientId(p.getId());

            MatchResult r = matchingService.match(request, p, identifiers);

            if (r == MatchResult.AUTO_MATCH) {
                finalResult = MatchResult.AUTO_MATCH;
                resolvedPatient = p;
                break;
            }

            if (r == MatchResult.REVIEW) {
                finalResult = MatchResult.REVIEW;
            }
        }

        if (finalResult == MatchResult.NO_MATCH) {
            resolvedPatient = createPatient(request, userId);
        }

        storeIntake(request, finalResult, resolvedPatient, userId);

        return finalResult;
    }

    private Patient createPatient(IntakeRequest r, String userId) {
        Patient p = new Patient();
        p.setFirstName(r.firstName);
        p.setLastName(r.lastName);
        p.setDob(r.dob);
        p.setGender(r.gender);
        p.setCreatedBy(userId);

        patientRepo.save(p);

        createIdentifier(p, IdentifierType.EMAIL, r.email);
        createIdentifier(p, IdentifierType.PHONE, r.phone);
        createIdentifier(p, IdentifierType.ADDRESS, r.address);

        return p;
    }

    private void createIdentifier(
            Patient patient,
            IdentifierType type,
            String value
    ) {
        if (value == null) return;

        PatientIdentifier id = new PatientIdentifier();
        id.setPatient(patient);
        id.setIdentifierType(type);
        id.setIdentifierValue(
                IdentifierNormalizer.normalize(type, value)
        );

        identifierRepo.save(id);
    }

    private void storeIntake(
            IntakeRequest r,
            MatchResult result,
            Patient patient,
            String userId
    ) {
        IntakeRecord ir = new IntakeRecord();
        ir.setId(UUID.randomUUID());
        ir.setFirstName(r.firstName);
        ir.setLastName(r.lastName);
        ir.setDob(r.dob);
        ir.setGender(r.gender);
        ir.setEmail(r.email);
        ir.setPhone(r.phone);
        ir.setAddress(r.address);
        ir.setMatchResult(result);
        ir.setResolvedPatient(patient);
        ir.setCreatedBy(userId);

        intakeRepo.save(ir);
    }
}
