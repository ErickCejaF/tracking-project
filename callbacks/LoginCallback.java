package io.pixan.systramer.callbacks;

import io.pixan.systramer.responses.LoginResponse;

public interface LoginCallback {
    void baseCallback(LoginResponse loginResponse);
}
