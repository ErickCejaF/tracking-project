package io.pixan.systramer.callbacks;

import io.pixan.systramer.responses.MediaResponse;

public interface ImageCallback {
    void baseResponse(MediaResponse responseBody);
    void baseError();
}
