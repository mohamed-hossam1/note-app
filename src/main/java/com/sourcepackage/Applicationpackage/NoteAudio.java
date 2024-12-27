package com.sourcepackage.Applicationpackage;

import java.io.Serializable;

public class NoteAudio implements Serializable {
    private String audioPath;

    public NoteAudio(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioPath() { return audioPath; }
}