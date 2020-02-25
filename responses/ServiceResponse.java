package io.pixan.systramer.responses;

import io.pixan.systramer.data.ServiceData;

public class ServiceResponse {
    private ServiceData data;

    public ServiceData getData() {
        return data;
    }

    public void setData(ServiceData data) {
        this.data = data;
    }
}
