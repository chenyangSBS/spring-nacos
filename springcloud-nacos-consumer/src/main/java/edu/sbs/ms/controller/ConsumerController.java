package edu.sbs.ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// rest request via RestTemplate
@RestController
public class ConsumerController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private final RestTemplate restTemplate;

    @Autowired
    public ConsumerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/test")
    public String test(@RequestParam String name) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
        String url = serviceInstance.getUri() + "/provider/welcome?name=" + name;
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return "Invoke : " + url + ", return : " + result;
    }

    @GetMapping("/metadata")
    public String metadata() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
        return "metadata : " + serviceInstance.getMetadata().toString();
    }
}
