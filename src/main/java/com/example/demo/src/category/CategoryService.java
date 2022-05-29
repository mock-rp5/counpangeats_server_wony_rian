package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.CategorySimple;
import com.example.demo.src.category.model.Req.PostSearchReq;
import com.example.demo.src.category.model.Res.GetSearchRes;
import com.example.demo.src.category.model.Res.PostSearchRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<CategorySimple> getCategoryList() throws BaseException{
        try{
            List<CategorySimple> categorySimpleList = categoryDao.getCategoryList();
            return categorySimpleList;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostSearchRes createSearch(int userIdx, PostSearchReq postSearchReq) throws BaseException {
        try{
            int search_id = categoryDao.createSearch(userIdx, postSearchReq.getCategory_name());
            return new PostSearchRes(search_id);
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetSearchRes getSearchList(int userIdx) throws BaseException{
        try{
<<<<<<< HEAD
            GetSearchRes getSearchResList = categoryDao.getSearchList(userIdx);
            return getSearchResList;
=======
            GetSearchRes getSearchRes=categoryDao.getSearchList(userIdx);
            return getSearchRes;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteOneSearch(int userIdx, int searchIdx) throws BaseException{
        if(categoryDao.getSearchStatus(userIdx, searchIdx).equals("N")) {
            throw new BaseException(ALREADY_DELETED_SEARCH);
        }

        try{
            int search_id=categoryDao.deleteOneSearch(userIdx, searchIdx);
            if(search_id==0)
                throw new BaseException(FAIL_DELETE_SEARCH_ONE);
>>>>>>> b9dc98d2ca437d72a5effa39c1a29b41043d56cd
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
