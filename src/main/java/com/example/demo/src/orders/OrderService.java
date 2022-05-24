package com.example.demo.src.orders;

import com.example.demo.config.BaseException;
import com.example.demo.src.orders.model.GetCartRes;
import com.example.demo.src.orders.model.PatchCartReq;
import com.example.demo.src.orders.model.PostCartReq;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCart(int store_id, int cart_id, PatchCartReq patchCartReq) throws BaseException {
        try {
            int result = orderDao.modifyCart(store_id, cart_id, patchCartReq);
            if (result == FAIL){
                throw new BaseException(FAIL_CREATE_CART);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetCartRes getCart(int userIdx) throws BaseException {
        try {
            return orderDao.getCart(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void deleteCart(int cart_id, int user_id) throws BaseException {
        try {
            orderDao.deleteCart(user_id, cart_id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkCart(int user_id) throws BaseException {
        try {
            return orderDao.checkCartStore(user_id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkCartMenu(int menu_id, int user_id, PostCartReq postCartReq) throws BaseException {
        try {
            return orderDao.checkCartMenu(menu_id, user_id, postCartReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkCartExists(int cart_id) throws BaseException {
        try {
            return orderDao.checkCartExists(cart_id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
