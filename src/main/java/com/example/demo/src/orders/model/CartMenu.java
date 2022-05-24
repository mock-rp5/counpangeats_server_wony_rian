package com.example.demo.src.orders.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartMenu {
    private Integer cart_id;
    private Integer menu_count;
    private String total_price;
    private Integer menu_option_id;
}
