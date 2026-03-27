package com.example.gymmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class MemberUpdateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;

    public MemberUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
