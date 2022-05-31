package com.example.demo.src.payment.Model.Req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCashReq {
    @NotNull(message = "not null cash_number")
    private String cash_number;

    @NotNull(message = "not null status")
    private Integer status;
}
