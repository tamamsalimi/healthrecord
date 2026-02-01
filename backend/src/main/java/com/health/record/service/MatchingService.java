package com.health.record.service;

import com.health.record.dto.IntakeRequest;
import com.health.record.entity.Patient;
import com.health.record.entity.PatientIdentifier;
import com.health.record.entity.IdentifierType;
import com.health.record.entity.MatchResult;
import com.health.record.util.IdentifierNormalizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingService {

    public MatchResult match(
            IntakeRequest intake,
            Patient patient,
            List<PatientIdentifier> identifiers
    ) {
        int matches = 0;

        if (nameMatches(intake, patient)) matches++;
        if (dobMatches(intake, patient)) matches++;
        if (emailMatches(intake, identifiers)) matches++;
        if (phoneMatches(intake, identifiers)) matches++;

        if (matches >= 2) return MatchResult.AUTO_MATCH;
        if (matches == 1) return MatchResult.REVIEW;

        return MatchResult.NO_MATCH;
    }

    private boolean nameMatches(IntakeRequest i, Patient p) {
        return eq(i.firstName, p.getFirstName())
            && eq(i.lastName, p.getLastName());
    }

    private boolean dobMatches(IntakeRequest i, Patient p) {
        return i.dob != null && i.dob.equals(p.getDob());
    }

    private boolean emailMatches(
            IntakeRequest i,
            List<PatientIdentifier> identifiers
    ) {
        if (i.email == null) return false;
        String email = IdentifierNormalizer.normalize(
                IdentifierType.EMAIL, i.email
        );

        return identifiers.stream()
                .anyMatch(id ->
                        id.getIdentifierType() == IdentifierType.EMAIL
                        && id.getIdentifierValue().equals(email)
                );
    }

    private boolean phoneMatches(
            IntakeRequest i,
            List<PatientIdentifier> identifiers
    ) {
        if (i.phone == null) return false;
        String phone = IdentifierNormalizer.normalize(
                IdentifierType.PHONE, i.phone
        );

        return identifiers.stream()
                .anyMatch(id ->
                        id.getIdentifierType() == IdentifierType.PHONE
                        && id.getIdentifierValue().equals(phone)
                );
    }

    private boolean eq(String a, String b) {
        return a != null && b != null
            && a.trim().equalsIgnoreCase(b.trim());
    }
}
