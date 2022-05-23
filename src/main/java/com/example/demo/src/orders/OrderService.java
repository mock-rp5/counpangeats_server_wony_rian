package com.example.demo.src.orders;

import com.example.demo.config.BaseException;
import com.example.demo.src.orders.model.GetCartRes;
import com.example.demo.src.orders.model.PatchCartReq;
import com.example.demo.src.orders.model.PostCartReq;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;
import static sun.security.provider.certpath.BuildStep.FAIL;

@Service
public class OrderService {
    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }


    public void createCart(int userIdx, int storeIdx, int menuIdx, PostCartReq postCartReq) throws BaseException {
        try {
            int result = orderDao.createCart(userIdx, storeIdx, menuIdx, postCartReq);
            if (result == FAIL){
                throw new BaseException(FAIL_CREATE_CART);
            }
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCart(int userIdx, int storeIdx, int menuIdx, PatchCartReq patchCartReq) throws BaseException {
        try {
            int result = orderDao.modifyCart(userIdx, storeIdx, menuIdx, patchCartReq);
            if (result == FAIL){
                throw new BaseException(FAIL_CREATE_CART);
            }
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetCartRes getCart(int userIdx) throws BaseException {
        try {
            GetCartRes cart = orderDao.getCart(userIdx);
            return cart;
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
