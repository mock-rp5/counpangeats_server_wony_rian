package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MyEatsInfo {
    private String user_name;
    private String user_phone;
    private List<Ad> adList;
}
