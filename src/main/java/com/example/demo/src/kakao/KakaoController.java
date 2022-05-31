package com.example.demo.src.kakao;

import com.example.demo.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.POST_KAKAO_LOGIN_CODE_EMPTY;


@RestController
@RequestMapping("/users")
public class KakaoController {

    @Autowired
    private KakaoAPI kakao;

    @PostMapping("/login/access-token")
    public void getAccessToken(@RequestParam("code") String code) {
        String access_Token = kakao.getAccessToken(code);
        System.out.println("controller access_token : " + access_Token);
    }

    @PostMapping("/login/kakao")
    public BaseResponse<HashMap<String,Object>> kakaoLogin(@RequestParam("code") String code){

        if(code == ""){
            return new BaseResponse<>(POST_KAKAO_LOGIN_CODE_EMPTY);
        }
            String access_Token = kakao.getAccessToken(code);
            HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
            System.out.println("login Controller : " + userInfo);
            return new BaseResponse<>(userInfo);
    }

}
