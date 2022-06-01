package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.Model.Req.PostCashReq;
import com.example.demo.src.payment.Model.Req.PostCouponReq;
import com.example.demo.src.payment.Model.Req.PostPaymentReq;
import com.example.demo.src.payment.Model.Res.GetCouponRes;
import com.example.demo.src.payment.Model.Res.GetPaymentRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/payments")
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
    @PatchMapping("/status")
    public BaseResponse<String> deletePayment(@RequestParam Integer paymentIdx) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();
            if(paymentIdx == null){
                return new BaseResponse<>(PAYMENT_ID_EMPTY);
            }
            wayService.deletePayment(userIdx, paymentIdx);
            return new BaseResponse<>("결제 수단이 삭제되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    //결제 수단 조회
    @ResponseBody
    @GetMapping
    public BaseResponse<List<GetPaymentRes>> getPayment() throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();
            List<GetPaymentRes> payment = wayService.getPayment(userIdx);
            return new BaseResponse<>(payment);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/cash-receipt")
    public BaseResponse<String> postCash(@Valid @RequestBody PostCashReq postCashReq) throws BaseException{
        try {
            int userIdx= jwtService.getUserIdx();
            System.out.println("userIdx = " + userIdx);
            wayService.postCash(userIdx, postCashReq);
            return new BaseResponse<>("현금영수증 생성 or 수정이 완료되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/cash-receipt/status")
    public BaseResponse<String> deleteCash() throws BaseException{
        try {
            int userIdx= jwtService.getUserIdx();
            wayService.deleteCash(userIdx);
            return new BaseResponse<>("현금영수증이 삭제되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/coupons")
    public BaseResponse<String> createCoupon(@Valid @RequestBody PostCouponReq postCouponReq) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();

            // 쿠폰 번호 확인
            int numCheck = wayService.checkCoupon(postCouponReq.getCoupon_description());
            if(numCheck == 0){
                throw new BaseException(NO_EXISTS_COUPON);
            }

            //유저에게 쿠폰 유무 확인
            int existsCheck = wayService.checkMeCoupon(userIdx, postCouponReq.getCoupon_description());
            System.out.println("existsCheck = " + existsCheck);
            if(existsCheck == 1){
                throw new BaseException(ALREADY_GET_COUPON);
            }

            //쿠폰 만들기
            wayService.createCoupon(userIdx, postCouponReq);
            return new BaseResponse<>("쿠폰이 등록되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //결제 수단 조회
    @ResponseBody
    @GetMapping("/coupons")
    public BaseResponse<List<GetCouponRes>> getCoupon() throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();
            return new BaseResponse<>(wayService.getCoupon(userIdx));
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
