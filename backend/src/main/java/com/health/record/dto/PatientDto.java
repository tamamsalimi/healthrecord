package com.health.record.dto;

import java.time.LocalDate;
import java.util.UUID;

public class PatientDto {

    public UUID id;          // PID
    public String firstName;
    public String lastName;
    public LocalDate dob;
    public String gender;
    public String status;
}
