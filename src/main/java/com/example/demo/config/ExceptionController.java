package com.example.demo.config;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@ControllerAdvice
public class ExceptionController { //@Controller, @RestController에서 발생한 Exception은 모두 이곳에서 처리
    /**
     * @valid 유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생한다.
     */
    @ExceptionHandler(BaseException.class)
    public BaseResponse BaseException(BaseException exception) {
        return new BaseResponse<>(exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException 발생!!! url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        BaseResponse baseResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.BAD_REQUEST);
    }


    private BaseResponse makeErrorResponse(BindingResult bindingResult) {

        String detail = "";

        //DTO에서 message로 설정한 값
        detail = bindingResult.getFieldError().getDefaultMessage();


        //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
        String bindResultCode = bindingResult.getFieldError().getCode();

        switch (bindResultCode) {
            case "NotBlank":
                return new BaseResponse(NOT_BLANK,detail);
            case "NotEmpty":
                return new BaseResponse(NOT_EMPTY,detail);
            case "NotNull":
                return new BaseResponse(NOT_NULL,detail);
            case "Pattern":
                return new BaseResponse(PATTERN,detail);
            case "Min":
                return new BaseResponse(MIN_VALUE,detail);
            case "Max":
                return new BaseResponse(MAX_VALUE,detail);
            case "Size":
                return new BaseResponse(SIZE,detail);
        }


        return new BaseResponse(DEFAULT_ERROR);
    }
}
