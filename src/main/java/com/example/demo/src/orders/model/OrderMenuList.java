package com.example.demo.src.orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMenuList {
    private String menu_name;
    private String option_name;
    private Integer menu_count;
    private Integer menu_price;
    private Integer option_price;
}
