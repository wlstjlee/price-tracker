package com.github.wlstjlee.pricetracker.controller;

import com.github.wlstjlee.pricetracker.dto.LoginRequest;
import com.github.wlstjlee.pricetracker.dto.LoginResponse;
import com.github.wlstjlee.pricetracker.dto.MemberResponse;
import com.github.wlstjlee.pricetracker.dto.MemberSignUpRequest;
import com.github.wlstjlee.pricetracker.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signUp(@RequestBody @Valid MemberSignUpRequest request){
        MemberResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        LoginResponse response = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
