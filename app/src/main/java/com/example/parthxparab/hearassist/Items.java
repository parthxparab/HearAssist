package com.example.parthxparab.hearassist;

public class Items {

    private int id;
    private String name;
    private String path;

    public Items(String name, String path, int id) {
        this.name = name;
        this.path = path;
        this.id = id;
    }
    public int geId() {
        return id;
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
