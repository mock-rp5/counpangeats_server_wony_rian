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

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StoreService {
    private final StoreDao storeDao;

    public StoreService(StoreDao storeDao) {
        this.storeDao = storeDao;
    }

    @Transactional(readOnly = true)
    public GetStoreHomeRes getStoreResList() throws BaseException {
        try {
            GetStoreHomeRes getStoreResList = storeDao.getHome();
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
    public int createReview(int userIdx, PostReviewReq postReviewReq) throws BaseException{
        try {
            return storeDao.createReview(userIdx, postReviewReq);
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

    @Transactional(rollbackFor = Exception.class)
    public int modifyReview(int userIdx, int reviewIdx, PatchReviewReq patchReviewReq) throws BaseException{
        try {
            return storeDao.modifyReview(userIdx, reviewIdx, patchReviewReq);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteReview(int userIdx, int reviewIdx) throws BaseException{
        try {
            return storeDao.deleteReview(userIdx, reviewIdx);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
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
}