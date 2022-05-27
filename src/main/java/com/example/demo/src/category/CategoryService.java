package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.CategorySimple;
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


    @Autowired
    public CategoryService(CategoryDao categoryDao){
        this.categoryDao=categoryDao;
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

}
