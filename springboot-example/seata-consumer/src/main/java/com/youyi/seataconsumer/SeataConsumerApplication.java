package com.youyi.seataconsumer;

import io.seata.spring.boot.autoconfigure.SeataAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 排除 SeataAutoConfiguration
@SpringBootApplication(exclude = SeataAutoConfiguration.class)
public class SeataConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataConsumerApplication.class, args);
    }

}
