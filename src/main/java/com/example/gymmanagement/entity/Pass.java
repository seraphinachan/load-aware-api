package com.example.gymmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "passes")
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: 여러 Pass가 하나의 Member에 속한다.
    // @JoinColumn: DB에 member_id 컬럼으로 저장된다 (FK).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 50)
    private String name; // 예: "1개월 회원권", "PT 10회"

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // 회원권 시작일

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate; // 회원권 만료일

    protected Pass() {
    }

    public Pass(Member member, String name, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // fetch = FetchType.LAZY: Pass 객체를 조회할 때 Member 객체는 실제로 DB에서 가져오지 않고,
    // member.getName() 같은 메서드를 호출할 때 DB에서 가져온다.
}
