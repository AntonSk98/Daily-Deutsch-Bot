package com.ansk.development.learngermanwithansk98.integration.telegram;

import static java.util.Optional.ofNullable;

import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Encapsulates utility methods to extract information from {@link Update} objects.
 *
 * @author Anton Skripin
 */
class TelegramMessageExtractor {

  private TelegramMessageExtractor() {}

  /**
   * Returns chat id.
   *
   * @param update update
   * @return chat id
   */
  static Long chatId(Update update) {
    return Optional.ofNullable(update.getMessage())
        .map(Message::getChatId)
        .or(
            () ->
                Optional.ofNullable(update.getCallbackQuery())
                    .map(CallbackQuery::getMessage)
                    .map(MaybeInaccessibleMessage::getChatId))
        .orElseThrow(() -> new IllegalStateException("Could not extract chat id"));
  }

  /**
   * Returns input of a user.
   *
   * @param update update
   * @return input
   */
  static String input(Update update) {
    return Optional.ofNullable(update.getMessage())
        .map(Message::getText)
        .or(
            () ->
                Optional.ofNullable(update.getMessage())
                    .map(Message::getAudio)
                    .map(Audio::getFileId))
        .orElse(null);
  }

  /**
   * Returns user id.
   *
   * @param update update
   * @return user id
   */
  static Long userId(Update update) {
    return Optional.ofNullable(update.getMessage())
        .map(Message::getChat)
        .map(Chat::getId)
        .or(
            () ->
                ofNullable(update.getCallbackQuery())
                    .map(CallbackQuery::getMessage)
                    .map(MaybeInaccessibleMessage::getChat)
                    .map(Chat::getId))
        .orElseThrow();
  }
}
