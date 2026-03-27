package com.example.gymmanagement.service;

import com.example.gymmanagement.dto.MemberCreateRequest;
import com.example.gymmanagement.dto.MemberResponse;
import com.example.gymmanagement.entity.Member;
import com.example.gymmanagement.repository.MemberRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.example.gymmanagement.dto.MemberUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        Member member = new Member(request.getName(), request.getEmail(), request.getPhone());
        Member saved = memberRepository.save(member);
        return MemberResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다: " + id));
        return MemberResponse.from(member);
    }

    // 수정/삭제 메서드
    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다: " + id));
        member.update(request.getName(), request.getPhone());
        return MemberResponse.from(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다: " + id));
        memberRepository.delete(member);
    }

    // updateMember() 에서 save() 를 호출하지 않는 이유
    // JPA는 트랜잭션 안에서 엔티티의 변경을 자동으로 감지해서 DB에 반영한다.
    // 이걸 변경 감지(Dirty Checking) 라고 하는데, member.update() 를 호출하면 트랜잭션 종료 시 자동으로 UPDATE
    // 쿼리가 실행된다.
    // 단, 지금 MemberService 에 @Transactional 이 없어서 실제로는 동작하지 않는다.

    // 데이터를 변경하는 메서드 → @Transactional
    // 데이터를 조회만 하는 메서드 → @Transactional(readOnly = true)
    // readOnly = true 는 DB에 "이 트랜잭션은 읽기 전용이니 최적화해도 된다"고 알려준다.
    // 변경 감지(Dirty Checking) 도 비활성화되어 성능상 이점이 있다.
}
