package com.example.gymmanagement.dto;

import com.example.gymmanagement.entity.Member;
import java.time.LocalDateTime;

public class MemberResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime joinedAt;

    private MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.joinedAt = member.getJoinedAt();
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    // from() 은 정적 팩토리 매서드 패턴이다.
    // new MemberResponse(member) 대신 MemberResponse.from(member) 처럼 의미있는 이름으로 객체를
    // 만드는 Java 관용 표현이다.
    // PHP에서 static function create(...) 같은 패턴과 유사하다.
}
