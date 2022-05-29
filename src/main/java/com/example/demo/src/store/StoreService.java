package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.Req.PatchReviewReq;
import com.example.demo.src.store.model.Req.PostReviewReq;
import com.example.demo.src.store.model.Res.GetMenuRes;
import com.example.demo.src.store.model.Res.GetStoreHomeRes;
import com.example.demo.src.store.model.Res.GetStoreInfoRes;
import com.example.demo.src.store.model.Res.GetStoreOneRes;
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
    public List<GetStoreHomeRes> getStoreResList() throws BaseException {
        try {
            List<GetStoreHomeRes> getStoreResList = storeDao.getHome();
            System.out.println("getStoreResList = " + getStoreResList);
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


    public int createReview(int userIdx, PostReviewReq postReviewReq) throws BaseException{
        try {
            return storeDao.createReview(userIdx, postReviewReq);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int modifyReview(int userIdx, int reviewIdx, PatchReviewReq patchReviewReq) throws BaseException{
        try {
            return storeDao.modifyReview(userIdx, reviewIdx, patchReviewReq);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int deleteReview(int userIdx, int reviewIdx) throws BaseException{
        try {
            return storeDao.deleteReview(userIdx, reviewIdx);
        } catch (Exception exception) {
            System.out.println("exception.get = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}