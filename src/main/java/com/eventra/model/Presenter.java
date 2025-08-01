package com.eventra.model;

import java.time.LocalDateTime;

public class Presenter {
    private int presenterId;
    private String name;
    private String title;
    private String company;
    private String bio;
    private String imageUrl;
    private String email;
    private String linkedinUrl;
    private String twitterUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Presenter() {}

    public Presenter(String name, String title, String company, String bio) {
        this.name = name;
        this.title = title;
        this.company = company;
        this.bio = bio;
    }

    // Getters and Setters
    public int getPresenterId() { return presenterId; }
    public void setPresenterId(int presenterId) { this.presenterId = presenterId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper method to get full title
    public String getFullTitle() {
        if (title != null && company != null) {
            return title + ", " + company;
        } else if (title != null) {
            return title;
        } else if (company != null) {
            return company;
        }
        return "";
    }

    @Override
    public String toString() {
        return "Presenter{" +
                "presenterId=" + presenterId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
} 