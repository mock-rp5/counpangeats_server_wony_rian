package com.example.demo.src.orders.model.Res;

import com.example.demo.src.orders.model.CartMenu;
import com.example.demo.src.orders.model.OrderMenuList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserOrder {
    private String store_name;
    private Integer order_info_id;
    private Timestamp created_at;
    private Integer total_price;
    private String detail_address;
    private Integer start_delivery_fee;
    private List<OrderMenuList> cartMenuList;
}
