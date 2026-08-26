package com.github.wlstjlee.pricetracker.controller;

import com.github.wlstjlee.pricetracker.dto.MemberResponse;
import com.github.wlstjlee.pricetracker.dto.MemberSignUpRequest;
import com.github.wlstjlee.pricetracker.repository.MemberRepository;
import com.github.wlstjlee.pricetracker.security.JwtTokenProvider;
import com.github.wlstjlee.pricetracker.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    MemberService memberService;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    MemberRepository memberRepository;

    @Test
    @DisplayName("올바른 정보로 회원가입 요청하면 201을 응답한다")
    void signUp_success() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("test@test.com", "12345678", "테스트");
        MemberResponse response = MemberResponse.builder()
                .email("test@test.com")
                .name("테스트")
                .id(1L)
                .build();

        given(memberService.signUp(any(MemberSignUpRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.name").value("테스트"));
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않으면 400을 응답한다")
    void signUp_invalidEmail() throws Exception {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("잘못된이메일", "12345678", "테스트");

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
