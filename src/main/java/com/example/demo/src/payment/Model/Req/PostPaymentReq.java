package com.example.demo.src.payment.Model.Req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPaymentReq {
    private String payment_name;
    private String payment_number;
    private String payment_type;
}
