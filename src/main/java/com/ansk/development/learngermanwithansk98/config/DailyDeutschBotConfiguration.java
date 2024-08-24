package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Common configuration for the bot.
 *
 * @author Anton SKripin
 */
@Configuration
@ConfigurationProperties("bot")
public class DailyDeutschBotConfiguration {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
