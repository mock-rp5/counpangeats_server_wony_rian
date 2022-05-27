package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.MyEatsInfo;
import com.example.demo.src.user.model.Req.PostLoginReq;
import com.example.demo.src.user.model.Res.*;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public MyEatsInfo getMyEats(int userIdx) throws BaseException {
        try {
            MyEatsInfo getMyEatsRes = userDao.getMyEats(userIdx);
            return getMyEatsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        String email= postLoginReq.getUser_email();

        boolean isExistEmail=userDao.isExistEmail(email);
        System.out.println("isExistEmail: "+isExistEmail);

        if(isExistEmail==false){
            throw new BaseException(NO_EXIST_EMAIL);
        }

        String isExistUserStatus = userDao.isExistUserStatus(email);
        System.out.println("isExistUserStatus : "+ isExistUserStatus);
        if(isExistUserStatus.equals("N")){
            throw new BaseException(DELETED_USER);
        }

        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getUser_password());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (user.getUser_password().equals(encryptPwd)) {
            int user_id = user.getUser_id();
            String jwt = jwtService.createJwt(user_id);
            return new PostLoginRes(user_id, jwt);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public GetUserEmailRes findUserEmail(String user_name, String user_phone) throws BaseException{
        try{
            GetUserEmailRes getUserEmailRes=userDao.findUserEmail(user_name,user_phone);
            return getUserEmailRes;
        }catch(Exception exception){
            throw new BaseException(FAIL_FIND_EMAIL);
        }
    }

    public GetUserPasswordRes findUserPassword(String user_name, String user_email) throws BaseException{
        try{
            GetUserPasswordRes getUserPasswordRes=userDao.findUserPassword(user_name, user_email);
            return getUserPasswordRes;
        }catch(Exception exception){
            throw new BaseException(FAIL_FIND_PASSWORD);
        }
    }

    public List<GetAddressSimpleRes> getAddress(int userIdx) throws BaseException{
        try{
            List<GetAddressSimpleRes> getAddressSimpleResList= userDao.getAddress(userIdx);
            return getAddressSimpleResList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetAddressRes getAddressOne(int addressIdx) throws BaseException{
        String status=userDao.getAddressStatus(addressIdx);

        if(userDao.getAddressStatus(addressIdx).equals("N")) {
            throw new BaseException(NO_EXIST_ADDRESS);
        }
        
        try{
            GetAddressRes getAddressRes= userDao.getAddressOne(addressIdx);
            return getAddressRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetBookmarkRes getBookmarkList(int userIdx) throws BaseException{
        try{
            GetBookmarkRes getBookmarkResList=userDao.getBookmarkList(userIdx);
            return getBookmarkResList;
        }
        catch (Exception exception){
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
            
        }
    }


}
