package com.sample.rocketmq.ons2apache.mask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EventMainApplication {


    public static void main(String[] args) {
        SpringApplication.run(EventMainApplication.class, args);
    }

    /**
     * 伪装的ons提供的接口，是的通过老的方式接入mq的代码也无需更改，只改配置
     */
    @RequestMapping("/rocketmq/nsaddr4client-internal")
    public String maskOns() {
        System.out.println("invoke ons");
        return "192.168.54.112:9876";
    }

}
