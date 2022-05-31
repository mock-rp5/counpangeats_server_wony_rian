package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.CategorySimple;
import com.example.demo.src.category.model.Req.PostSearchReq;
import com.example.demo.src.category.model.Res.GetCategoryDetailRes;
import com.example.demo.src.category.model.Res.GetSearchRes;
import com.example.demo.src.category.model.Res.PostSearchRes;
import com.example.demo.src.category.model.StoreInfo;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CategoryService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryDao categoryDao;
    private final JwtService jwtService;


    @Autowired
    public CategoryService(CategoryDao categoryDao, JwtService jwtService){
        this.categoryDao=categoryDao;
        this.jwtService=jwtService;
    }
    //GET 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<CategorySimple> getCategoryList() throws BaseException{
        try{
            List<CategorySimple> categorySimpleList = categoryDao.getCategoryList();
            return categorySimpleList;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //GET 카테고리 상세화면
    @Transactional(readOnly = true)
    public GetCategoryDetailRes getCategoryDetail(int categoryIdx )throws BaseException{
        try{
            GetCategoryDetailRes getCategoryDetailRes= categoryDao.getCategoryDetail(categoryIdx);
            return getCategoryDetailRes;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //POST 검색 생성
    @Transactional(rollbackFor = {BaseException.class})
    public PostSearchRes createSearch(int userIdx, PostSearchReq postSearchReq) throws BaseException {
        try{
            int search_id = categoryDao.createSearch(userIdx, postSearchReq.getCategory_name());
            return new PostSearchRes(search_id);
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //GET 검색어 목록 조회
    @Transactional(readOnly = true)
    public GetSearchRes getSearchList(int userIdx) throws BaseException{
        try{
            GetSearchRes getSearchResList = categoryDao.getSearchList(userIdx);
            return getSearchResList;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //GET 검색어에 따른 가게 목록 조회
    @Transactional(readOnly = true)
    public List<StoreInfo> getSearchStoreList(String categoryName) throws BaseException{
        try{
          List<StoreInfo> storeInfoList = categoryDao.getSearchStoreList(categoryName);
          return storeInfoList;
        }
        catch(Exception exception){
            System.out.println("exception.getMessage() = " + exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 검색어 개별 삭제
    @Transactional(rollbackFor = {BaseException.class})
    public void deleteOneSearch(int userIdx, int searchIdx) throws BaseException{
        if(categoryDao.getSearchStatus(userIdx, searchIdx).equals("N")) {
            throw new BaseException(ALREADY_DELETED_SEARCH);
        }

        try{
            int search_id=categoryDao.deleteOneSearch(userIdx, searchIdx);
            if(search_id==0)
                throw new BaseException(FAIL_DELETE_SEARCH_ONE);

        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH 검색어 전체 삭제
    @Transactional(rollbackFor = {BaseException.class})
    public void deleteAllSearch(int userIdx) throws BaseException{
        try{
            categoryDao.deleteAllSearch(userIdx);
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
