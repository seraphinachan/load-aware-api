package com.example.gymmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gymmanagement.entity.Pass;
import com.example.gymmanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;

public interface PassRepository extends JpaRepository<Pass, Long> {

    // 특정 회원의 수강권 목록 조회
    // Spring Data JPA가 메서드 이름을 보고 자동으로 쿼리를 만든다.
    // → SELECT * FROM passes WHERE member_id = ?
    List<Pass> findByMember(Member member);

    // endDate가 today 이후인 수강권만 조회한다.
    // → SELECT * FROM passes WHERE member_id = ? AND end_date >= ?
    // Spring Date JPA가 메서드 이름을 분석해서 자동으로 쿼리를 만든다.
    // "GreaterThanEqual" → end_date >= ?
    List<Pass> findByMemberAndEndDateGreaterThanEqual(Member member, LocalDate today);
}
