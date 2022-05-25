package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.Address;
import com.example.demo.src.user.model.Req.*;
import com.example.demo.src.user.model.Res.*;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPhone;

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


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }


    /**
     * 전체 회원 조회 API (포스트맨 확인용)
     * [GET] /users
     *
     * @return BaseResponse<List < GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers() {
        try {

            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsers();
            return new BaseResponse<>(getUsersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/join")
    public BaseResponse<PostUserRes> createUser(@RequestBody @Valid PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        //이메일 정규표현
        if (!isRegexEmail(postUserReq.getUser_email())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //휴대폰 정규표현
        if (!isRegexPhone(postUserReq.getUser_phone())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        /*
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
        */

        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 마이 이츠 조회 API
     * [GET] /users/my-eats
     *
     * @return BaseResponse<GetMyEatsRes>
     */
    @ResponseBody
    @GetMapping("/my-eats")
    public BaseResponse<GetMyEatsRes> getMyEats() {

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
//            PatchUserReq patchUserReq = new PatchUserReq(userIdxByJwt,user.getUser_name());
            // Get MyEats
            GetMyEatsRes getMyEatsRes = userProvider.getMyEats(userIdxByJwt);
            return new BaseResponse<>(getMyEatsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 로그인 API
     * [POST] /users/login
     *
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {

        //입력한 이메일이 NULL 인지 검사
        if (postLoginReq.getUser_email() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        // 입력한 패스워드가 NULL 인지 검사
        if (postLoginReq.getUser_password() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        try {
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 이름 변경 API
     * [PATCH] /users/name
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/name")
    public BaseResponse<String> modifyUserName(@RequestBody User user) {

        // 입력한 이름이 NULL 인지 검사
        if (user.getUser_name() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
//            PatchUserReq patchUserReq = new PatchUserReq(userIdxByJwt,user.getUser_name());
            User modify_user = new User(userIdxByJwt, user.getUser_email(), user.getUser_password(), user.getUser_name(), user.getUser_phone());
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
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/email")
    public BaseResponse<String> modifyUserEmail(@RequestBody User user) {
        // 입력한 이메일이 NULL 인지 검사
        if (user.getUser_email() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if (!isRegexEmail(user.getUser_email())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            User modify_user = new User(userIdxByJwt, user.getUser_email(), user.getUser_password(), user.getUser_name(), user.getUser_phone());
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
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/phone")
    public BaseResponse<String> modifyUserPhone(@RequestBody User user) {

        // 입력한 휴대폰번호가 NULL 인지 검사
        if (user.getUser_phone() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
        }

        //휴대폰번호 정규표현 확인
        if (!isRegexPhone(user.getUser_phone())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            User modify_user = new User(userIdxByJwt, user.getUser_email(), user.getUser_password(), user.getUser_name(), user.getUser_phone());
            userService.modifyUserPhone(modify_user);

            String result = "유저 휴대폰 번호가 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 패스워드 변경 API
     * [PATCH] /users/password
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/password")
    public BaseResponse<String> modifyUserPassword(@RequestBody @Valid User user) {

        // 입력한 패스워드가 NULL 인지 검사
        if (user.getUser_password() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            User modify_user = new User(userIdxByJwt, user.getUser_email(), user.getUser_password(), user.getUser_name(), user.getUser_phone());
            userService.modifyUserPassword(modify_user);

            String result = "유저 비밀번호가 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 탈퇴 API
     * [PATCH] /users/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/status")
    public BaseResponse<String> deleteUser() {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            userService.deleteUser(userIdxByJwt);

            String result = userIdxByJwt + "번 유저가 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 이메일 찾기 API
     * [GET] /users/email
     * @return BaseResponse<GetUserEmailRes>
     */
    @ResponseBody
    @GetMapping("/email")
    public BaseResponse<GetUserEmailRes> findUserEmail(@RequestBody @Valid GetUserEmailReq getUserEmailReq){
        //휴대폰번호 정규표현 확
        if (!isRegexPhone(getUserEmailReq.getUser_phone())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        try{
            GetUserEmailRes getUserEmailRes = userProvider.findUserEmail(getUserEmailReq);
            return new BaseResponse<>(getUserEmailRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 유저 비밀번호 찾기 API
     * [GET] /users/password
     * @return BaseResponse<GetUserPasswordRes>
     */
    @ResponseBody
    @GetMapping("/password")
    public BaseResponse<GetUserPasswordRes> findUserPassword(@RequestBody @Valid GetUserPasswordReq getUserPasswordReq){
        //이메일 정규표현 확인
        if (!isRegexEmail(getUserPasswordReq.getUser_email())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            GetUserPasswordRes getUserPasswordRes = userProvider.findUserPassword(getUserPasswordReq);
            return new BaseResponse<>(getUserPasswordRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 유저 주소 추가
     * [POST] /users/password
     * @return BaseResponse<GetUserPasswordRes>
     */
    @ResponseBody
    @PostMapping("/address")
    public BaseResponse<PostAddressRes> createAddress(@RequestBody @Valid Address address) {

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            Address new_address = new Address(address.getAddress_id(), address.getMain_address(), address.getDetail_address(),
                    address.getAddress_guide(),userIdxByJwt,address.getLongitude(),address.getLatitude(),address.getAddress_name(),address.getStatus());

            PostAddressRes postAddressRes= userService.createAddress(new_address);

            String result = "새 주소가 추가되었습니다.";
            return new BaseResponse<>(postAddressRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 주소 수정
     * [PATCH] /users/address/status/:addressIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/address/status/{addressIdx}")
    public BaseResponse<String> deleteAddress(@PathVariable("addressIdx") int addressIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            userService.deleteAddress(userIdxByJwt,addressIdx);

            String result ="주소가 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 주소 수정
     * [PATCH] /users/address/:addressIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/address/{addressIdx}")
    public BaseResponse<String> modifyAddress(@PathVariable("addressIdx") int addressIdx, @RequestBody @Valid PatchAddressReq patchAddressReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            PatchAddressReq new_patchAddressReq = new PatchAddressReq(userIdxByJwt, patchAddressReq.getDetail_address(), patchAddressReq.getAddress_guide(), patchAddressReq.getStatus(), patchAddressReq.getAddress_name(),
                    patchAddressReq.getLongitude(),patchAddressReq.getLatitude());
            userService.modifyAddress(addressIdx,new_patchAddressReq);

            String result = "주소 상세 정보가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 회원 주소목록 조회 API
     * [GET] /users/address
     *  유저 주소 목록 - status, 메인주소, 주소 이름
     * @return BaseResponse<List<GetAddressSimpletRes>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/address")
    public BaseResponse<List<GetAddressSimpleRes>> getAddress() {
        try{
            int userIdx = jwtService.getUserIdx();

            List<GetAddressSimpleRes> getAddressSimpleResList = userProvider.getAddress(userIdx);
            return new BaseResponse<>(getAddressSimpleResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 주소 상세조회 API
     * [GET] /users/address
     * 상세조회 탭 - 메인주소, 상세주소, 길안내, Status, 주소 이름
     * @return BaseResponse<GetAddressRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/address/{addressIdx}")
    public BaseResponse<GetAddressRes> getAddressOne(@PathVariable("addressIdx") int addressIdx) {
        try{
            GetAddressRes getAddressRes = userProvider.getAddressOne(addressIdx);
            return new BaseResponse<>(getAddressRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
