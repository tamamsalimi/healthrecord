package com.health.record.repository;

import com.health.record.entity.IntakeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IntakeRecordRepository
        extends JpaRepository<IntakeRecord, UUID> {
}
