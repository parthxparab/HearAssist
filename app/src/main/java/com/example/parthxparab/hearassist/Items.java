package com.example.parthxparab.hearassist;

public class Items {

    private int id;
    private String name;
    private String path;
    private String user;
    private String age;


    public Items(String name, String path, int id, String user, String age) {
        this.name = name;
        this.path = path;
        this.id = id;
        this.user = user;
        this.age = age;

    }
    public int geId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getAge() {
        return age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return path;
    }
}
