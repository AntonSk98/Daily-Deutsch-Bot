package com.ansk.development.learngermanwithansk98;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class DailyDeutschBot {

    public static void main(String[] args) {
        StringUtils.splitByWholeSeparator("Anton anton banton canton", "an");
        SpringApplication.run(DailyDeutschBot.class, args);
    }

}
