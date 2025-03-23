package com.xl.identitybusiness.controller;

import com.xl.common.dto.ResponseEntity;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.entity.Administrator;
import com.xl.common.dubbo.entity.Customer;
import com.xl.identitybusiness.service.impl.CustomerServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Customer customer) {
        Map<String, Object> responseObject = customerService.registerCustomer(customer);
        return ResponseEntity.okResult(responseObject);
    }
}
