package com.xl.identitybiz.service.controller;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/servlets")
    public Map<String, String> listServlets() {
        Map<String, String> servletMappings = new HashMap<>();
        servletContext.getServletRegistrations().forEach((k, v) -> {
            servletMappings.put(k, v.getMappings().toString());
        });
        return servletMappings;
    }
}
