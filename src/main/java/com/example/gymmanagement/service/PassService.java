package com.example.gymmanagement.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gymmanagement.dto.PassCreateRequest;
import com.example.gymmanagement.dto.PassResponse;
import com.example.gymmanagement.entity.Pass;
import com.example.gymmanagement.repository.MemberRepository;
import com.example.gymmanagement.repository.PassRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.example.gymmanagement.entity.Member;

@Service
public class PassService {

    private final PassRepository passRepository;
    private final MemberRepository memberRepository;

    // 생성자 주입: Spring이 자동으로 PassRepository, MemberRepository를 주입해준다.
    // 수강권은 회원에 속하므로 MemberRepository도 필요하다.
    public PassService(PassRepository passRepository, MemberRepository memberRepository) {
        this.passRepository = passRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public PassResponse createPass(Long memberId, PassCreateRequest request) {
        // 회원이 존재하는지 먼저 확인한다. 없으면 예외를 던진다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));

        Pass pass = new Pass(member, request.getName(), request.getStartDate(), request.getEndDate());
        Pass saved = passRepository.save(pass);
        return PassResponse.from(saved);
    }

    // 특정 회원의 수강권 목록 조회
    @Transactional(readOnly = true)
    public List<PassResponse> getPassesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));

        // PassRepository.findByMember()는 메서드 이름으로 자동 생성된 쿼리다.
        // → SELECT * FROM passes WHERE member_id = ?
        return passRepository.findByMember(member).stream()
                .map(PassResponse::from)
                .collect((Collectors.toList()));
    }
}
