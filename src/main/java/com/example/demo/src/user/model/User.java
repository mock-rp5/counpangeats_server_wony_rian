package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int user_id;

    private String user_email;

    @Size(min=8, max=20, message="비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String user_password;

    private String user_name;

    private String user_phone;
}
