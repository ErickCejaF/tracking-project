package io.pixan.systramer.models;

import io.realm.RealmObject;

public class ElementModel extends RealmObject {
    private GoogleItemModel distance;
    private GoogleItemModel duration;

    public GoogleItemModel getDistance() {
        return distance;
    }

    public void setDistance(GoogleItemModel distance) {
        this.distance = distance;
    }

    public GoogleItemModel getDuration() {
        return duration;
    }

    public void setDuration(GoogleItemModel duration) {
        this.duration = duration;
    }
}
