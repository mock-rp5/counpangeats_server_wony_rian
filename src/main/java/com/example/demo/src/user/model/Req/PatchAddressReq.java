package com.example.demo.src.user.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class PatchAddressReq {

    // 주소 수정할 때, 바꾼부분만 변경하고 건드리지 않은 부분은 기존 디비에있던 정보를 그대로 저장하면 되는데 로직을 어떻게 해야할지 모르겠다.
    // detail_address 같은 경우, 빈칸으로 입력하면 안되니까 NotEmpty 처리를 해야하는데, 빈칸으로 둔것==기존 데이터 그대로 저장 이라는 말도 되니까 어떻게 해야하지 ...
    // 클라이언트의 영역인걸까 ...
    // 우선 주소 생성할 때랑 마찬가지로 디비 설정값이 Not NULL인 값에 대해서  전부 NotEmpty 처리를 하도록 했다.!
    private int user_id;

    private String detail_address;

    private String address_guide;

    @NotBlank(message="주소 분류를 지정해주세요.")
    private String status;

    @NotBlank(message="주소 별칭을 입력해주세요.")
    private String address_name;

    private double longitude;
    private double latitude;


}
