package com.example.demo.src.orders.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderReq {
    @NotNull(message = "주소 식별자를 입력해주세요")
    private Integer address_id;
    @NotNull(message = "가게 식별자를 입력해주세요")
    private Integer store_id;
    private Integer payment_method_id;
    private Integer delivery_request;
    private String store_request;
}
