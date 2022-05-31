package com.example.demo.src.kakao;

import com.example.demo.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


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
        String access_Token = kakao.getAccessToken(code);
        HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);
        return new BaseResponse<>(userInfo);
    }


//       try {
//        //jwt에서 idx 추출.
//        int userIdxByJwt = jwtService.getUserIdx();
////            PatchUserReq patchUserReq = new PatchUserReq(userIdxByJwt,user.getUser_name());
//        // Get MyEats
//        MyEatsInfo getMyEatsRes = userProvider.getMyEats(userIdxByJwt);
//        return new BaseResponse<>(getMyEatsRes);
//    } catch (
//    BaseException exception) {
//        return new BaseResponse<>((exception.getStatus()));
//    }

}
