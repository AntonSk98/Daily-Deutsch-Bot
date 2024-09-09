package com.ansk.development.learngermanwithansk98.gateway.telegram;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.TimeUnit;

/**
 * A consumer class for handling updates in a single-threaded, long-polling manner for the Daily Deutsch Bot.
 * This class processes incoming updates by filtering them using a {@link FilterChain}.
 * It then forwards the message to the {@link InputGateway}.
 *
 * @author Anton Skripin
 */
@Component
public class DailyDeutschBotConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final DailyDeutschBotConfiguration config;
    private final FilterChain filterChain;
    private final InputGateway inputGateway;
    private TelegramBotsLongPollingApplication longPollingApplication;


    /**
     * Constructor.
     *
     * @param config       See {@link DailyDeutschBotConfiguration}
     * @param filterChain  See {@link FilterChain}
     * @param inputGateway See {@link InputGateway}
     */
    public DailyDeutschBotConsumer(DailyDeutschBotConfiguration config,
                                   FilterChain filterChain,
                                   InputGateway inputGateway) {
        this.config = config;
        this.filterChain = filterChain;
        this.inputGateway = inputGateway;
    }

    /**
     * Registers the bot when the application starts.
     */
    @EventListener(ApplicationStartedEvent.class)
    public void registerBot() {
        longPollingApplication = new TelegramBotsLongPollingApplication();
        try {
            longPollingApplication.registerBot(config.token(), this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Consumes the given {@link Update} by filtering it and processing it through the {@link InputGateway}.
     *
     * @param update the update to be consumed.
     */
    @Override
    public void consume(Update update) {
        filterChain.filter(update);
        inputGateway.process(update);
    }

    /**
     * Ensures that the bot is running by periodically checking and starting the {@link TelegramBotsLongPollingApplication}.
     */
    @Scheduled(initialDelay = 10, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void ensureBotRunning() {
        if (longPollingApplication.isRunning()) {
            return;
        }

        try {
            longPollingApplication.start();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
