package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.Model.Req.CashReq;
import com.example.demo.src.payment.Model.Req.PostCouponReq;
import com.example.demo.src.payment.Model.Req.PostPaymentReq;
import com.example.demo.src.payment.Model.Res.GetCouponRes;
import com.example.demo.src.payment.Model.Res.GetPaymentRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class WayService {
    private final WayDao wayDao;

    public WayService(WayDao wayDao) {
        this.wayDao = wayDao;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createPayment(int userIdx, PostPaymentReq postPaymentReq) throws BaseException {
        try {
            wayDao.createPayment(userIdx, postPaymentReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePayment(int userIdx, Integer payment_method_id) throws BaseException {
        try {
            wayDao.deletePayment(userIdx, payment_method_id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetPaymentRes> getPayment(int user_id) throws BaseException {
        try {
            return wayDao.getPayment(user_id);
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //현금영수증 생성
    @Transactional(rollbackFor = Exception.class)
    public void postCash(int user_id, CashReq cashReq) throws BaseException {
        try {
            int result = wayDao.postCash(user_id, cashReq);
            if(result == 0){
                throw new BaseException(FAIL_CREATE_CASH);
            }
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //현금영수증 수정
    @Transactional(rollbackFor = Exception.class)
    public void patchCash(int user_id, CashReq cashReq) throws BaseException {
        try {
            int result = wayDao.patchCash(user_id, cashReq);
            if(result == 0){
                throw new BaseException(FAIL_MODIFY_CASH);
            }
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //현금 영수증 삭제
    @Transactional(rollbackFor = Exception.class)
    public void deleteCash(int user_id) throws BaseException {
        try {
            int result = wayDao.deleteCash(user_id);
            if(result == 0){
                throw new BaseException(FAIL_MODIFY_CASH);
            }
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //쿠폰 생성
    @Transactional(rollbackFor = Exception.class)
    public void createCoupon(int user_id, PostCouponReq postCouponReq) throws BaseException {
        try {
            int result = wayDao.createCoupon(user_id, postCouponReq);
            if(result == 0){
                throw new BaseException(FAIL_CREATE_COUPON);
            }
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //쿠폰 조회
    @Transactional(readOnly = true)
    public List<GetCouponRes> getCoupon(int user_id) throws BaseException {
        try {
            return wayDao.getCoupon(user_id);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //현금 영수증 확인
    public int checkCash(int user_id) throws BaseException {
        try {
            return wayDao.checkCash(user_id);
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //쿠폰 번호 확인
    public int checkCoupon(String coupon_description) throws BaseException {
        try {
            return wayDao.checkCoupon(coupon_description);

        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //쿠폰 번호 확인
    public int checkMeCoupon(int user_id, String coupon_description) throws BaseException {
        try {
            return wayDao.checkMeCoupon(user_id, coupon_description);
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
