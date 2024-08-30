package com.ansk.development.learngermanwithansk98.gateway;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * A consumer class for handling updates in a single-threaded, long-polling manner for the Daily Deutsch Bot.
 * It processes incoming updates by filtering with {@link FilterChain} and forwarding them to the {@link InputGateway}.
 *
 * @author Anton Skripin
 */
@Component
public class DailyDeutschBotConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final DailyDeutschBotConfiguration config;
    private final FilterChain filterChain;
    private final InputGateway inputGateway;


    public DailyDeutschBotConsumer(DailyDeutschBotConfiguration config,
                                   FilterChain filterChain,
                                   InputGateway inputGateway) {
        this.config = config;
        this.filterChain = filterChain;
        this.inputGateway = inputGateway;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void registerBot() {
        try {
            TelegramBotsLongPollingApplication longPollingApplication = new TelegramBotsLongPollingApplication();
            longPollingApplication.registerBot(config.token(), this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void consume(Update update) {
        filterChain.filter(update);
        inputGateway.process(update);
    }
}
