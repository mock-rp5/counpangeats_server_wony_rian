package com.example.demo.src.payment.Req;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletePaymentReq {
    @NotNull(message="결제 식별자를 입력하세요.")
    private Integer payment_method_id;
}
