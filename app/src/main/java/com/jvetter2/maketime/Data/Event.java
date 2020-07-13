package com.jvetter2.maketime.Data;

import java.util.Date;

public class Event {
    private String eventName;
    private String duration;
    private String time;
    private String date;
    private Boolean status;

    public Event(String eventName, String duration, String time, String date) {
        this.eventName = eventName;
        this.duration = duration;
        this.time = time;
        this.date = date;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
