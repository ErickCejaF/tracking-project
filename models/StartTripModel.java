package io.pixan.systramer.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StartTripModel extends RealmObject {

    @PrimaryKey
    private int serviceId;
    private String pushToken;

    public StartTripModel(int serviceId, String pushToken) {
        this.serviceId = serviceId;
        this.pushToken = pushToken;
    }

    public StartTripModel() {

    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
