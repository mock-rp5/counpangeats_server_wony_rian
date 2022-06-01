package com.example.demo.src.payment.Model.Req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCouponReq {
    @NotNull(message = "쿠폰 번호를 입력해주세요")
    private String coupon_description;
}
