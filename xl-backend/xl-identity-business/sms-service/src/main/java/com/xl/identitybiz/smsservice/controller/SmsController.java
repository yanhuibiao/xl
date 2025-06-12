package com.xl.identitybiz.smsservice.controller;

import com.xl.common.dto.ResponseEntity;
import com.xl.common.dubbo.api.SmsServiceTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ws")
public class SmsController {

    @Autowired
    SmsServiceTool smsServiceTool;

    @PostMapping("/broadcast")
    public ResponseEntity broadcast(@RequestParam String message) {
        smsServiceTool.broadcast(message);
        return ResponseEntity.okResult("SMS sent successfully");
    }

    @PostMapping("/sendTo/{msisdn}")
    public ResponseEntity sendTo(@PathVariable String msisdn, @RequestParam String message) {
        smsServiceTool.sendTo(message,msisdn);
        return ResponseEntity.okResult("SMS sent successfully");
    }

}