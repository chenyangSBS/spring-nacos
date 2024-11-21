package edu.sbs.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        System.setProperty("spring.cloud.bootstrap.enabled", "true");
        SpringApplication.run(Application.class, args);
        System.out.println("Application启动成功");
    }
}
