package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.CategorySimple;
import com.example.demo.src.category.model.Req.PostSearchReq;
import com.example.demo.src.category.model.Res.PostSearchRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

}
