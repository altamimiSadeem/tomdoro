package com.notes.tomdoro.model;


public class NoteData {
    public String userId;
    public String name;


    public NoteData() {
    }

    public NoteData(String userId, String name) {
        this.userId = userId;
        this.name = name;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "CalenderData{" +
                "name='" + name + '\'' +
                "userId='" + userId + '\'' +
                '}';
    }
}
