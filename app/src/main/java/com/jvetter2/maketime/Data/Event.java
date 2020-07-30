package com.jvetter2.maketime.Data;

public class Event {
    private String eventName;
    private String duration;
    private String time;
    private String date;
    private Boolean completed;
    private String id;

    public Event(String eventName, String duration, String time, String date, boolean completed, String id) {
        this.eventName = eventName;
        this.duration = duration;
        this.time = time;
        this.date = date;
        this.completed = completed;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
