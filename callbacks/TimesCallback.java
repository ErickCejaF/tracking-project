package io.pixan.systramer.callbacks;

import io.pixan.systramer.data.ProfileData;
import io.pixan.systramer.responses.TimesResponse;

public interface TimesCallback {

    void baseResponse(TimesResponse data);
    void baseError();


}
