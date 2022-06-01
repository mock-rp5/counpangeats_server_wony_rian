package com.example.demo.src.kakao;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.Res.PostLoginRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.config.BaseResponseStatus.POST_KAKAO_LOGIN_CODE_EMPTY;


@RestController
@RequestMapping("/users")
public class KakaoController {

    @Autowired
    private KakaoAPI kakao;
    @Autowired
    private final UserProvider userProvider;

    public KakaoController(KakaoAPI kakao, UserProvider userProvider) {
        this.kakao = kakao;
        this.userProvider = userProvider;
    }

    @PostMapping("/login/access-token")
    public void getAccessToken(@RequestParam("code") String code) {
        String access_Token = kakao.getAccessToken(code);
        System.out.println("controller access_token : " + access_Token);
    }


    @PostMapping("/login/kakao")
    public BaseResponse<PostLoginRes> kakaoLogin(@RequestParam("code") String code) throws BaseException {

        if (code == "") {
            return new BaseResponse<>(POST_KAKAO_LOGIN_CODE_EMPTY);
        }
        String access_Token = kakao.getAccessToken(code);
//        HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
        KakaoUserInfo userInfo = kakao.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);

        try {
            PostLoginRes postLoginRes = userProvider.kakaoLogin(userInfo);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
