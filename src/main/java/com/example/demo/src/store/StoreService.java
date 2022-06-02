package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.Req.PatchHelpReq;
import com.example.demo.src.store.model.Req.PatchReviewReq;
import com.example.demo.src.store.model.Req.PostHelpReq;
import com.example.demo.src.store.model.Req.PostReviewReq;
import com.example.demo.src.store.model.Res.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class StoreService {
    private final StoreDao storeDao;

    public StoreService(StoreDao storeDao) {
        this.storeDao = storeDao;
    }

    @Transactional(readOnly = true)
    public GetStoreHomeRes getStoreResList(String is_cheetah, int start_delivery_fee, int minimum_price, String is_Delivery) throws BaseException {
        try {
            GetStoreHomeRes getStoreResList = storeDao.getHome(is_cheetah, start_delivery_fee, minimum_price, is_Delivery);
            return getStoreResList;
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public GetStoreOneRes getStoreOne(int storeId, int userIdx) throws BaseException {
        try {
            return storeDao.storeOne(storeId, userIdx);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public GetStoreInfoRes getStoreInfo(int storeId) throws BaseException {
        try {
            return storeDao.getStoreInfo(storeId);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public GetMenuRes getMenuInfo(int storeId, int menuId) throws BaseException {
        try {
            return storeDao.menuInfo(storeId, menuId);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createReview(int userIdx, PostReviewReq postReviewReq) throws BaseException{
        try {
            int result = storeDao.createReview(userIdx, postReviewReq);
            if(result == 0){
                throw new BaseException(FAIL_CREATE_REVIEW);
            }
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public GetReviewOrderRes orderReview(int orderIdx) throws BaseException{
        try {
            return storeDao.getOrderReview(orderIdx);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public GetReviewStoreRes getReviewStore(int storeId) throws BaseException {
        try {
            return storeDao.getReviewStoreRes(storeId);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //리뷰 수정
    @Transactional(rollbackFor = Exception.class)
    public void modifyReview(int userIdx, int reviewIdx, PatchReviewReq patchReviewReq) throws BaseException{
        try {
            int result = storeDao.modifyReview(userIdx, reviewIdx, patchReviewReq);
            if(result == 0){
                throw new BaseException(FAIL_MODIFY_REVIEW);
            }
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReview(int userIdx, int reviewIdx) throws BaseException{
        try {
            int result = storeDao.deleteReview(userIdx, reviewIdx);
            if(result == 0){
                throw new BaseException(FAIL_DELETE_REVIEW);
            }
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<GetSearchMenu> menuSearch(int store_id, String menuName) throws BaseException{
        try {
            return storeDao.searchMenu(store_id, menuName);

        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createCouponUser(int userIdx, int storeIdx) throws BaseException{
        try {
            int result = storeDao.createCoupon(userIdx, storeIdx);
            if(result == 0){
                throw new BaseException(FAIL_CREATE_COUPON);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public int createHelpSign(int userIdx, PostHelpReq postHelpReq) throws BaseException{
        try {
            return storeDao.createHelpSign(userIdx, postHelpReq);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteHelpSign(int userIdx, PatchHelpReq patchHelpReq) throws BaseException{
        try {
            return storeDao.deleteHelpSign(userIdx, patchHelpReq);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public int existsReview(int reviewIdx) throws BaseException {
        try {
            return storeDao.existsReview(reviewIdx);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public int existsCouponUser(int store_id, int user_id) throws BaseException {
        try {
            return storeDao.existsCouponUser(store_id, user_id);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}