package com.example.demo.src.payment.Model.Req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCouponStoreReq {
    @NotNull(message = "쿠폰 식별자를 입력해주세요")
    private Integer couponIdx;
}
