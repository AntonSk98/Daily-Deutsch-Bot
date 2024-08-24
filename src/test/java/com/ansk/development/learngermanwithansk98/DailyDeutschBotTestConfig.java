package com.ansk.development.learngermanwithansk98;

import com.ansk.development.learngermanwithansk98.gateway.DailyDeutschBotConsumer;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class DailyDeutschBotTestConfig {

    @Bean
    @Primary
    public DailyDeutschBotConsumer dailyDeutschBot() {
        return Mockito.mock(DailyDeutschBotConsumer.class);
    }

    @Bean
    @Primary
    public DailyDeutschBotConfiguration dailyDeutschBotConfig() {
        DailyDeutschBotConfiguration mockConfig = Mockito.mock(DailyDeutschBotConfiguration.class);
        Mockito.when(mockConfig.getToken()).thenReturn("12345:YOUR_TOKEN");
        return mockConfig;
    }
}
