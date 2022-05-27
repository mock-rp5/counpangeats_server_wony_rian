package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreInfoRes {
    private String store_name;
    private String store_address;
    private String store_phone;
    private String store_ceo_name;
    private String business_number;
    private String search_business_name;
    private String find_store_tip;
    private String business_hours;
    private String store_description;
}
