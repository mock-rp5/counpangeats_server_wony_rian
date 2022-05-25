package com.example.demo.src.user.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressSimpleRes { //주소 목록 조회에서 화면에서 정보들
    private String address_name;
    private String main_address;
    private String detail_address;
    private String status;
}
