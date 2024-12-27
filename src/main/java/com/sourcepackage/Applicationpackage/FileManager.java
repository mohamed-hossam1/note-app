package com.sourcepackage.Applicationpackage;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    public static void updateUsers(ArrayList<User> users, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(users);
            System.out.println("Users data updated successfully.");
        } catch (IOException e) {
            System.err.println("Error saving users to file: " + e.getMessage());
        }
    }

    public static ArrayList<User> readUsers(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Creating new users list");
            return new ArrayList<>();
        }
    }

    public static void updateNote(Note note, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(note);
            System.out.println("Note saved successfully: " + note.getNoteName());
        } catch (IOException e) {
            System.err.println("Error saving note: " + e.getMessage());
        }
    }


}