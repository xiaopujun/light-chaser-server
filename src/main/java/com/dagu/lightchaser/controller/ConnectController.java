package com.dagu.lightchaser.controller;

import com.dagu.lightchaser.service.ConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lc/connect")
public class ConnectController {

    @Autowired
    private ConnectService connectService;

    @GetMapping("/test")
    public String testConnect(String name, String pwd) {
        return connectService.testConnect(name, pwd);
    }
}
