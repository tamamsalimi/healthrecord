package com.health.record.service;

import com.health.record.dto.PageResponse;
import com.health.record.dto.PatientDto;
import com.health.record.dto.PatientRequest;
import com.health.record.entity.Patient;
import com.health.record.entity.PatientStatus;
import com.health.record.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * 1️⃣ Grid – list patients (server-side pagination)
     */
    public PageResponse<PatientDto> list(Pageable pageable) {
        Page<Patient> page = patientRepository.findAllByStatus(PatientStatus.ACTIVE,pageable);
        return toPageResponse(page);
    }

    /**
     * 2️⃣ Search by PID or patient name (server-side pagination)
     */
    public PageResponse<PatientDto> search(
            String keyword,
            PatientStatus status,
            Pageable pageable
    ) {
        Page<Patient> page = patientRepository.search(keyword, status, pageable);
        return toPageResponse(page);
    }


    /**
     * 3️⃣ Create new patient
     */
    @Transactional
    public PatientDto create(PatientRequest request) {
        Patient patient = new Patient();
        patient.setFirstName(request.firstName);
        patient.setLastName(request.lastName);
        patient.setDob(request.dob);
        patient.setGender(request.gender);
        patient.setStatus(
                request.status != null
                        ? PatientStatus.valueOf(request.status)
                        : PatientStatus.ACTIVE
        );

        patient.setCreatedBy("SYSTEM");
        patient.setCreatedAt(OffsetDateTime.now());
        Patient saved = patientRepository.save(patient);
        return toDto(saved);
    }

    /**
     * 4️⃣ Update existing patient
     */
    @Transactional
    public PatientDto update(UUID id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        patient.setFirstName(request.firstName);
        patient.setLastName(request.lastName);
        patient.setDob(request.dob);
        patient.setGender(request.gender);

        if (request.status != null) {
            patient.setStatus(PatientStatus.valueOf(request.status));
        }

        return toDto(patient);
    }

    /**
     * 5️⃣ Delete existing patient
     */
    @Transactional
    public void delete(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setStatus(PatientStatus.INACTIVE); // 👈 soft delete
    }

    // ------------------------------------------------------
    // Mapping helpers (PRIVATE)
    // ------------------------------------------------------

    private PageResponse<PatientDto> toPageResponse(Page<Patient> page) {
        PageResponse<PatientDto> response = new PageResponse<>();
        response.content = page.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        response.page = page.getNumber();
        response.size = page.getSize();
        response.totalElements = page.getTotalElements();
        response.totalPages = page.getTotalPages();
        return response;
    }

    private PatientDto toDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.id = patient.getId();
        dto.firstName = patient.getFirstName();
        dto.lastName = patient.getLastName();
        dto.dob = patient.getDob();
        dto.gender = patient.getGender();
        dto.status = patient.getStatus().name();
        return dto;
    }
}
