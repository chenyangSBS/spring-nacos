package edu.sbs.ms.controller;

import edu.sbs.ms.config.DataSourceConfig;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Resource
    private DataSourceConfig dataSourceConfig;

    @RequestMapping("/get")
    public String getMessage(){
        log.info("url:" + dataSourceConfig.getUrl() + "</br>username:" + dataSourceConfig.getUsername() + "</br>password:" + dataSourceConfig.getPassword());
        return "url:" + dataSourceConfig.getUrl() + "</br>username:" + dataSourceConfig.getUsername() + "</br>password:" + dataSourceConfig.getPassword();
    }
}
