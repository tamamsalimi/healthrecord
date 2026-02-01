package com.health.record.controller;

import com.health.record.dto.PageResponse;
import com.health.record.dto.PatientDto;
import com.health.record.dto.PatientRequest;
import com.health.record.entity.PatientStatus;
import com.health.record.service.PatientService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    // Grid + pagination
    @GetMapping
    public PageResponse<PatientDto> list(Pageable pageable) {
        return service.list(pageable);
    }

    // 2️⃣ Create new patient
    @PostMapping
    public PatientDto create(@RequestBody PatientRequest request) {
        return service.create(request);
    }

    // 3️⃣ Update existing patient
    @PutMapping("/{id}")
    public PatientDto update(
            @PathVariable UUID id,
            @RequestBody PatientRequest patientDto
    ) {
        return service.update(id, patientDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    // Search by PID or name
    @GetMapping("/search")
    public PageResponse<PatientDto> search(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        return service.search(keyword, PatientStatus.ACTIVE,pageable);
    }


}
