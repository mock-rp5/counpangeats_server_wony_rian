package com.example.demo.src.orders.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchCartReq {
    private Integer count;
}
