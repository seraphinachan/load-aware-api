package com.example.gymmanagement.repository;

import com.example.gymmanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    // JpaRepository를 상속하면 findAll(), findById(), save(), delete() 같은 기본 CRUD가 자동으로
    // 생긴다.
    // PHP에서 Eloquent의 Model::all(), Model::find() 같은 패턴과 유사하다.
    // existsByEmail() 처럼 메서드 이름만 규칙에 맞게 선언하면 쿼리도 자동으로 만들어진다.
}
