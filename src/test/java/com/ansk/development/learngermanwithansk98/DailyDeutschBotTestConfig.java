package com.ansk.development.learngermanwithansk98;

import com.ansk.development.learngermanwithansk98.integration.telegram.DailyDeutschBotConsumer;
import com.ansk.development.learngermanwithansk98.integration.telegram.TelegramClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class DailyDeutschBotTestConfig {

    @Bean
    @Primary
    public DailyDeutschBotConsumer dailyDeutschBotConsumer() {
        return Mockito.mock(DailyDeutschBotConsumer.class);
    }

    @Bean
    @Primary
    public TelegramClient telegramOutputGateway() {
        return Mockito.mock(TelegramClient.class);
    }
}
