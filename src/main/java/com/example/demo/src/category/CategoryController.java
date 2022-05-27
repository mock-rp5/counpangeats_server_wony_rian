package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.CategorySimple;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final JwtService jwtService;

    public CategoryController(CategoryService categoryService,JwtService jwtService){
        this.categoryService=categoryService;
        this.jwtService=jwtService;
    }

    /**
     * 카테고리 목록 조회 API
     * [GET] /category
     * @return BaseResponse<PostOrderRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<CategorySimple>> getCategoryList(){
        try {
            List<CategorySimple> categorySimpleList=categoryService.getCategoryList();

            return new BaseResponse<>(categorySimpleList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
