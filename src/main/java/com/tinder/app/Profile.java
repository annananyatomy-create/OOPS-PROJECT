package com.tinder.app;

public class Profile {
    private Long id;
    private String name;
    private int age;
    private String bio;
    private String photo;

    public Profile() {
    }

    public Profile(Long id, String name, int age, String bio, String photo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.bio = bio;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
