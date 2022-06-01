package com.example.demo.src.payment.Model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetCouponRes {
    private String coupon_name;
    private Integer discount_price;
    private String discount_condition;
    private Timestamp expiration_date;
}
