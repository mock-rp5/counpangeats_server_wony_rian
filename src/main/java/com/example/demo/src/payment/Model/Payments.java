package com.example.demo.src.payment.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payments {
    private Integer payment_id;
    private String payment_name;
    private String payment_number;
    private String payment_type;

}
