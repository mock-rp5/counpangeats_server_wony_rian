package com.example.demo.src.user.model.Req;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPasswordReq {
    @NotBlank(message = "이름을 입력해 주세요.")
    private String user_name;

    @NotBlank(message="이메일을 입력해 주세요.")
    private String user_email;
}
