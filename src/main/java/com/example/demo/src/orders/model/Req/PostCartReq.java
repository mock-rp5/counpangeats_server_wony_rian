package com.example.demo.src.orders.model.Req;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCartReq {
    private Integer menu_count;
    private Integer menu_option_id;
}
