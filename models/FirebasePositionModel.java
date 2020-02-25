package io.pixan.systramer.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class FirebasePositionModel {

    private Date createdAt;
    private GeoPoint location;
    private String velocity;


    public FirebasePositionModel(Date createdAt, GeoPoint location, String velocity) {
        this.createdAt = createdAt;
        this.location = location;
        this.velocity = velocity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getVelocity() {
        return velocity;
    }

    public void setVelocity(String velocity) {
        this.velocity = velocity;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
