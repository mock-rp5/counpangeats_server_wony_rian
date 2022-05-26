package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//즐겨찾기 목록에 나오는 가게 요약 탭
//가게 사진, 가게 이름, 치타배달 여부, 리뷰 평균, 리뷰 갯수, 거리, 배달시간, 배달비 시작가격, 포장가능 여부
@Getter
@Setter
@AllArgsConstructor
public class StoreSimple {
    private String store_main_image_url;
    private String store_name;
    private String is_cheetah_delivery;
    private int review_avg;
    private int review_cnt;
    private double  distance;
    private int start_delivery_fee;
    private String is_takeout;
}
