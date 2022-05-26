package com.example.demo.src.user.model.Res;

import com.example.demo.src.user.model.StoreSimple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//즐겨찾기 목록 조회
// 총 즐겨찾기 갯수, 가게 사진, 가게 이름, 치타배달 여부, 리뷰 평균, 리뷰 갯수, 거리, 배달시간, 배달비, 포장가능 여부
// 정렬 필터 - 자주 주문한 순, 최근 주문한 순, 최근 추가한 순
@Getter
@Setter
@AllArgsConstructor
public class GetBookmarkRes {
        private int bookmark_count;
        private List<StoreSimple> storeSimpleList;
}
