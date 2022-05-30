package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    DEFAULT_ERROR(false, 2004, "DEFAULT_ERROR"),
    NOT_BLANK(false, 2005, "NOT_BLANK"),
    NOT_EMPTY(false, 2006, "NOT_EMPTY"),
    NOT_NULL(false, 2007, "NOT_NULL"),
    PATTERN(false, 2008, "PATTERN"),
    MIN_VALUE(false, 2009, "MIN"),
    MAX_VALUE(false, 2010, "MIN"),
    SIZE(false, 2011, "SIZE"),


    // users
    USERS_EMPTY_USER_ID(false, 2012, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),

    POST_USERS_EMPTY_NAME(false, 2018, "이름을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2019, "비밀번호를 입력해주세요."),
    POST_USERS_EMPTY_PHONE(false, 2020, "휴대폰 번호를 입력해주세요."),
    POST_USERS_INVALID_PHONE(false, 2021, "휴대폰 번호 형식을 확인해주세요."),

    //[GET] /users/email (이메일,비밀번호 찾기)
    GET_USERS_NAME_EMPTY(false, 2022,"회원 이름을 입력해주세요"),
    GET_USERS_PHONE_EMPTY(false,2023,"회원 휴대폰 번호를 입력해주세요"),
    GET_USERS_EMAIL_EMPTY(false,2024,"회원 이메일을 입력해주세요"),

    // /cart
    FAIL_CREATE_CART(false, 2018, "카트를 생성할 수 없습니다."),
    PATCH_MODIFY_CART_EMPTY(false, 2019, "입력한 카트가 없습니다."),
    FAIL_DUPLICATE_CART(false, 2020, "카트에 담긴 가게가 중복되었습니다."),
    FAIL_CART_EMPTY(false, 2021, "카트가 비었습니다."),

    // /order
    CART_ID_EMPTY(false, 2051, "유효한 카트가 없습니다."),

    // /payment
    PAYMENT_NAME_EMPTY(false, 2070, "결제 방식 은행을 입력해주세요."),
    PAYMENT_NUMBER_EMPTY(false, 2071, "계좌 또는 카드 번호를 입력해주세요."),
    PAYMENT_TYPE_EMPTY(false, 2072, "결제 방식 종류를 입력해주세요."),
    PAYMENT_ID_EMPTY(false, 2073, "결제 방식 식별자를 입력해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 이메일이거나 비밀번호가 틀렸습니다."),
    DELETED_USER(false,3015,"탈퇴한 유저의 접근입니다."),

    //[PATCH] /users
    NEED_NEW_USER_NAME(false,3020,"새로운 이름을 입력해주세요."),
    NEED_NEW_USER_EMAIL(false,3021,"새로운 이메일을 입력해주세요."),
    NEED_NEW_USER_PHONE(false,3022,"새로운 휴대폰번호를 입력해주세요."),
    NEED_NEW_USER_PASSWORD(false,3023,"새로운 비밀번호를 입력해주세요."),

    //[POST] /users/login
    NO_EXIST_EMAIL(false,3024,"존재하지 않는 이메일 입니다."),

    //[POST] /users/bookmark
    ALREADY_POST_BOOKMARK(false, 3025,"이미 추가한 즐겨찾기 입니다."),

    //[PATCH] /users/bookmark/status/:storeIdx
    ALREADY_DELETE_BOOKMARK(false,3026,"이미 삭제된 즐겨찾기 입니다"),

    //[GET] /users/address/:addressIdx
    NO_EXIST_ADDRESS(false,3030,"존재하지 않거나 삭제된 주소입니다."),

    //[PATCH] /category/search/:searchIdx
    ALREADY_DELETED_SEARCH(false, 3035, "이미 삭제된 검색어 입니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[PATCH] /users
    MODIFY_FAIL_USERNAME(false,4014,"유저이름 변경에 실패였습니다."),
    MODIFY_FAIL_USER_EMAIL(false,4015,"유저이메일 변경에 실패하였습니다."),
    MODIFY_FAIL_USER_PHONE(false, 4016, "유저 휴대폰번호 변경에 실패하였습니다."),
    MODIFY_FAIL_USER_PASSWORD(false, 4017, "유저 비밀번호 변경에 실패하였습니다."),

    //[PATCH] /users/status
    DELETE_FAIL_USER(false, 4020, "유저 삭제에 실패하였습니다."),

    //[GET] /users/email
    FAIL_FIND_EMAIL(false,4021,"이메일 찾기에 실패하였습니다. "),
    //[GET] /users/password
    FAIL_FIND_PASSWORD(false,4022,"비밀번호 찾기에 실패하였습니다"),

    //[POST] /users/address
    FAIL_CREATE_ADDRESS(false,4023,"새 주소 추가에 실패하였습니다."),

    //[PATCH] /users/address/:addressIdx
    FAIL_MODIFY_ADDRESS(false,4024,"주소 변경에 실패하였습니다."),

    //[PATCH] /users/address/current/:addressIdx
    FAIL_MODIFY_CURRENT_ADDRESS(false,4025, "현재 주소 변경에 실패하였습니다."),

    //[PATCH] /users/address/status/:addressIdx
    FAIL_DELETE_ADDRESS(false,4026,"주소 삭제에 실패하였습니다."),

    //[POST] /users/bookmark/:storeIdx
    FAIL_POST_BOOKMARK(false, 4036,"즐겨찾기 추가에 실패하였습니다."),

    //[PATCH] /users/bookmark/status/:storeIdx
    FAIL_DELETE_BOOKMARK(false,4038,"즐겨찾기 삭제에 실패하였습니다"),

    //[PATCH] /category/search/:searchIdx
    FAIL_DELETE_SEARCH_ONE(false, 4050, "해당 검색어 삭제에 실패하였습니다."),

    //[PATCH] /category/search
    FAIL_DELETE_SEARCH_ALL(false, 4051, "검색어 전체 삭제에 실패하였습니다.");




    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}