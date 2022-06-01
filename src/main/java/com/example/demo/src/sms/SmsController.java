package com.example.demo.src.sms;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.Req.PostSmsReq;
import com.example.demo.src.user.model.Res.PostSmsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_INVALID_PHONE;
import static com.example.demo.utils.ValidationRegex.isRegexPhone;

@RestController
@RequestMapping("/users")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;

    public SmsController(SmsService smsService, UserService userService) {
        this.smsService = smsService;
        this.userService = userService;
    }

    @PostMapping("/sms")
    public BaseResponse<PostSmsRes> messageUser(@RequestBody @Valid PostSmsReq postSmsReq){
        //휴대폰 정규표현
        if (!isRegexPhone(postSmsReq.getUser_phone())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }

        String user_phone = postSmsReq.getUser_phone();

        //  난수 생성
        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }
        PostSmsRes postSmsRes = new PostSmsRes(user_phone,numStr);

        try {
            smsService.certifiedPhoneNumber(user_phone,numStr);
            return new BaseResponse<>(postSmsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

}
