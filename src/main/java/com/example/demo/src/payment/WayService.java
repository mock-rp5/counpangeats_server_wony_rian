package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.Model.Req.PostCashReq;
import com.example.demo.src.payment.Model.Req.PostPaymentReq;
import com.example.demo.src.payment.Model.Res.GetPaymentRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

    @Transactional(rollbackFor = Exception.class)
    public int postCash(int user_id, PostCashReq postCashReq) throws BaseException {
        try {
            return wayDao.patchCash(user_id, postCashReq);
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //현금 영수증 삭제
    @Transactional(rollbackFor = Exception.class)
    public int deleteCash(int user_id) throws BaseException {
        try {
            return wayDao.deleteCash(user_id);
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
