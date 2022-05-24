package com.example.demo.src.orders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.orders.model.DeleteCartReq;
import com.example.demo.src.orders.model.GetCartRes;
import com.example.demo.src.orders.model.PatchCartReq;
import com.example.demo.src.orders.model.PostCartReq;
import com.example.demo.src.store.model.GetStoreHomeRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;
    @Autowired
    private final JwtService jwtService;

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    //카트 생성
    @ResponseBody
    @PostMapping("/cart")
    public BaseResponse<String> createCart(@RequestParam(required = false) int store_id,
                                           @RequestParam(required = false) int menu_id, @RequestBody PostCartReq postCartReq) throws BaseException {
        try {
            // user 다 만들어지면 jwt 이용
            //int userIdx= jwtService.getUserIdx();

            int user_id = 1;
            int checkCartStore = orderService.checkCart(user_id);
            if (checkCartStore != 0 && store_id != checkCartStore) {
                return new BaseResponse<>(FAIL_DUPLICATE_CART);
            }
            orderService.createCart(user_id, store_id, menu_id, postCartReq);
            return new BaseResponse<>("카드에 담겼습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //카트 조회
    @ResponseBody
    @GetMapping("/cart")
    public BaseResponse<GetCartRes> getCartList() throws BaseException {
        //int userIdx= jwtService.getUserIdx();
        int userIdx = 1;

        GetCartRes cart = orderService.getCart(userIdx);
        return new BaseResponse<>(cart);
    }

    //카트 수정
    @ResponseBody
    @PatchMapping("/cart")
    public BaseResponse<String> modifyCart(@RequestParam(required = false) int store_id,
                                           @RequestParam(required = false) int cart_id, @RequestBody PatchCartReq patchCartReq) throws BaseException {
        // user 다 만들어지면 jwt 이용
        //int userIdx= jwtService.getUserIdx();
        int user_id = 1;

        if (store_id == 0 || cart_id == 0) {
            return new BaseResponse<>(PATCH_MODIFY_CART_EMPTY);
        }
        if (orderService.checkCartExists(cart_id) == 0) {
            return new BaseResponse<>(FAIL_MODIFY_CART_EMPTY);
        }
        orderService.modifyCart(store_id, cart_id, patchCartReq);
        return new BaseResponse<>("카트가 수정되었습니다.");
    }

    //카트 삭제
    @ResponseBody
    @PatchMapping("/cart/status")
    public BaseResponse<String> deleteCart(@RequestBody DeleteCartReq deleteCartReq) throws BaseException {
        // user 다 만들어지면 jwt 이용
        //int userIdx= jwtService.getUserIdx();
        int user_id = 1;

        if(orderService.checkCartExists(deleteCartReq.getCart_id()) == 0){
            return new BaseResponse<>(FAIL_MODIFY_CART_EMPTY);
        }
        orderService.deleteCart(deleteCartReq.getCart_id(), user_id);
        return new BaseResponse<>("카드가 삭제 되었습니다.");
    }
}
