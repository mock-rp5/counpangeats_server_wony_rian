package com.example.demo.src.store.model.Res;

import com.example.demo.src.store.model.MenuDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchMenu {
    private List<MenuDetail> menuList;
}
