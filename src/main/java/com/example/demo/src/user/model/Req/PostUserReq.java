package com.example.demo.src.user.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq { //회원 가입
    private int user_id;
//    private Timestamp created_at;
//    private Timestamp updated_at;

    private String user_email;
    private String user_password;
    private String user_name;
    private String user_phone;

}
