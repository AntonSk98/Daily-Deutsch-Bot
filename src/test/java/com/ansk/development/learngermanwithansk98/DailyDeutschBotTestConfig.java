package com.ansk.development.learngermanwithansk98;

import com.ansk.development.learngermanwithansk98.integration.telegram.DailyDeutschBotConsumer;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.TelegramClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;

@TestConfiguration
public class DailyDeutschBotTestConfig {

    @Bean
    @Primary
    public LongPollingSingleThreadUpdateConsumer dailyDeutschBotConsumer() {
        return Mockito.mock(DailyDeutschBotConsumer.class);
    }

    @Bean
    @Primary
    public ITelegramClient telegramClient() {
        return Mockito.mock(TelegramClient.class);
    }
}
