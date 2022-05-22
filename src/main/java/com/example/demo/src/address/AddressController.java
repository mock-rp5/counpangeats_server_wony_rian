package com.example.demo.src.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }



}
