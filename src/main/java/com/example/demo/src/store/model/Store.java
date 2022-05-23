package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    private Integer store_id;
    private String store_name;
    private String store_address;
    private Integer category_id;
    private String store_phone;
    private String store_ceo_name;
    private String business_number;
    private String search_business_name;
    private String find_store_tip;
    private String business_hours;
    private String store_description;
    private String store_main_image_url;
    private String is_cheetah_delivery;
    private String status;
    private Timestamp created_at;
    private Timestamp updated_at;
}
