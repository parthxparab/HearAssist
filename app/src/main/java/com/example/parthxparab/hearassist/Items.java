package com.example.parthxparab.hearassist;

public class Items {

    private int id;
    private String name;
    private String path;
    private String user;
    private String age;
    private String report;

    public Items(String name, String path, int id, String user, String age, String report) {
        this.name = name;
        this.path = path;
        this.id = id;
        this.user = user;
        this.age = age;
        this.report = report;

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

    public String getReport() {
        return report;
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
