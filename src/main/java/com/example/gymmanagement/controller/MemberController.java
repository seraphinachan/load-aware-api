package com.example.gymmanagement.controller;

import org.springframework.http.ResponseEntity;

import com.example.gymmanagement.dto.MemberResponse;
import com.example.gymmanagement.service.MemberService;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.gymmanagement.dto.MemberCreateRequest;
import jakarta.validation.Valid;
import com.example.gymmanagement.dto.MemberUpdateRequest;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(memberService.updateMember(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // @RestController = API 라우터 (JSON 응답 자동)
    // @RequestMapping("/api/members") = 라우트 prefix
    // @PostMapping / @GetMapping = POST/GET 라우트
    // @RequestBody = $request->all() 같은 요청 바디 매핑
    // @Valid = Request 클래스에 붙은 @NotBlank 등의 유효성 검사를 실행
}
