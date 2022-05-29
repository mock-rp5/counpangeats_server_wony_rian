package com.example.demo.src.store.model.Res;

import com.example.demo.src.store.model.MenuCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreOneRes {
    private Integer store_id;
    private String store_name;
    private String is_cheetah_delivery;
    private String store_main_image_url;
    private String delivery_time;
    private Integer start_delivery_fee;
    private Integer minimum_price;
    private Integer cnt;
    private Float average;
    private Integer isBookMark;
    private List<?> review;
    private List<MenuCategory> menuCategoryList;
}
