package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class PopularSearch {
    private int p_search_id;
    private int search_rank;
    private String category_name;
}
