package com.health.record.dto;

import java.time.LocalDate;

public class IntakeRequest {

    public String firstName;
    public String lastName;
    public LocalDate dob;
    public String gender;

    public String email;
    public String phone;
    public String address; // JSON string
}
