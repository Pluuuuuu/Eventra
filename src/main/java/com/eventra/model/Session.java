package com.eventra.model;

public class Session {
    private String name;
    private String date;
    private String day;
    private String time;
    private String location;
    private String presenter;
    private String status;
    
    public Session() {}
    
    public Session(String name, String date, String day, String time, String location, String presenter, String status) {
        this.name = name;
        this.date = date;
        this.day = day;
        this.time = time;
        this.location = location;
        this.presenter = presenter;
        this.status = status;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getPresenter() { return presenter; }
    public void setPresenter(String presenter) { this.presenter = presenter; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}