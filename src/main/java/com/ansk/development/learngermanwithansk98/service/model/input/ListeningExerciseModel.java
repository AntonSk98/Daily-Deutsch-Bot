package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * A model representing a listening exercise input.
 *
 * @author Anton Skripin
 */
public class ListeningExerciseModel extends AbstractCommandModel<ListeningExerciseModel> {
    private String audioId;

    /**
     * Getter for {@link #audioId}.
     * @return {@link #audioId}
     */
    public String getAudio() {
        return audioId;
    }

    /**
     * Setter for {@link #audioId}.
     * @param audio {@link #audioId}
     */
    public void setAudio(String audio) {
        this.audioId = audio;
    }

    @Override
    public AbstractCommandModel<ListeningExerciseModel> init() {
        return new ListeningExerciseModel();
    }
}
