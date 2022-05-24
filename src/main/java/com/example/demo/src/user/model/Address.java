package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class Address {

    private int address_id;

    @NotBlank(message="메인 주소를 입력하세요.")
    private String main_address;

    @NotBlank(message="상세 주소를 입력하세요.")
    private String detail_address;
    private String address_guide;
    private int user_id;

    private double longitude;
    private double latitude;

    @NotBlank(message="주소 별칭을 입력하세요.")
    private String address_name;
}
