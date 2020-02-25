package io.pixan.systramer.callbacks;

import okhttp3.ResponseBody;

public interface FinishedUploadingPictureCallback {
    void finishedUploading();
    void failedUploading();
}
