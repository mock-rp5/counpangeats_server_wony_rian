package com.example.demo.src.user.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq { //회원 가입
    private int user_id;
//    private Timestamp created_at;
//    private Timestamp updated_at;

    @NotBlank(message="이메일을 입력해주세요.")
    private String user_email;

    //패스워드 제한: 영문/숫자/특수문자 2가지 이상 조합 (8~20자)
    //3개이상 연속되거나 동일한 문자/숫자 제한
    //아이디(이메일) 제외
    @NotBlank(message="비밀번호를 입력해주세요.")
    @Size(min=8, max=20, message="비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String user_password;

    @NotBlank(message="이름을 입력해주세요.")
    private String user_name;

    @NotBlank(message="휴대폰번호를 입력해주세요.")
    private String user_phone;

}
