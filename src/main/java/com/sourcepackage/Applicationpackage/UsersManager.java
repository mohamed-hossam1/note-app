package com.sourcepackage.Applicationpackage;

import java.util.ArrayList;

public class UsersManager {
    private static final String sourcePath = "/home/mohamed/Desktop/Project/source package/Local storage/users";
    private static ArrayList<User> users;

    public UsersManager() {
        loadExistingUsers();
    }

    private void loadExistingUsers() {
        ArrayList<User> loadedUsers = FileManager.readUsers(sourcePath);
        if (loadedUsers != null) {
            users = loadedUsers;
        } else {
            users = new ArrayList<>();
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }


    public static void addUser(User user) {
        users.add(user);
        FileManager.updateUsers(users, sourcePath);
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void updateData() {
        FileManager.updateUsers(users, sourcePath);
    }

}
