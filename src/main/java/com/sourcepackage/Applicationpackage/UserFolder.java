package com.sourcepackage.Applicationpackage;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class UserFolder implements Serializable {
    private String folderPath;
    private String imagesPath;
    private String audioPath;
    private ArrayList<Note> Notes;

    public UserFolder(String userName) {
        this.folderPath = "/home/mohamed/Desktop/Project/source package/Local storage/" + userName ;
        this.imagesPath = folderPath + "/images/";
        this.audioPath = folderPath + "/audio/";
        this.Notes = new ArrayList<>();

        createDirectories();
    }

    private void createDirectories() {
        new File(folderPath).mkdirs();
        new File(imagesPath).mkdirs();
        new File(audioPath).mkdirs();
    }

    public ArrayList<Note> getNotes() {
        return Notes;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void deleteNote(Note deleteNote) {
        if (Notes.contains(deleteNote)) {
            File noteFile = new File(folderPath + '/' + deleteNote.getNoteName() + ".dat");

            for (NoteImage imagePath : deleteNote.getImagePaths()) {
                new File(imagePath.getImagePath()).delete();
            }

            for (NoteAudio audioPath : deleteNote.getAudioPaths()) {
                new File(audioPath.getAudioPath()).delete();
            }

            if (noteFile.delete()) {
                Notes.remove(deleteNote);
                UsersManager.updateData();
            }
        }
    }

    public Note createNote(String noteName) {
        Note newNote = new Note(noteName);
        Notes.add(newNote);
        FileManager.updateNote(newNote, folderPath + '/' + noteName + ".dat");
        UsersManager.updateData();
        return newNote;
    }

    public SecureNote createSecureNote(String noteName, String notePassword) {
        SecureNote newNote = new SecureNote(noteName, notePassword);
        Notes.add(newNote);
        FileManager.updateNote(newNote, folderPath + '/' + noteName + ".dat");
        UsersManager.updateData();
        return newNote;
    }

    public boolean ValidName(String noteName) {
        boolean flag = true;
        for (int i = 0; i < Notes.size(); i++) {
            if(Notes.get(i).getNoteName().equals((noteName))){
                flag = false;
                break;
            }

        }
        return  flag;
    }

    public String getFolderPath() {
        return folderPath;
    }
}