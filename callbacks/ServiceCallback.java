package io.pixan.systramer.callbacks;

import io.pixan.systramer.responses.ServiceResponse;
import okhttp3.ResponseBody;

public interface ServiceCallback {
    void baseResponse(ServiceResponse serviceResponse);

    void baseError();
}


