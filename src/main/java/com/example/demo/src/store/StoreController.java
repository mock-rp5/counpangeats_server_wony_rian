package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.GetStoreHomeRes;
import com.example.demo.src.store.model.GetStoreOneRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            GetStoreOneRes storeOne = storeService.getStoreOne(storeIdx);
            return new BaseResponse<>(storeOne);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
