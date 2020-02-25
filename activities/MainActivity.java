package io.pixan.systramer.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.model.LatLng;
import com.johnnylambada.location.LocationObserver;
import com.johnnylambada.location.LocationProvider;
import com.thanosfisherman.mayi.MayI;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import io.pixan.systramer.R;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.dialog.LoaderDialog;
import io.pixan.systramer.fragments.HomeFragment;
import io.pixan.systramer.fragments.ProfileFragment;
import io.pixan.systramer.services.MyFirebaseMessagingService;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static io.pixan.systramer.utils.Globals.loaderUpdatingTrip;
import static io.pixan.systramer.utils.SharedPreferences.getSharedShowedDelayedAlert;
import static io.pixan.systramer.utils.SharedPreferences.setSharedShowedDelayedAlert;
import static io.pixan.systramer.utils.SharedPreferences.setSharedUpdatingTrip;
import static io.pixan.systramer.utils.Utils.isAppOnForeground;

public class MainActivity extends BaseActivity {

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout llMainContainer;
    private LocationProvider locationProvider;
    private Location userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment(false);
        profileFragment = new ProfileFragment(false);

        llMainContainer = findViewById(R.id.ll_main_container);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MayI.Companion.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .onResult((Function1<PermissionBean, Unit>) permissionBean -> null)
                .onRationale((permissionBean, permissionToken) -> null).check();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_frame_layout, homeFragment, "home")
                .commitAllowingStateLoss();


        locationProvider = new LocationProvider.Builder(MainActivity.this)
                .accuracy(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .locationObserver(location -> userLocation = location)
                .onPermissionDeniedFirstTime(() -> {
                })
                .onPermissionDeniedAgain(() -> {
                })
                .onPermissionDeniedForever(() -> {
                })
                .build();

        locationProvider.startTrackingLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationProvider.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        try {
            getSupportFragmentManager()
                    .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        switch (item.getItemId()) {
            case R.id.navigation_routes:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_frame_layout, homeFragment, "home")
                        .commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_frame_layout, profileFragment, "profile")
                        .commit();
                return true;
        }
        return false;
    };





}