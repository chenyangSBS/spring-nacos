package edu.sbs.ms.config;

import com.netflix.loadbalancer.*;
import feign.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CustomFeignConfiguration {

    @Bean
    @LoadBalanced
    public IRule myRule(){
        // 随机策略
//        return new RandomRule();
        // 最低并发策略
//        return new BestAvailableRule();
        // 轮询策略
        return new RoundRobinRule();
        // 响应时间加权重策略
//        return new ResponseTimeWeightedRule();
        // 区域权重策略
//        return new ZoneAvoidanceRule();

    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}

