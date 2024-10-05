package com.ansk.development.learngermanwithansk98.rest;

import com.ansk.development.learngermanwithansk98.integration.telegram.DailyDeutschBotConsumer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal API.
 *
 * @author Anton Skripin
 */
@RestController
@RequestMapping("/internal")
public class InternalController {

    private final DailyDeutschBotConsumer dailyDeutschBotConsumer;

    /**
     * Constructor.
     *
     * @param dailyDeutschBotConsumer See {@link DailyDeutschBotConsumer}
     */
    public InternalController(DailyDeutschBotConsumer dailyDeutschBotConsumer) {
        this.dailyDeutschBotConsumer = dailyDeutschBotConsumer;
    }

    /**
     * Restarts a bot.
     */
    @GetMapping("/restart-bot")
    public ResponseEntity<String> restartBot() {
        dailyDeutschBotConsumer.restartBot();
        return ResponseEntity.ok("Bot restarted!");
    }
}
