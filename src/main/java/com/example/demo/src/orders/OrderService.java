package com.example.demo.src.orders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.orders.model.Res.GetCartRes;
import com.example.demo.src.orders.model.Req.PatchCartReq;
import com.example.demo.src.orders.model.Req.PostCartReq;
import com.example.demo.src.orders.model.Req.PostOrderReq;
import com.example.demo.src.orders.model.Res.PostOrderRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderService {
    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Transactional(rollbackFor = Exception.class)
    public int createOrder(int userIdx, Integer[] cartList, PostOrderReq postOrderReq) throws BaseException {
        try {
            return orderDao.createOrder(userIdx, cartList, postOrderReq);
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createCart(int userIdx, int storeIdx, int menuIdx, PostCartReq postCartReq) throws BaseException {
        int checkCartStore = checkCart(userIdx);

        System.out.println("checkCartStore = " + checkCartStore);
        System.out.println("storeIdx = " + storeIdx);
        if (checkCartStore != 0 && storeIdx != checkCartStore) {
            throw new BaseException(FAIL_DUPLICATE_CART);
        }
        try {
            int result = orderDao.createCart(userIdx, storeIdx, menuIdx, postCartReq);
            if (result == 0){
                throw new BaseException(FAIL_CREATE_CART);
            }
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyCart(int store_id, int cart_id, PatchCartReq patchCartReq) throws BaseException {
        try {
            int result = orderDao.modifyCart(store_id, cart_id, patchCartReq);
            if (result == 0){
                throw new BaseException(FAIL_CREATE_CART);
            }
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void restartCart(int user_id, int store_id, int menu_id, int now_store_id, PostCartReq postCartReq) throws BaseException {
        try {
            int result = orderDao.restartCart(user_id, now_store_id);
            System.out.println("result = " + result);
            if(result == 0){
                throw new BaseException(FAIL_RESTART_CART);
            }
            int cart = orderDao.createCart(user_id, store_id, menu_id, postCartReq);
            System.out.println("cart = " + cart);
            if(cart == 0){
                throw new BaseException(FAIL_CREATE_CART);
            }
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public GetCartRes getCart(int userIdx) throws BaseException {
        try {
            return orderDao.getCart(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
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
    public int checkCartMenu(int menu_id, int user_id, int storeIdx, PostCartReq postCartReq) throws BaseException {
        try {
            return orderDao.checkCartMenu(menu_id, user_id, storeIdx, postCartReq);
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
    public List<PostOrderRes> checkCartUserExists(int user_id) throws BaseException {
        try {
            return orderDao.checkCartExistsUser(user_id);
        } catch (Exception exception) {
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
