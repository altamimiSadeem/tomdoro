package com.notes.tomdoro.model;


public class ToDoData {
    public String userId;
    public String id;

    public String name;
    public Boolean checked;


    public ToDoData() {
    }

    public ToDoData(String userId, String id, String name, Boolean checked) {
        this.userId = userId;
        this.id = id;
        this.name = name;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "ToDoData{" +
                "userId='" + userId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", checked=" + checked +
                '}';
    }
}
