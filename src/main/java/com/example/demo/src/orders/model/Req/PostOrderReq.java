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
    @NotNull(message = "address_id를 입력해주세요")
    private Integer address_id;
    @NotNull(message = "store_id를 입력해주세요")
    private Integer store_id;

    private String store_request;
}
