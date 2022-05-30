package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDetail {
    private Integer menu_id;
    private String menu_name;
    private String menu_img_url;
    private String menu_description;
    private Integer menu_price;

}
