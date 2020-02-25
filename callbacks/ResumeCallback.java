package io.pixan.systramer.callbacks;

import io.pixan.systramer.data.ProfileData;
import io.pixan.systramer.responses.ResponseResume;

public interface ResumeCallback {
    void baseResponse(ResponseResume data);

    void baseError();
}


