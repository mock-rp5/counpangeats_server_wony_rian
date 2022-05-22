package com.example.demo.src.address.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostAddressReq {
    private String main_address;
    private String detail_address;
    private String address_guid;
}
