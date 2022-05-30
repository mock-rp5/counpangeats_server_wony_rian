package com.example.demo.src.store.model.Res;

import com.example.demo.src.store.model.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewOrderRes {
    private String StoreName;
    private Integer reviewStar;
    private String reviewPic;
    private Timestamp createdAt;
    private String reviewContent;
    private List<OrderMenu> OrderMenu;
}
