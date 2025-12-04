package edu.sbs.ms.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import edu.sbs.ms.config.DataSourceConfig;
import edu.sbs.ms.entity.User;
import edu.sbs.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.Resource;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Resource
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private UserService userService;

    @Value("${config.length}")
    private String length;

    @GetMapping("/get")
    public String getMessage(){
        log.info("url:" + dataSourceConfig.getUrl() + "</br>username:" + dataSourceConfig.getUsername() + "</br>password:" + dataSourceConfig.getPassword());
        return "url:" + dataSourceConfig.getUrl() + "</br>username:" + dataSourceConfig.getUsername() + "</br>password:" + dataSourceConfig.getPassword();
    }

    @GetMapping("/user/{alias}")
    public String getUser(@PathVariable String alias){
        List<User> userList = userService.getStatsForUser(alias);
        return userList.toString();
    }

    @GetMapping("/length")
    public String getLength(){
        return "current length is: " +length;
    }
}
