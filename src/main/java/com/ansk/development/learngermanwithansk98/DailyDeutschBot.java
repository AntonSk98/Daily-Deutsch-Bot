package com.ansk.development.learngermanwithansk98;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class DailyDeutschBot {

    public static void main(String[] args) {
        SpringApplication.run(DailyDeutschBot.class, args);
    }

}
