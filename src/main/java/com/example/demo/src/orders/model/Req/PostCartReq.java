package com.example.demo.src.orders.model.Req;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCartReq {
    @NotNull(message = "메뉴 개수를 선택해주세요.")
    private Integer menu_count;
    private Integer menu_option_id;
}
