package com.ansk.development.learngermanwithansk98;

import com.ansk.development.learngermanwithansk98.gateway.telegram.DailyDeutschBotConsumer;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.telegram.TelegramOutputGateway;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestConfiguration
public class DailyDeutschBotTestConfig {

    @Bean
    @Primary
    public DailyDeutschBotConsumer dailyDeutschBotConsumer() {
        return Mockito.mock(DailyDeutschBotConsumer.class);
    }

    @Bean
    @Primary
    public TelegramOutputGateway telegramOutputGateway() {
        return Mockito.mock(TelegramOutputGateway.class);
    }
}
