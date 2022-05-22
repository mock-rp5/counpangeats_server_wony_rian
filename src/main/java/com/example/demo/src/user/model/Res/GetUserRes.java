package com.example.demo.src.user.model.Res;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int user_id;
    private String user_email;
    private String user_password;
    private String user_name;
    private String user_phone;
}