package io.pixan.systramer.models;

import io.realm.RealmObject;

public class ScheduleModel extends RealmObject {
    private String title;
    private String date;
    private String time;
    private String distance;
    private String speed;

    public ScheduleModel() {
    }

    public ScheduleModel(String title, String date, String speed) {
        this.title = title;
        this.date = date;
        this.speed = speed;
    }

    public ScheduleModel(String title, String date, String distance, String speed) {
        this.title = title;
        this.date = date;
        this.distance = distance;
        this.speed = speed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
