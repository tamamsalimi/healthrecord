package com.health.record.repository;

import com.health.record.entity.Patient;
import com.health.record.entity.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PatientRepository
        extends JpaRepository<Patient, UUID> {

    Page<Patient> findAllByStatus(PatientStatus status, Pageable pageable);

    @Query("""
        select p from Patient p
        where lower(p.firstName) = lower(:firstName)
           or lower(p.lastName) = lower(:lastName)
           or p.dob = :dob
    """)
    List<Patient> findCandidates(
            String firstName,
            String lastName,
            LocalDate dob
    );


    @Query("""
        SELECT p
        FROM Patient p
        WHERE
            p.status = :status
            AND (
            CAST(p.id AS string) LIKE (CONCAT('%', :keyword, '%'))
            OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.lastName)  LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<Patient> search(
            @Param("keyword") String keyword,
            @Param("status") PatientStatus status,
            Pageable pageable
    );

}
