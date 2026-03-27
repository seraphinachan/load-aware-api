package com.example.gymmanagement.dto;

import com.example.gymmanagement.entity.Pass;
import java.time.LocalDate;

public class PassResponse {

    private Long id;
    private Long memberId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    private PassResponse(Pass pass) {
        this.id = pass.getId();
        this.memberId = pass.getMember().getId();
        this.name = pass.getName();
        this.startDate = pass.getStartDate();
        this.endDate = pass.getEndDate();
    }

    public static PassResponse from(Pass pass) {
        return new PassResponse(pass);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
}
