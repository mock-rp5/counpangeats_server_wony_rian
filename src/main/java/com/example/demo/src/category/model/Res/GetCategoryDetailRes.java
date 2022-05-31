package com.example.demo.src.category.model.Res;

import com.example.demo.src.category.model.CategorySimple;
import com.example.demo.src.category.model.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCategoryDetailRes {

    //카테고리 배너 리스트
    private List<CategorySimple> categorySimpleList;

    //카테고리 상세조회의 식당 리스트
    //식당 id, 식당 이미지, 식당 이름, 치타배달 여부, 포장 시간, 리퓨 평점, 리뷰 갯수, 거리, 배달 시작 가격, 포장가능 여부
    private List<StoreInfo> storeInfoList;

}
