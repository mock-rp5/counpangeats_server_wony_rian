package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.Req.PatchHelpReq;
import com.example.demo.src.store.model.Req.PatchReviewReq;
import com.example.demo.src.store.model.Req.PostHelpReq;
import com.example.demo.src.store.model.Req.PostReviewReq;
import com.example.demo.src.store.model.Res.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.STORE_ID_EMPTY;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreService storeService, JwtService jwtService) {
        this.storeService = storeService;
        this.jwtService = jwtService;
    }


    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<GetStoreHomeRes> getHome(@RequestParam(required = false, defaultValue = "N") String is_cheetah,
                                                 @RequestParam(required = false, defaultValue = "100000") Integer delivery_fee,
                                                 @RequestParam(required = false, defaultValue = "0") Integer minimum_fee,
                                                 @RequestParam(required = false, defaultValue = "N") String is_Delivery) throws BaseException {
        GetStoreHomeRes storeResList = storeService.getStoreResList(is_cheetah, delivery_fee, minimum_fee, is_Delivery);
        return new BaseResponse<>(storeResList);
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
    @PostMapping("/reviews")
    public BaseResponse<String> createReview(@Valid @RequestBody PostReviewReq postReviewReq) throws BaseException {
        try{
            int userIdx= jwtService.getUserIdx();
            storeService.createReview(userIdx, postReviewReq);
            return new BaseResponse<>("리뷰가 등록되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/reviews/{reviewIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("reviewIdx") Integer reviewIdx, @Valid @RequestBody PatchReviewReq patchReviewReq) throws BaseException {
        try{
            int userIdx= jwtService.getUserIdx();
            storeService.modifyReview(userIdx, reviewIdx, patchReviewReq);
            return new BaseResponse<>("리뷰가 수정되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/reviews/status/{reviewIdx}")
    public BaseResponse<String> deleteReview(@PathVariable("reviewIdx") Integer reviewIdx) throws BaseException {
        try{
            int userIdx= jwtService.getUserIdx();
            storeService.deleteReview(userIdx, reviewIdx);
            return new BaseResponse<>("리뷰가 삭제되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/reviews/orders/{orderIdx}")
    public BaseResponse<GetReviewOrderRes> getReviewOrder(@PathVariable("orderIdx") Integer orderIdx) throws BaseException {
        if(orderIdx == null){
            throw new BaseException(STORE_ID_EMPTY);
        }
        try{
            return new BaseResponse<>(storeService.orderReview(orderIdx));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/reviews/{storeIdx}")
    public BaseResponse<GetReviewStoreRes> getReviewStore(@PathVariable("storeIdx") Integer storeIdx) throws BaseException {
        if(storeIdx == null){
            throw new BaseException(STORE_ID_EMPTY);
        }
        try{
            return new BaseResponse<>(storeService.getReviewStore(storeIdx));
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/reviews/sign")
    public BaseResponse<String> createSign(@Valid @RequestBody PostHelpReq postHelpReq) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();
            storeService.createHelpSign(userIdx, postHelpReq);
            return new BaseResponse<>("리뷰 도움 유무가 반영되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/reviews/sign/status")
    public BaseResponse<String> deleteSign(@Valid @RequestBody PatchHelpReq patchHelpReq) throws BaseException {
        try{
            int userIdx= jwtService.getUserIdx();
            storeService.deleteHelpSign(userIdx, patchHelpReq);
            return new BaseResponse<>("리뷰 도움 유무가 삭제되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
