package com.example.gymmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    // 기본 생성자 (JPA용)
    protected Member() {
    }

    public Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.joinedAt = LocalDateTime.now(); // 여기로 이동
                                             // 필드에 = LocalDatime.now() 를 쓰면, JPA가 DB에서 데이터를 읽어올 때도 실행돼서 저장된 시간이 덮어씌워질 수
                                             // 있다.
    }

    // Getter/Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public void update(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // setName(), setPhone()을 각각 호출해도 되지만, update()로 묶으면 "이름과 전화번호를 함께 수정한다"는 의미도
    // 코드에 명확히 드러난다.
    // 나중에 수정 시 추가 로직(예: 변경 이력 기록)이 생겨도 이 메서드 하나만 수정하면 된다.
}
