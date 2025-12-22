package com.ansk.development.learngermanwithansk98.integration.telegram.sender;

import static com.ansk.development.learngermanwithansk98.integration.telegram.sender.TelegramSenderSupport.audioSender;
import static com.ansk.development.learngermanwithansk98.integration.telegram.sender.TelegramSenderSupport.documentSender;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Component to send writing exercises.
 *
 * @author Anton Skripin
 */
public class WritingExerciseSender {

  private final TelegramClient telegramClient;

  private static final String WRITING_EXERCISE_TEMPLATE =
      """
      ⭐️ #Writing <b>| %s</b>

      🗒️ <b>Share your thoughts on the following topic:</b>

          <b><i>%s</i></b>

      You can also always review our sample text for inspiration ⬆️

      🔹🔹🔹
      """;

  private static final String TEXT_CORRECTION_TEMPLATE =
      """
      ⭐️ #WritingCorrection

      ✏️ <b>In this exercise, you'll find two versions of the text:</b>
           📌 The original text.
           📌 The corrected version.

      🧐 Analyze the differences to sharpen your writing skills!

      🔹🔹🔹
      """;

  private static final String CORRECT_TEXT_AUDIO_TEMPLATE =
      """
      🔊 <b> Enhance Your Experience!</b>

      Listen to the AI audio version of the corrected text!

      🔹🔹🔹
      """;

  /**
   * Constructor.
   *
   * @param telegramClient See {@link TelegramClient}
   */
  public WritingExerciseSender(TelegramClient telegramClient) {
    this.telegramClient = telegramClient;
  }

  /**
   * Sends a writing exercise.
   *
   * @param chatId chat id
   * @param writingExercise writing exercise
   */
  public void sendWritingExercise(Long chatId, WritingExercise writingExercise) {
    final String writingExerciseText =
        String.format(WRITING_EXERCISE_TEMPLATE, writingExercise.level(), writingExercise.topic());

    var documentCaption = new TelegramSenderSupport.DocumentCaption(writingExerciseText, "HTML");
    var mediaParameters =
        new TelegramSenderSupport.DocumentRenderingParams(Optional.of(documentCaption), true);

    documentSender(chatId, writingExercise.exerciseDocument(), mediaParameters)
        .accept(telegramClient);
  }

  /**
   * Sends an exercise with the text and its corrected version.
   *
   * @param chatId chat id
   * @param originalTextDocument original text document
   * @param correctedTextDocument corrected text document
   */
  public void sendCorrectedText(
      Long chatId, ExerciseDocument originalTextDocument, ExerciseDocument correctedTextDocument) {
    ExerciseDocument mergedDocument = ExerciseDocument.of();
    Stream.concat(originalTextDocument.pages().stream(), correctedTextDocument.pages().stream())
        .forEach(mergedDocument::addPage);
    var documentRendererParams =
        new TelegramSenderSupport.DocumentRenderingParams(
            Optional.of(
                new TelegramSenderSupport.DocumentCaption(TEXT_CORRECTION_TEMPLATE, "HTML")),
            false);

    documentSender(chatId, mergedDocument, documentRendererParams).accept(telegramClient);
  }

  /**
   * Sends an audio of the corrected text.
   *
   * @param chatId chat id
   * @param audioStream audio stream
   */
  public void sendCorrectedTextAudio(Long chatId, InputStream audioStream) {
    audioSender(chatId, audioStream, "writing correction.mp3", CORRECT_TEXT_AUDIO_TEMPLATE)
        .accept(telegramClient);
  }
}
