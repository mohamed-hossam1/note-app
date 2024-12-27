package com.sourcepackage.Applicationpackage;

public class SecureNote extends Note {
    private String notePassword;

    public SecureNote(String noteName, String notePassword) {
        super(noteName);
        this.notePassword = PasswordUtils.hashPassword(notePassword);
    }

    public String getNotePassword() {
        return notePassword;
    }
}
