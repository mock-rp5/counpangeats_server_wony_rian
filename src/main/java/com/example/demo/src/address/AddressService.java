package com.example.demo.src.address;

import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressDao addressDao;

    public AddressService(AddressDao addressDao) {
        this.addressDao = addressDao;
    }
}
