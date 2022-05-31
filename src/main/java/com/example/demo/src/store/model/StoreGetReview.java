package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreGetReview {
    private String user_name;
    private String review_content;
    private String review_image_url;
    private Integer review_star;
    private Timestamp created_at;
    private List<OrderMenu> orderMenu;

}
