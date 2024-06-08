package com.example.alarmapp;

public class TimeEntry {
    private String time;
    private boolean isActive;

    public TimeEntry(String time, boolean isActive) {
        this.time = time;
        this.isActive = isActive;
    }

    public String getTime() {
        return time;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
