package io.pixan.systramer.callbacks;

import io.pixan.systramer.data.ProfileData;
import io.pixan.systramer.data.RouteData;

public interface RouteCallback {
    void baseResponse(RouteData data);

    void baseError();
}
