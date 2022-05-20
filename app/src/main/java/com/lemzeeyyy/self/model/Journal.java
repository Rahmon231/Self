package com.lemzeeyyy.self.model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String description;
    private String imageUrl;
    private Timestamp timeAdded;
    private String userName;
    private String userId;

    public Journal() {
    }

    public Journal(String title, String description, String imageUrl, Timestamp timeAdded, String userName, String userId) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.timeAdded = timeAdded;
        this.userName = userName;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUri(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
