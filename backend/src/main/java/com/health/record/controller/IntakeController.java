package com.health.record.controller;

import com.health.record.dto.IntakeRequest;
import com.health.record.entity.MatchResult;
import com.health.record.service.IntakeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intake")
public class IntakeController {

    private final IntakeService intakeService;

    public IntakeController(IntakeService intakeService) {
        this.intakeService = intakeService;
    }

    @PostMapping
    public ResponseEntity<MatchResult> intake(
            @RequestBody IntakeRequest request,
            @RequestHeader("X-User-Id") String userId
    ) {
        MatchResult result = intakeService.process(request, userId);
        return ResponseEntity.ok(result);
    }
}
