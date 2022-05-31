package com.example.demo.src.store.model.Res;

import com.example.demo.src.store.model.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreHomeRes {
    private List<StoreCategory> categoryList;
    private List<GetStoreRes> storeResList;
}
