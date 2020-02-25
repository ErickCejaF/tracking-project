package io.pixan.systramer.callbacks;

import io.pixan.systramer.data.ProfileData;
import io.pixan.systramer.responses.ServiceResponse;

public interface ProfileCallback {
    void baseResponse(ProfileData data);

    void baseError();
}


