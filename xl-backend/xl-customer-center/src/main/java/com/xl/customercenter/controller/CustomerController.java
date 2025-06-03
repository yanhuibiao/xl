package com.xl.customercenter.controller;

import com.xl.common.dto.ResponseEntity;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.entity.Customer;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{username}")
    public ResponseEntity<?> getCustomer(@PathVariable String username) {
        Customer customer = customerService.findCustomerByUsername(username);
        return ResponseEntity.okResult(customer);
    }
}
