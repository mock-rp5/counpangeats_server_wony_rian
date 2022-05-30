package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.Address;
import com.example.demo.src.user.model.Req.PatchAddressReq;
import com.example.demo.src.user.model.Req.PostUserReq;
import com.example.demo.src.user.model.Res.PostAddressRes;
import com.example.demo.src.user.model.Res.PostBookmarkRes;
import com.example.demo.src.user.model.Res.PostUserRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = {BaseException.class})
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //이메일 중복 검사
        if (userProvider.checkEmail(postUserReq.getUser_email()) == 1) {
            throw new BaseException(DUPLICATED_EMAIL);
        }

        String pwd;
        try {
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getUser_password());
            postUserReq.setUser_password(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int userIdx = userDao.createUser(postUserReq);
            return new PostUserRes(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 회원 이름 변경
    @Transactional(rollbackFor = {BaseException.class})
    public void modifyUserName(User user) throws BaseException {
        //기존과 다른 이름인지 검사
        String originName = userDao.getUserName(user.getUser_id());
        if (originName.equals(user.getUser_name())) {
            throw new BaseException(NEED_NEW_USER_NAME);
        }

        try {
            int result = userDao.modifyUserName(user);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 회원 이메일 변경
    @Transactional(rollbackFor = {BaseException.class})
    public void modifyUserEmail(User user) throws BaseException {

        //기존과 다른 이메일인지 검사
        String originEmail = userDao.getUserEmail(user.getUser_id());
        if (originEmail.equals(user.getUser_email())) {
            throw new BaseException(NEED_NEW_USER_EMAIL);
        }

        try {
            int result = userDao.modifyUserEmail(user);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_EMAIL);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 회원 휴대폰번호 변경
    @Transactional(rollbackFor = {BaseException.class})
    public void modifyUserPhone(User user) throws BaseException {

        //기존과 다른 휴대폰번호인지 검사
        String originPhone = userDao.getUserPhone(user.getUser_id());
        if (originPhone.equals(user.getUser_phone())) {
            throw new BaseException(NEED_NEW_USER_PHONE);
        }
        try {
            int result = userDao.modifyUserPhone(user);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_PHONE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 회원 비밀번호 변경
    @Transactional(rollbackFor = {BaseException.class})
    public void modifyUserPassword(User user) throws BaseException {
        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(user.getUser_password());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        //기존과 다른 패스워드인지 검사
        String originPassword = userDao.getUserPassword(user.getUser_id());
        System.out.println("originPassword: " + originPassword);
        System.out.println("encryptPwd: " + encryptPwd);
        if (originPassword.equals(encryptPwd)) {
            throw new BaseException(NEED_NEW_USER_PASSWORD);
        }
        String pwd;
        try {
            //비밀번호 변경할 때도 암호화해서 저장
            pwd = new SHA256().encrypt(user.getUser_password());
            user.setUser_password(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int result = userDao.modifyUserPassword(user);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USER_PASSWORD);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 유저 삭제
    @Transactional(rollbackFor = {BaseException.class})
    public void deleteUser(int userIdx) throws BaseException {
        try {
            int result = userDao.deleteUser(userIdx);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_USER);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //POST 새 주소 추가
    @Transactional(rollbackFor = {BaseException.class})
    public PostAddressRes createAddress(Address address) throws BaseException {

        try {
            int addressIdx = userDao.createAddress(address);
            if (addressIdx == 0) {
                throw new BaseException(FAIL_CREATE_ADDRESS);
            }
            return new PostAddressRes(addressIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 주소 변경
    @Transactional(rollbackFor = {BaseException.class})
    public void modifyAddress(int userIdx, int addressIdx, PatchAddressReq patchAddressReq) throws BaseException {
        try {
            int modify_addressIdx = userDao.modifyAddress(addressIdx, patchAddressReq);
            if (modify_addressIdx == 0) {
                throw new BaseException(FAIL_MODIFY_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 사용자 현재 주소 변경
    @Transactional(rollbackFor = {BaseException.class})
    public void modifyCurrentAddress(int userIdx, int addressIdx) throws BaseException {
        try {
            int modifyCurrentAddressIdx = userDao.modifyCurrentAddress(userIdx, addressIdx);
            if (modifyCurrentAddressIdx == 0) {
                throw new BaseException(FAIL_MODIFY_CURRENT_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 주소 삭제
    @Transactional(rollbackFor = {BaseException.class})
    public void deleteAddress(int userIdx,int addressIdx) throws BaseException {

        try {
            int delete_addressIdx = userDao.deleteAddress(userIdx,addressIdx);

            if (delete_addressIdx == 0) {
                throw new BaseException(FAIL_DELETE_ADDRESS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //POST 북마크 추가
    @Transactional(rollbackFor = {BaseException.class})
    public PostBookmarkRes createBookmark(int userIdx, int storeIdx) throws BaseException {
        //이미 추가한 북마크인지 확인
        int isAlreadyCreate = userDao.isAlreadyCreate(userIdx, storeIdx);
        int bookmarkIdx = 0;
        if (isAlreadyCreate == 1) {
            throw new BaseException(ALREADY_POST_BOOKMARK);
        }
        if (userDao.isExist(userIdx, storeIdx) == 1) { //예전에 삭제 기록이 있으면 그 기록의 상탯값 변경
            bookmarkIdx = userDao.modifyStatus(userIdx, storeIdx);
        } else { //삭제기록이 없으면 데이터 새로 생성
            bookmarkIdx = userDao.createBookmark(userIdx, storeIdx);
        }
        try {
            if (bookmarkIdx == 0) {
                throw new BaseException(FAIL_POST_BOOKMARK);
            }
            return new PostBookmarkRes(bookmarkIdx, userIdx, storeIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //PATACH 북마크 삭제
    @Transactional(rollbackFor = {BaseException.class})
    public void deleteBookmark(int userIdx, int storeIdx) throws BaseException {
        //이미 삭제된 북마크인지 확인
        String bookmarkIdxStatus = userDao.getBookmarkStatus(userIdx, storeIdx);
        if (bookmarkIdxStatus.equals("N")) {
            throw new BaseException(ALREADY_DELETE_BOOKMARK);
        }
        try {
            int bookmarkIdx = userDao.deleteBookmark(userIdx, storeIdx);
            if (bookmarkIdx == 0) {
                throw new BaseException(FAIL_DELETE_BOOKMARK);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //PATACH 북마크 선택 삭제
    @Transactional(rollbackFor = {BaseException.class})
    public Integer[] deleteBookmarkList(int userIdx, Integer[] bookmarkList) throws BaseException {

        try {
            Integer[] bookmarkIdxList = userDao.deleteBookmarkList(userIdx, bookmarkList);
            if (bookmarkIdxList == null) {
                throw new BaseException(FAIL_DELETE_BOOKMARK_LIST);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
        return bookmarkList;
    }
}
