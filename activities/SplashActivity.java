package io.pixan.systramer.activities;

import android.content.Intent;
import android.os.Bundle;


import static io.pixan.systramer.utils.SharedPreferences.getSharedRouteId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.Utils.getSavedRouteData;
import static io.pixan.systramer.utils.Utils.getSavedServiceModel;
import static io.pixan.systramer.utils.Utils.getSavedUser;

public class SplashActivity extends BaseActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSavedUser() != null) {
            if (getSharedTripStatus(SplashActivity.this)) {
                intent = new Intent(SplashActivity.this, TripActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
