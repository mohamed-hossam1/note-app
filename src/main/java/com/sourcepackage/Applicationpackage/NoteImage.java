package com.sourcepackage.Applicationpackage;

import java.io.Serializable;

public class NoteImage implements Serializable {
    private String imagePath;

    public NoteImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() { return imagePath; }
}