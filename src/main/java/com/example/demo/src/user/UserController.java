package com.example.demo.src.user;

import com.example.demo.src.user.model.Req.PostLoginReq;
import com.example.demo.src.user.model.Req.PostUserReq;
import com.example.demo.src.user.model.Res.GetUserRes;
import com.example.demo.src.user.model.Res.PostLoginRes;
import com.example.demo.src.user.model.Res.PostUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API (이메일 조회)
     * [GET] /users
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
//    //Query String
//    @ResponseBody
//    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
//    public BaseResponse<List<GetUserRes>> getUsers2(@RequestParam(required = false) String Email) {
//        try{
//            if(Email == null){
//                List<GetUserRes> getUsersRes = userProvider.getUsers();
//                return new BaseResponse<>(getUsersRes);
//            }
//            // Get Users
//            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
//            return new BaseResponse<>(getUsersRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

    /**
     * 전체 회원 조회 API (포스트맨 확인용)
     * [GET] /users
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers() {
        try{

            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsers();
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/join")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getUser_email())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //입력한 이메일이 NULL 인지 검사
        if(postUserReq.getUser_email() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        // 입력한 이름이 NULL 인지 검사
        if(postUserReq.getUser_name() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        // 입력한 패스워드가 NULL 인지 검사
        if(postUserReq.getUser_password() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        // 입력한 휴대폰 번호가 NULL 인지 검사
        if(postUserReq.getUser_phone() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){

        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 이름 변경 API
     * [PATCH] /users/name
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/name")
    public BaseResponse<String> modifyUserName(@RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
//            PatchUserReq patchUserReq = new PatchUserReq(userIdxByJwt,user.getUser_name());
            User modify_user=new User(userIdxByJwt,user.getUser_email(),user.getUser_password(),user.getUser_name(),user.getUser_phone());
            userService.modifyUserName(modify_user);

            String result = "유저 이름이 변경되었습니다.";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 이메일 변경 API
     * [PATCH] /users/name
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/email")
    public BaseResponse<String> modifyUserEmail(@RequestBody User user){

        //이메일 정규표현
        if(!isRegexEmail(user.getUser_email())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            User modify_user=new User(userIdxByJwt,user.getUser_email(),user.getUser_password(),user.getUser_name(),user.getUser_phone());
            userService.modifyUserEmail(modify_user);

            String result = "유저 이메일이 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 휴대폰번호 변경 API
     * [PATCH] /users/phone
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/phone")
    public BaseResponse<String> modifyUserPhone(@RequestBody User user){

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            User modify_user=new User(userIdxByJwt,user.getUser_email(),user.getUser_password(),user.getUser_name(),user.getUser_phone());
            userService.modifyUserPhone(modify_user);

            String result = "유저 휴대폰 번호가 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
