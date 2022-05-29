package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.Req.PatchReviewReq;
import com.example.demo.src.store.model.Req.PostReviewReq;
import com.example.demo.src.store.model.Res.GetMenuRes;
import com.example.demo.src.store.model.Res.GetStoreHomeRes;
import com.example.demo.src.store.model.Res.GetStoreInfoRes;
import com.example.demo.src.store.model.Res.GetStoreOneRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreService storeService, JwtService jwtService) {
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    /**
     * 아무런 필터 적용 없는 디폴트 메인 홈 화면
     * 위치 적용 필요
     */
    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<List<GetStoreHomeRes>> getHome() {
        try {
            List<GetStoreHomeRes> getStoreRes = storeService.getStoreResList();
            return new BaseResponse<>(getStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @GetMapping("/{storeIdx}")
    public BaseResponse<GetStoreOneRes> getStore(@PathVariable("storeIdx") Integer storeIdx){
        try {
            int userIdx= jwtService.getUserIdx();
            GetStoreOneRes storeOne = storeService.getStoreOne(storeIdx, userIdx);
            return new BaseResponse<>(storeOne);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @GetMapping("/info/{storeIdx}")
    public BaseResponse<GetStoreInfoRes> getStoreInfo(@PathVariable("storeIdx") Integer storeIdx){
        try {
            GetStoreInfoRes storeInfo = storeService.getStoreInfo(storeIdx);
            return new BaseResponse<>(storeInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @GetMapping("/{storeIdx}/{menuIdx}")
    public BaseResponse<GetMenuRes> getMenuInfo(@PathVariable("storeIdx") Integer storeIdx, @PathVariable("menuIdx") Integer menuIdx){
        try {
            GetMenuRes menuInfo = storeService.getMenuInfo(storeIdx, menuIdx);
            return new BaseResponse<>(menuInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/review")
    public BaseResponse<String> createReview(@Valid @RequestBody PostReviewReq postReviewReq) throws BaseException {
        int userIdx= jwtService.getUserIdx();
        storeService.createReview(userIdx, postReviewReq);
        return new BaseResponse<>("리뷰가 등록되었습니다.");
    }

    @ResponseBody
    @PatchMapping("/review/{reviewIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("reviewIdx") Integer reviewIdx, @Valid @RequestBody PatchReviewReq patchReviewReq) throws BaseException {
        int userIdx= jwtService.getUserIdx();
        storeService.modifyReview(userIdx, reviewIdx, patchReviewReq);
        return new BaseResponse<>("리뷰가 수정되었습니다.");
    }

    @ResponseBody
    @PatchMapping("/review/status/{reviewIdx}")
    public BaseResponse<String> deleteReview(@PathVariable("reviewIdx") Integer reviewIdx) throws BaseException {
        int userIdx= jwtService.getUserIdx();
        storeService.deleteReview(userIdx, reviewIdx);
        return new BaseResponse<>("리뷰가 삭제되었습니다.");
    }
}
