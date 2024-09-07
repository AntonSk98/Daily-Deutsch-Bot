package com.ansk.development.learngermanwithansk98.service.model.input;

public class ListeningExerciseModel extends AbstractCommandModel<ListeningExerciseModel> {
    private String audioId;

    public String getAudio() {
        return audioId;
    }

    public void setAudio(String audio) {
        this.audioId = audio;
    }

    @Override
    public AbstractCommandModel<ListeningExerciseModel> init() {
        return new ListeningExerciseModel();
    }
}
