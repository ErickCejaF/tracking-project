package io.pixan.systramer.responses;

import io.pixan.systramer.models.InitTripModel;

public class InitTripResponse {

    private InitTripModel data;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InitTripModel getData() {
        return data;
    }

    public void setData(InitTripModel data) {
        this.data = data;
    }
}
