package com.example.demo.src.category.model.Res;

import com.example.demo.src.category.model.PopularSearch;
import com.example.demo.src.category.model.Search;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchRes {
    private String updated_time;
    // 검색어, 검색 시간(월,일)(최근순)
    private List<PopularSearch> popularSearchList;
    private List<Search> searchList;



}
