package com.ansk.development.learngermanwithansk98.service.model.output;

/**
 * Record to edit the transcription of the provided audio.
 * @param audio audio
 * @param transcription transcription that is to be edited
 *
 * @author Anton Skripin
 */
public record EditListeningExercisePrompt(String audio, String transcription) {
}
