package com.example.demo.src.user.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressRes {
    private int address_id;
    private String main_address;
    private String detail_address;
    private String address_guide;
    private String status;
    private String address_name;

}
