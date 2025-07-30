package com.eventra.model;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Attendee {
    private int attendeeId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String organization;
    private String phone;
    private String location;
    private String gender;
    private LocalDate dateOfBirth;
    private String profilePicUrl;
    private String type;
    private String passwordHash;
    private int statusTypeId;
    private int periodCanLoginInMinutes;
    private LocalDateTime lastFailedLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Attendee() {}

    public Attendee(String firstName, String lastName, String email, String organization, 
                   String passwordHash, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.organization = organization;
        this.passwordHash = passwordHash;
        this.type = type != null ? type : "Regular";
        this.statusTypeId = 1; // Default active status
        this.periodCanLoginInMinutes = 0;
    }

    // Getters and Setters
    public int getAttendeeId() { return attendeeId; }
    public void setAttendeeId(int attendeeId) { this.attendeeId = attendeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getStatusTypeId() { return statusTypeId; }
    public void setStatusTypeId(int statusTypeId) { this.statusTypeId = statusTypeId; }

    public int getPeriodCanLoginInMinutes() { return periodCanLoginInMinutes; }
    public void setPeriodCanLoginInMinutes(int periodCanLoginInMinutes) { 
        this.periodCanLoginInMinutes = periodCanLoginInMinutes; 
    }

    public LocalDateTime getLastFailedLoginAt() { return lastFailedLoginAt; }
    public void setLastFailedLoginAt(LocalDateTime lastFailedLoginAt) { 
        this.lastFailedLoginAt = lastFailedLoginAt; 
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Attendee{" +
                "attendeeId=" + attendeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", organization='" + organization + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
} 