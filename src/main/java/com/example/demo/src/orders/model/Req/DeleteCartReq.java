package com.example.demo.src.orders.model.Req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteCartReq {
    private Integer cart_id;
}
