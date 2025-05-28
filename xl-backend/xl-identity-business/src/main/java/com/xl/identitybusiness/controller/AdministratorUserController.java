package com.xl.identitybusiness.controller;

import com.xl.common.dubbo.entity.Administrator;
import com.xl.common.dto.ResponseEntity;
import com.xl.identitybusiness.service.impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdministratorUserController {

    @Autowired
    AdministratorServiceImpl administratorService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Administrator administrator) {
        String register = administratorService.register(administrator);
        return ResponseEntity.okResult(register);

    }

    @GetMapping
    public ResponseEntity<?> getAdministrators(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String roleId,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createTimeStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createTimeEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime passwordExpireTimeStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime passwordExpireTimeEnd) {
        List<Administrator> administrators = administratorService.findAdministrators(username, status, roleId, phone, createTimeStart, createTimeEnd, passwordExpireTimeStart, passwordExpireTimeEnd);
        for (Administrator administrator : administrators) {
            administrator.setPassword("");
        }
        return ResponseEntity.okResult(administrators);
    }

}
