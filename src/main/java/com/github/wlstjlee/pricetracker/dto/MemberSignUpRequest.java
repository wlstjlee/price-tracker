package com.github.wlstjlee.pricetracker.dto;


import com.github.wlstjlee.pricetracker.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignUpRequest {

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이여야 합니다")
    private String password;

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    public Member toEntity(String encodedPassword){
        return Member.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .build();
    }
}
