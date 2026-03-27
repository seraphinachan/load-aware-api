package com.example.gymmanagement.controller;

import com.example.gymmanagement.dto.PassCreateRequest;
import com.example.gymmanagement.dto.PassResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.gymmanagement.service.PassService;

// URL 구조: /api/members/{memberId}/passes
// 수강권은 회원에 속하므로 회원 ID를 URL에 포함시킨다.
// 이런 URL 설계를 REST에서 "중첩 리소스(Nested Resource)"라고 한다.
@RestController
@RequestMapping("/api/members/{memberId}/passes")
public class PassController {

    private final PassService passService;

    public PassController(PassService passService) {
        this.passService = passService;
    }

    // {memberId}는 @PathVariable로 받는다.
    // 예: POST /api/members/1/passes → memberId=1
    @PostMapping
    public ResponseEntity<PassResponse> createPass(@PathVariable Long memberId,
            @Valid @RequestBody PassCreateRequest request) {
        return ResponseEntity.ok(passService.createPass(memberId, request));
    }

    // 예: GET /api/members/1/passes → memberId=1
    @GetMapping
    public ResponseEntity<List<PassResponse>> getPasses(@PathVariable Long memberId) {
        return ResponseEntity.ok(passService.getPassesByMember(memberId));
    }

}
