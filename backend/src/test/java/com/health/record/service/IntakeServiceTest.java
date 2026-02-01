package com.health.record.service;

import com.health.record.dto.PageResponse;
import com.health.record.dto.PatientDto;
import com.health.record.entity.Patient;
import com.health.record.entity.PatientStatus;
import com.health.record.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void should_search_patients_with_pagination() {
        // given
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDob(LocalDate.of(1990, 1, 1));
        patient.setStatus(PatientStatus.ACTIVE);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> page = new PageImpl<>(
                List.of(patient),
                pageable,
                1
        );

        when(patientRepository.search("john", PatientStatus.ACTIVE, pageable))
                .thenReturn(page);

        // when
        PageResponse<PatientDto> result =
                patientService.search("john", PatientStatus.ACTIVE, pageable);

        // then
        assertThat(result.content).hasSize(1);
        assertThat(result.totalElements).isEqualTo(1);
        assertThat(result.content.get(0).firstName).isEqualTo("John");
    }
}
