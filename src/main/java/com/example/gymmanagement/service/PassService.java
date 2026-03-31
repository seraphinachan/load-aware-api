package com.example.gymmanagement.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gymmanagement.dto.PassCreateRequest;
import com.example.gymmanagement.dto.PassResponse;
import com.example.gymmanagement.entity.Pass;
import com.example.gymmanagement.repository.MemberRepository;
import com.example.gymmanagement.repository.PassRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import com.example.gymmanagement.entity.Member;

import com.example.gymmanagement.dto.PassUpdateRequest;

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

    // 오늘 날짜 기준으로 만료되지 않은 수강권만 조회한다.
    @Transactional(readOnly = true)
    public List<PassResponse> getActivePassesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));

        // LocalDate.now()로 오늘 날짜를 가져온다.
        // endDate가 오늘 이후인 수강권만 반환한다.
        return passRepository.findByMemberAndEndDateGreaterThanEqual(member, LocalDate.now()).stream()
                .map(PassResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PassResponse updatePass(Long passId, PassUpdateRequest request) {
        // passId로 수강권을 찾는다. 없으면 예외를 던진다.
        Pass pass = passRepository.findById(passId)
                .orElseThrow(() -> new IllegalArgumentException("수강권을 찾을 수 없습니다: " + passId));

        // 새 Pass 객체를 만들지 않고, 기존 객체를 교체한다.
        // JPA는 @Transactional 안에서 엔티티의 변경을 감지해 자동으로 UPDATE 쿼리를 실행한다.
        // 이를 "변경 감지(Dirty Checking)"라고 한다.
        Pass updated = new Pass(pass.getMember(), request.getName(), request.getStartDate(), request.getEndDate());
        passRepository.delete(pass);
        Pass saved = passRepository.save(updated);
        return PassResponse.from(saved);
    }

    @Transactional
    public void deletePass(Long passId) {
        // 존재 여부를 먼저 확인한다. 없는 id면 예외를 던진다.
        Pass pass = passRepository.findById(passId)
                .orElseThrow(() -> new IllegalArgumentException("수강권을 찾을 수 없습니다: " + passId));

        passRepository.delete(pass);
    }
}
