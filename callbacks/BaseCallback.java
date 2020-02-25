package io.pixan.systramer.callbacks;

import okhttp3.ResponseBody;

public interface BaseCallback {
    void baseResponse(ResponseBody responseBody);

    void baseError();
}
