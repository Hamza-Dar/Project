package com.example.project;

public class Event_info {
    private double latitude, longitude;
    private String event_name, Event_desc, image_url, UID;

    public Event_info(){
    }

    public Event_info(double latitude, double longitude, String event_name, String event_desc, String image_url, String UID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.event_name = event_name;
        Event_desc = event_desc;
        this.image_url = image_url;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_desc() {
        return Event_desc;
    }

    public void setEvent_desc(String event_desc) {
        Event_desc = event_desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
