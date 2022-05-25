package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.Req.PostPaymentReq;
import com.example.demo.src.payment.Res.GetPaymentRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class WayService {
    private final WayDao wayDao;

    public WayService(WayDao wayDao) {
        this.wayDao = wayDao;
    }

    public void createPayment(int userIdx, PostPaymentReq postPaymentReq) throws BaseException {
        try {
            wayDao.createPayment(userIdx, postPaymentReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deletePayment(int userIdx, Integer payment_method_id) throws BaseException {
        try {
            wayDao.deletePayment(userIdx, payment_method_id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPaymentRes> getPayment(int user_id) throws BaseException {
        try {
            return wayDao.getPayment(user_id);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
