package com.yx.nacos.controller;

import com.yx.nacos.config.DataSourceConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private DataSourceConfig dataSourceConfig;

    @RequestMapping("/get")
    public String getMessage(){
        return "url:" + dataSourceConfig.getUrl() + "</br>username:" + dataSourceConfig.getUsername() + "</br>password:" + dataSourceConfig.getPassword();
    }
}
