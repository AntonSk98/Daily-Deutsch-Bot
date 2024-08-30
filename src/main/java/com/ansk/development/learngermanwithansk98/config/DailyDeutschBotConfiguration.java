package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Common configuration for the bot.
 *
 * @author Anton SKripin
 */
@ConfigurationProperties("bot")
public record DailyDeutschBotConfiguration(String token) {
}
