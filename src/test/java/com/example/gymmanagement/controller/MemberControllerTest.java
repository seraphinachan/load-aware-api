package com.example.gymmanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest: 실제 애플리케이션 컨텍스트를 로드해서 테스트한다.
// @Transactional: 각 테스트가 끝나면 DB 변경사항을 롤백해서 테스트 간 데이터가 오염되지 않는다.
@SpringBootTest
@Transactional
class MemberControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    // MockMvc: 실제 HTTP 요청처럼 컨트롤러를 호출할 수 있는 테스트 도구다.
    private MockMvc mockMvc;

    // @BeforeEach: 각 테스트 메서드 실행 전에 MockMvc를 초기화한다.
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void 회원_생성_성공() throws Exception {
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test\", \"email\":\"test@example.com\", \"phone\":\"010-1234-5678\"}"))
                // status().isOk(): HTTP 200 OK 응답인지 확인한다.
                .andExpect(status().isOk())
                // jsonPath: 응답 JSON에서 특정 필드 값을 꺼내서 검증한다.
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void 중복_이메일_생성_실패() throws Exception {
        // 첫 번째 생성
        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\", \"email\":\"dup@example.com\", \"phone\":\"010-1234-5678\"}"))
                .andExpect(status().isOk());

        // 같은 이메일로 두 번째 생성 → 400 에러가 나야 한다.
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test2\", \"email\":\"dup@example.com\", \"phone\":\"010-1234-5679\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 등록된 이메일입니다."));
    }

    @Test
    void 회원_전체_조회() throws Exception {
        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                // jsonPath("$"): 응답이 배열인지 확인한다.
                .andExpect(jsonPath("$").isArray());
    }
}
