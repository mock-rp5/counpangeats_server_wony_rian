package com.example.demo.src.store.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreHomeRes {
    private Integer store_id;
    private String store_name;
    private String is_cheetah_delivery;
    private String take_out;
    private String delivery_time;
    private String start_delivery_fee;
    private String store_main_image_url;
    private Integer RCnt;
    private Float RAvg;
}
