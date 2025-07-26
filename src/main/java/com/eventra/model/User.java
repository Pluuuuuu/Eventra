package com.eventra.model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String profilePicUrl;
    private int roleTypeId;
    private int statusTypeId;
    private int periodCanLoginInMinutes;
    private LocalDateTime lastFailedLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public User() {}

    public User(String username, String firstName, String lastName, String email, String passwordHash) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleTypeId = 2; // Default role
        this.statusTypeId = 1; // Default status
        this.periodCanLoginInMinutes = 0;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public int getRoleTypeId() { return roleTypeId; }
    public void setRoleTypeId(int roleTypeId) { this.roleTypeId = roleTypeId; }

    public int getStatusTypeId() { return statusTypeId; }
    public void setStatusTypeId(int statusTypeId) { this.statusTypeId = statusTypeId; }

    public int getPeriodCanLoginInMinutes() { return periodCanLoginInMinutes; }
    public void setPeriodCanLoginInMinutes(int periodCanLoginInMinutes) { this.periodCanLoginInMinutes = periodCanLoginInMinutes; }

    public LocalDateTime getLastFailedLoginAt() { return lastFailedLoginAt; }
    public void setLastFailedLoginAt(LocalDateTime lastFailedLoginAt) { this.lastFailedLoginAt = lastFailedLoginAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper method to get full name
    public String getFullName() {
        if (middleName != null && !middleName.trim().isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }
} 