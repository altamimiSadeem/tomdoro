package com.notes.tomdoro.model;

/**
 * Created by C.M on 15/10/2018.
 */

public class SignUpData {

    public String name;
    public String email;

    public String Id;

    public SignUpData() {
    }

    public SignUpData(String name, String email, String id) {
        this.name = name;
        this.email = email;
        Id = id;
    }

    public SignUpData( String email, String name) {
        this.email = email;
        this.name = name;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
