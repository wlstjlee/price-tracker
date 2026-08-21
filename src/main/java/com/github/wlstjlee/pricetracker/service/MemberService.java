package com.github.wlstjlee.pricetracker.service;

import com.github.wlstjlee.pricetracker.dto.LoginRequest;
import com.github.wlstjlee.pricetracker.dto.LoginResponse;
import com.github.wlstjlee.pricetracker.dto.MemberResponse;
import com.github.wlstjlee.pricetracker.dto.MemberSignUpRequest;
import com.github.wlstjlee.pricetracker.entity.Member;
import com.github.wlstjlee.pricetracker.exception.DuplicateEmailException;
import com.github.wlstjlee.pricetracker.exception.InvalidLoginException;
import com.github.wlstjlee.pricetracker.repository.MemberRepository;
import com.github.wlstjlee.pricetracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponse signUp(MemberSignUpRequest request){
        if(memberRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException(request.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = request.toEntity(encodedPassword);
        Member saved = memberRepository.save(member);
        return MemberResponse.from(saved);
    }

    public LoginResponse login(LoginRequest request){
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidLoginException::new);

        if(!passwordEncoder.matches(request.getPassword(),member.getPassword())){
            throw new InvalidLoginException();
        }

        String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());

        return LoginResponse.builder()
                .token(token)
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
