package com.example.gymmanagement.controller;

import com.example.gymmanagement.entity.Member;
import com.example.gymmanagement.repository.MemberRepository;
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

@SpringBootTest
@Transactional
class PassControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MemberRepository memberRepository;

    private MockMvc mockMvc;

    // 테스트에서 공통으로 사용할 회원 id
    private Long memberId;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 각 테스트 전에 테스트용 회원을 DB에 저장해둔다.
        // @Transactional이 있으므로 테스트가 끝나면 자동으로 롤백된다.
        Member member = new Member("TestUser", "pass@test.com", "010-0000-0000");
        this.memberId = memberRepository.save(member).getId();
    }

    @Test
    void 수강권_생성_성공() throws Exception {
        mockMvc.perform(post("/api/members/" + memberId + "/passes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"1month\", \"startDate\":\"2026-03-30\", \"endDate\":\"2026-04-30\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("1month"))
                .andExpect(jsonPath("$.memberId").value(memberId));
    }

    @Test
    void 존재하지_않는_회원의_수강권_생성_실패() throws Exception {
        // 존재하지 않는 회원 id(9999)로 요청하면 400 에러가 나야 한다.
        mockMvc.perform(post("/api/members/9999/passes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"1month\", \"startDate\":\"2026-03-30\", \"endDate\":\"2026-04-30\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다: 9999"));
    }

    @Test
    void 수강권_목록_조회() throws Exception {
        // 먼저 수강권을 생성한다.
        mockMvc.perform(post("/api/members/" + memberId + "/passes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"1month\", \"startDate\":\"2026-03-30\", \"endDate\":\"2026-04-30\"}"));

        // 조회 결과가 배열이고 1개인지 확인한다.
        mockMvc.perform(get("/api/members/" + memberId + "/passes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
