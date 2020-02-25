package io.pixan.systramer.responses;

import io.pixan.systramer.data.ProfileData;

public class ProfileResponse {
    private ProfileData data;

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }
}
