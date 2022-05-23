package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreHomeRes {
    private String store_name;
    private String is_cheetah_delivery;
    private String store_main_image_url;
    private Integer RCnt;
    private Float RAvg;
}
