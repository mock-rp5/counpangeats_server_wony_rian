package com.example.demo.src.payment.Model.Res;

import com.example.demo.src.payment.Model.Payments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPaymentRes {
    private String cash_number;
    private List<Payments> paymentsList;

}
