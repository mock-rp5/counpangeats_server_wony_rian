package com.example.demo.src.orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCartRes {
    private String store_name;
    private String is_cheetah_delivery;
    private List<CartMenu> cartMenu;
}
