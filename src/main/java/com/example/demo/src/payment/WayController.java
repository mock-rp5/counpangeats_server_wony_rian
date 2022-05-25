package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.Req.DeletePaymentReq;
import com.example.demo.src.payment.Req.PostPaymentReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/way")
public class WayController {

    @Autowired
    private final WayService wayService;
    @Autowired
    private final JwtService jwtService;

    public WayController(WayService wayService, JwtService jwtService) {
        this.wayService = wayService;
        this.jwtService = jwtService;
    }

    //결제수단 등록
    @ResponseBody
    @PostMapping
    public BaseResponse<String> createPayment(@RequestBody PostPaymentReq paymentReq) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();
            if(paymentReq.getPayment_name() == null){
                return new BaseResponse<>(PAYMENT_NAME_EMPTY);
            }
            if(paymentReq.getPayment_number() == null){
                return new BaseResponse<>(PAYMENT_NUMBER_EMPTY);
            }
            if(paymentReq.getPayment_type() == null){
                return new BaseResponse<>(PAYMENT_TYPE_EMPTY);
            }
            wayService.createPayment(userIdx, paymentReq);
            return new BaseResponse<>("결제 수단이 등록되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    //결제 수단 삭제
    @ResponseBody
    @PatchMapping
    public BaseResponse<String> deletePayment(@RequestBody @Valid DeletePaymentReq deletePaymentReq) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();

            wayService.deletePayment(userIdx, deletePaymentReq);
            return new BaseResponse<>("결제 수단이 삭제되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
