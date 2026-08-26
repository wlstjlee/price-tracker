package com.github.wlstjlee.pricetracker.repository;

import com.github.wlstjlee.pricetracker.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원을 조회할 수 있다")
    void findByEmail_success(){

        // given
        Member member = Member.builder()
                .email("test@test.com")
                .name("테스트")
                .password("encodedPassword")
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> result = memberRepository.findByEmail(member.getEmail());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회하면 빈 값이 반환된다")
    void findByEmail_notFound(){

        // when
        Optional<Member> result = memberRepository.findByEmail("nonexist@test.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("이미 가입된 이메일이면 true를 반환한다")
    void existByEmail_true(){

        // given
        Member member = Member.builder()
                .email("test@test.com")
                .name("테스트")
                .password("encodedPassword")
                .build();
        memberRepository.save(member);

        // when
        boolean result = memberRepository.existsByEmail("test@test.com");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("가입되지 않은 이메일이면 false를 반환한다")
    void existByEmail_false(){

        // when
        boolean result = memberRepository.existsByEmail("nonexist@test.com");

        // then
        assertThat(result).isFalse();
    }
}
