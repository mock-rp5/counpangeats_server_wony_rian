package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.Req.*;
import com.example.demo.src.store.model.Res.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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

    //홈 화면
    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<GetStoreHomeRes> getHome(@RequestParam(required = false, defaultValue = "N") String is_cheetah,
                                                 @RequestParam(required = false, defaultValue = "100000") Integer delivery_fee,
                                                 @RequestParam(required = false, defaultValue = "0") Integer minimum_fee,
                                                 @RequestParam(required = false, defaultValue = "N") String is_Delivery) throws BaseException {
        if(delivery_fee < 0 ){
            throw new BaseException(GET_HOME_DELIVERY_FEE_FAIL);
        }
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

    //매장 정보 조회
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

    //리뷰 생성
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

    //리뷰 수정
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

    //리뷰 삭제
    @ResponseBody
    @PatchMapping("/reviews/{reviewIdx}/status")
    public BaseResponse<String> deleteReview(@PathVariable("reviewIdx") Integer reviewIdx) throws BaseException {
        try{
            if(reviewIdx == null){
                throw new BaseException(REVIEW_ID_EMPTY);
            }
            int userIdx= jwtService.getUserIdx();
            storeService.deleteReview(userIdx, reviewIdx);
            return new BaseResponse<>("리뷰가 삭제되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //가게 쿠폰 사용자에게 등록
    @ResponseBody
    @PostMapping("/coupons")
    public BaseResponse<String> createCouponUser(@Valid @RequestBody PostCouponUserReq postCouponUserReq) throws BaseException {
        try{
            int userIdx= jwtService.getUserIdx();

            int result = storeService.existsCouponUser(postCouponUserReq.getStore_id(), userIdx);
            if(result == 1){
                throw new BaseException(ALREADY_GET_COUPON);
            }
            storeService.createCouponUser(userIdx, postCouponUserReq.getStore_id());
            return new BaseResponse<>("쿠폰이 유저에게 등록되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //주문 내역에 따른 리뷰
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

    //후기 도움 돼요/안돼요 생성
    @ResponseBody
    @PostMapping("/reviews/sign")
    public BaseResponse<String> createSign(@Valid @RequestBody PostHelpReq postHelpReq) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();
            int checkReviewId = storeService.existsReview(postHelpReq.getReview_id());
            if(checkReviewId == 0){
                throw new BaseException(NO_EXISTS_REVIEW_ID);
            }
            storeService.createHelpSign(userIdx, postHelpReq);
            return new BaseResponse<>("리뷰 도움 유무가 반영되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //후기 도움 돼요/안돼요 삭제
    @ResponseBody
    @PatchMapping("/reviews/sign/status")
    public BaseResponse<String> deleteSign(@Valid @RequestBody PatchHelpReq patchHelpReq) throws BaseException {
        try{
            int userIdx= jwtService.getUserIdx();
            int checkReviewId = storeService.existsReview(patchHelpReq.getReview_id());
            if(checkReviewId == 0){
                throw new BaseException(NO_EXISTS_REVIEW_ID);
            }
            storeService.deleteHelpSign(userIdx, patchHelpReq);
            return new BaseResponse<>("리뷰 도움 유무가 삭제되었습니다.");
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
