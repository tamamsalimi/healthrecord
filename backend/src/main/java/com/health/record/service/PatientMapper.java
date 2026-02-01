package com.health.record.service;

import com.health.record.dto.PatientDto;
import com.health.record.entity.Patient;

public class PatientMapper {

    public static PatientDto toDto(Patient p) {
        PatientDto dto = new PatientDto();
        dto.id = p.getId();
        dto.firstName = p.getFirstName();
        dto.lastName = p.getLastName();
        dto.dob = p.getDob();
        dto.gender = p.getGender();
        dto.status = p.getStatus().name();
        return dto;
    }
}
