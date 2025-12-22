package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the bot.
 *
 * @author Anton Skripin
 */
@ConfigurationProperties("bot")
public record BotConfigurationProperties(
    Long verifiedUser, Long groupId, String token, String resourceFolder) {}
