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
<<<<<<< HEAD
    //인기검색어 업데이트 시간
    //인기검색어 목록
    //검색어id, 카테고리 검색어, 검색 시간(월,일)(최근순)
    private String updated_time;
    private List<PopularSearch> popularSearchList;
    private List<Search> searchList;
=======
    private String updated_time;
    // 검색어, 검색 시간(월,일)(최근순)
    private List<PopularSearch> popularSearchList;
    private List<Search> searchList;


>>>>>>> b9dc98d2ca437d72a5effa39c1a29b41043d56cd

}
