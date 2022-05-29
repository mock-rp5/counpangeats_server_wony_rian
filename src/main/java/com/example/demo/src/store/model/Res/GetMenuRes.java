package com.example.demo.src.store.model.Res;

import com.example.demo.src.store.model.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMenuRes {
    private Integer menu_id;
    private String menu_name;
    private String menu_img_url;
    private Integer menu_price;
    private List<MenuOption> menu_option;
}
