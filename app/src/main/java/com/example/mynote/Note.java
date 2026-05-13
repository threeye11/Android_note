package com.example.mynote;

public class Note {
    private int id;
    private String title;
    private String content;
    private String createdTime;
    private String updatedTime;

    public Note() {}

    public Note(String title, String content, String createdTime, String updatedTime) {
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}