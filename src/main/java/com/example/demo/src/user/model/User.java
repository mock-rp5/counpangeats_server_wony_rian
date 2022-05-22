package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int user_id;
    private String user_email;
    private String user_password;
    private String user_name;
    private String user_phone;
}
