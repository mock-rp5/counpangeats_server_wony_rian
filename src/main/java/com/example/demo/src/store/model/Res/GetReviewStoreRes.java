package com.example.demo.src.store.model.Res;

import com.example.demo.src.store.model.StoreGetReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewStoreRes {
    private String store_name;
    private Integer count_star;
    private Float avg_star;
    private List<StoreGetReview> review;
}
