package com.example.demo.src.payment.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPaymentRes {
    private String payment_name;
    private String payment_number;
    private String payment_type;
}
