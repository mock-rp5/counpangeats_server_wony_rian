package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.CategorySimple;
import com.example.demo.src.category.model.Req.PostSearchReq;
import com.example.demo.src.category.model.Res.GetSearchRes;
import com.example.demo.src.category.model.Res.PostSearchRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    /**
     * 검색어 생성 API
     * [POST] /category/search
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/search")
    public BaseResponse<PostSearchRes> createSearch(@RequestBody PostSearchReq postSearchReq) {

        try {
            //jwt에서 idx 추출.
            int userIdx = jwtService.getUserIdx();
            PostSearchRes postSearchRes= categoryService.createSearch(userIdx, postSearchReq);

            return new BaseResponse<>(postSearchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 최근검색목록 조회 API
     * [GET] /category/search
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<GetSearchRes> getSearchList() {

        try {
            //jwt에서 idx 추출.
            int userIdx = jwtService.getUserIdx();
            GetSearchRes getSearchRes= categoryService.getSearchList(userIdx);

            return new BaseResponse<>(getSearchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색어 한개 삭제 API
     * [PATCH] /category/search/:searchIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/search/{searchIdx}")
    public BaseResponse<String> deleteOneSearch(@PathVariable("searchIdx") int searchIdx ){
        try {
            //jwt에서 idx 추출.
            int userIdx = jwtService.getUserIdx();
            categoryService.deleteOneSearch(userIdx,searchIdx);
            String result=searchIdx+"번 검색어가 삭제되었습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 검색어 전체 삭제 API
     * [PATCH] /category/search/:searchIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/search")
    public BaseResponse<String> deleteAllSearch( ){
        try {
            //jwt에서 idx 추출.
            int userIdx = jwtService.getUserIdx();
            categoryService.deleteAllSearch(userIdx);
            String result="전체 검색어가 삭제되었습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
