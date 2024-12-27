package com.sourcepackage.Applicationpackage;

import java.io.Serializable;
import java.util.ArrayList;

public class Note implements Serializable {
    private String noteName;
    private String textContent;
    private ArrayList<NoteImage> imagePaths;
    private ArrayList<NoteAudio> audioPaths;
    private ArrayList<Sketch> sketches;

    public Note(String noteName) {
        this.noteName = noteName;
        initializeLists();
    }



    private void initializeLists() {
        imagePaths = new ArrayList<>();
        audioPaths = new ArrayList<>();
        sketches = new ArrayList<>();
    }

    public String getNoteName() {
        return noteName;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void addImagePath(String path) {
        NoteImage noteImage = new NoteImage(path);
        imagePaths.add(noteImage);
    }

    public void removeImagePath(String path) {
        imagePaths.remove(path);
    }

    public ArrayList<NoteImage> getImagePaths() {
        return imagePaths;
    }

    public void addAudioPath(String path) {
        NoteAudio noteAudio = new NoteAudio(path);
        audioPaths.add(noteAudio);
    }


    public ArrayList<NoteAudio> getAudioPaths() {
        return audioPaths;
    }

    public void addSketchPoint(Sketch point) {
        sketches.add(point);
    }

    public ArrayList<Sketch> getSketchPoints() {
        return sketches;
    }

    public void clearSketchPoints() {
        sketches.clear();
    }
}