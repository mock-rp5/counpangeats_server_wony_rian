package com.example.demo.src.orders;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.orders.model.Req.DeleteCartReq;
import com.example.demo.src.orders.model.Res.GetCartRes;
import com.example.demo.src.orders.model.Req.PatchCartReq;
import com.example.demo.src.orders.model.Req.PostCartReq;
import com.example.demo.src.orders.model.Req.PostOrderReq;
import com.example.demo.src.orders.model.Res.GetUserOrder;
import com.example.demo.src.orders.model.Res.PostOrderRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/orders")
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
    @PostMapping("/carts")
    public BaseResponse<String> createCart(@RequestParam int storeIdx,
                                           @RequestParam int menuIdx, @Valid @RequestBody PostCartReq postCartReq) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();

            //같은 메뉴 있는 지 확인
            int sameMenu = orderService.checkCartMenu(menuIdx, userIdx, storeIdx, postCartReq);
            if(sameMenu != 0){
                return new BaseResponse<>("카트에 담겼습니다.");
            }
            // 메뉴 옵션 식별자가 메뉴에 속한 지 확인
            int i = orderService.checkMenuAndOption(menuIdx, postCartReq.getMenu_option_id());
            if(i == 0){
                return new BaseResponse<>(FAIL_CART_MISMATCH);
            }

            orderService.createCart(userIdx, storeIdx, menuIdx, postCartReq);
            return new BaseResponse<>("카드에 담겼습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //카트 조회
    @ResponseBody
    @GetMapping("/carts")
    public BaseResponse<GetCartRes> getCartList() throws BaseException {
        int userIdx= jwtService.getUserIdx();

        GetCartRes cart = orderService.getCart(userIdx);
        return new BaseResponse<>(cart);
    }

    //카트 수정
    @ResponseBody
    @PatchMapping("/carts")
    public BaseResponse<String> modifyCart(@RequestParam int storeIdx,
                                           @RequestParam int cartIdx, @RequestBody PatchCartReq patchCartReq) throws BaseException {
        int userIdx= jwtService.getUserIdx();

        if (storeIdx == 0 || cartIdx == 0) {
            return new BaseResponse<>(PATCH_MODIFY_CART_EMPTY);
        }
        if (orderService.checkCartExists(cartIdx) == 0) {
            return new BaseResponse<>(FAIL_CART_EMPTY);
        }
        orderService.modifyCart(storeIdx, cartIdx, patchCartReq);
        return new BaseResponse<>("카트가 수정되었습니다.");
    }

    //카트 삭제
    @ResponseBody
    @PatchMapping("/carts/status")
    public BaseResponse<String> deleteCart(@RequestBody DeleteCartReq deleteCartReq) throws BaseException {
        int user_id= jwtService.getUserIdx();
        if(orderService.checkCartExists(deleteCartReq.getCart_id()) == 0){
            return new BaseResponse<>(FAIL_CART_EMPTY);
        }
        System.out.println("user_id = " + user_id);
        orderService.deleteCart(deleteCartReq.getCart_id(), user_id);
        return new BaseResponse<>("카드가 삭제 되었습니다.");
    }

    //카트 다시 담기
    @ResponseBody
    @PostMapping("/carts/new")
    public BaseResponse<String> restartCart(@RequestParam int storeIdx,
                                            @RequestParam int menuIdx, @Valid @RequestBody PostCartReq postCartReq) throws BaseException {
        try {
            int userIdx= jwtService.getUserIdx();

            int now_store_id = orderService.checkCart(userIdx);
            System.out.println("now_store_id = " + now_store_id);
            if(now_store_id == 0){
                return new BaseResponse<>(FAIL_CART_NEW);
            }

            orderService.restartCart(userIdx, storeIdx, menuIdx, now_store_id, postCartReq);
            return new BaseResponse<>("이전 카드 삭제 후 다시 담았습니다.");
        } catch (BaseException exception) {

            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //주문 생성
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createOrder(@RequestParam Integer[] cartList, @Valid @RequestBody PostOrderReq postOrderReq){
        try {
            int user_id= jwtService.getUserIdx();
            orderService.createOrder(user_id, cartList, postOrderReq);
            return new BaseResponse<>("주문이 완료되었습니다.");

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    // 재주문
    @ResponseBody
    @PostMapping("/reorders")
    public BaseResponse<String> reOrder(@RequestParam(required = false) int[] cartList){
        try {
            int user_id= jwtService.getUserIdx();

            List<PostOrderRes> postOrderRes = orderService.checkCartUserExists(user_id);
            if(!postOrderRes.isEmpty()){
                return new BaseResponse<>(FAIL_DUPLICATE_CART);
            }

            orderService.reOrder(user_id, cartList);

            return new BaseResponse<>("재주문이 완료되었습니다.");

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 재주문 새로 카트 담기
    @ResponseBody
    @PostMapping("/carts/reorders")
    public BaseResponse<String> reOrderFix(@RequestParam(required = false) int[] cartList){
        try {
            int user_id= jwtService.getUserIdx();

            int now_store_id = orderService.checkCart(user_id);
            System.out.println("now_store_id = " + now_store_id);
            if(now_store_id == 0){
                return new BaseResponse<>(FAIL_CART_NEW);
            }
            orderService.restartOrder(user_id, now_store_id, cartList);

            return new BaseResponse<>("카트에 새로 담겼습니다.");

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    //주문 목록 조회 API
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetUserOrder>> getOrder() throws BaseException{
        int user_id= jwtService.getUserIdx();

        return new BaseResponse<>(orderService.getOrderList(user_id));
    }

    //주문 내역 검색 API
    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<List<GetUserOrder>> getOrder(@RequestParam(required = false) String menuName) throws BaseException{
        int user_id= jwtService.getUserIdx();

        return new BaseResponse<>(orderService.getSearchMenu(user_id, menuName));
    }
    //주문 준비중 목록 조회 API
    @ResponseBody
    @GetMapping("/ready")
    public BaseResponse<List<GetUserOrder>> getReadyOrder() throws BaseException{
        int user_id= jwtService.getUserIdx();

        return new BaseResponse<>(orderService.getOrderReadyList(user_id));
    }
    //영수증 조회
    @ResponseBody
    @GetMapping("/results")
    public BaseResponse<List<GetUserOrder>> getUserOrder(@RequestParam int storeIdx,
                                                         @RequestParam int orderIdx) throws BaseException{
        try {
            int user_id= jwtService.getUserIdx();

            List<GetUserOrder> orderListOne = orderService.getOrderListOne(user_id, orderIdx, storeIdx);
            return new BaseResponse<>(orderListOne);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}