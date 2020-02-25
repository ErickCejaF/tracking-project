package io.pixan.systramer.callbacks;

import io.pixan.systramer.data.RouteData;
import io.pixan.systramer.models.InitTripModel;

public interface InitTripCallback {
    void baseResponse(InitTripModel data);
    void baseError();
}
