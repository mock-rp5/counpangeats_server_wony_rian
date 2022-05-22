package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.model.Req.PostUserReq;
import com.example.demo.src.user.model.Res.PostUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST 회원가입
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //이메일 중복 검사
        if(userProvider.checkEmail(postUserReq.getUser_email()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getUser_password());
            postUserReq.setUser_password(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
//            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(User user) throws BaseException {
        //기존과 다른 이름인지 검사
        String originName=userDao.getUserName(user.getUser_id());
        if(originName.equals(user.getUser_name())){
            throw new BaseException(NEED_NEW_USER_NAME);
        }

        try{
            int result = userDao.modifyUserName(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserEmail(User user) throws BaseException {

        //기존과 다른 이메일인지 검사
        String originEmail=userDao.getUserEmail(user.getUser_id());
        if(originEmail.equals(user.getUser_email())){
            throw new BaseException(NEED_NEW_USER_EMAIL);
        }

        try{
            int result = userDao.modifyUserEmail(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_EMAIL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPhone(User user) throws BaseException {

        //기존과 다른 휴대폰번호인지 검사
        String originPhone=userDao.getUserPhone(user.getUser_id());
        if(originPhone.equals(user.getUser_phone())){
            throw new BaseException(NEED_NEW_USER_PHONE);
        }

        try{
            int result = userDao.modifyUserPhone(user);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_PHONE);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
