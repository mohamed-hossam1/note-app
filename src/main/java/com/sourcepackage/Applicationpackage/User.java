package com.sourcepackage.Applicationpackage;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String password;
    public UserFolder folder;


    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
        folder = new UserFolder(userName);

    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String toString(){
        return userName;
    }



}


