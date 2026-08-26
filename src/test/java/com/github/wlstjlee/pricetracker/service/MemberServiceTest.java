package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.MemberResponse;
import com.github.wlstjlee.pricetracker.dto.MemberSignUpRequest;
import com.github.wlstjlee.pricetracker.entity.Member;
import com.github.wlstjlee.pricetracker.exception.DuplicateEmailException;
import com.github.wlstjlee.pricetracker.repository.MemberRepository;
import com.github.wlstjlee.pricetracker.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("정상적인 정보로 회원가입하면 성공한다")
    void signUp_success(){
        // given
        MemberSignUpRequest request = createSignUpRequest("test@test.com", "12345678", "테스트");

        given(memberRepository.existsByEmail("test@test.com")).willReturn(false);
        given(passwordEncoder.encode("12345678")).willReturn("encodedPassword");

        Member savedMember = Member.builder()
                .email("test@test.com")
                .password("encodedPassword")
                .name("테스트")
                .build();
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        // when
        MemberResponse response = memberService.signUp(request);

        // then
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getName()).isEqualTo("테스트");
        verify(memberRepository).save(org.mockito.ArgumentMatchers.any(Member.class));
    }

    @Test
    @DisplayName("이미 가입된 이메일로 회원가입하면 예외가 발생한다")
    void signUp_duplicateEmail(){
        // given
        MemberSignUpRequest request = createSignUpRequest("test@test.com", "12345678", "테스트");

        given(memberRepository.existsByEmail("test@test.com")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(DuplicateEmailException.class);
    }



    private MemberSignUpRequest createSignUpRequest(String email, String password, String name){
        return new MemberSignUpRequest(email, password, name);
    }
}
