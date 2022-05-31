package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreInfo {
    //카테고리 상세조회의 식당 리스트
    //식당 id, 식당 이미지, 식당 이름, 치타배달 여부, 포장 시간, 리퓨 평점, 리뷰 갯수, 거리, 배달 시작 가격, 포장가능 여부
    private int store_id;
    private String store_image_url;
    private String store_name;
    private String is_cheetah_delivery;
    private String takeout_time;
    private double avg_review;
    private int count_review;
    private int start_delivery_fee;
    private String is_takeout;

}
