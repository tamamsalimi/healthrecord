package com.health.record.repository;

import com.health.record.entity.PatientIdentifier;
import com.health.record.entity.IdentifierType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PatientIdentifierRepository
        extends JpaRepository<PatientIdentifier, UUID> {

    List<PatientIdentifier> findByIdentifierTypeAndIdentifierValue(
            IdentifierType type,
            String identifierValue
    );

    List<PatientIdentifier> findByPatientId(UUID patientId);

}
