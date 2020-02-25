package io.pixan.systramer.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.pixan.systramer.R;
import io.pixan.systramer.data.RouteData;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.dialog.LoaderDialog;
import io.pixan.systramer.fragments.ChatFragment;
import io.pixan.systramer.fragments.EvidenceFragment;
import io.pixan.systramer.fragments.ProfileFragment;
import io.pixan.systramer.fragments.ScheduleFragment;
import io.pixan.systramer.fragments.TripFragment;
import io.pixan.systramer.models.ServiceModel;
import io.pixan.systramer.models.StartTripModel;
import io.pixan.systramer.services.MyFirebaseMessagingService;
import io.pixan.systramer.services.TripService;

import static io.pixan.systramer.utils.Globals.loaderUpdatingTrip;
import static io.pixan.systramer.utils.SharedPreferences.getSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.getSharedRouteId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedShowedDelayedAlert;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.SharedPreferences.getSharedUpdatingTrip;
import static io.pixan.systramer.utils.SharedPreferences.setSharedShowedDelayedAlert;
import static io.pixan.systramer.utils.SharedPreferences.setSharedUpdatingTrip;
import static io.pixan.systramer.utils.Utils.getSavedRouteData;
import static io.pixan.systramer.utils.Utils.getSavedServiceModel;
import static io.pixan.systramer.utils.Utils.isAppOnForeground;
import static io.pixan.systramer.utils.Utils.isMyServiceRunning;
import static io.pixan.systramer.utils.Utils.saveStartTripModel;

public class TripActivity extends BaseActivity implements ServiceConnection, TripService.CallBack {

    private AHBottomNavigation bottomNavigation;

    private ChatFragment chatFragment;
    private ScheduleFragment scheduleFragment;
    private ProfileFragment profileFragment;
    private EvidenceFragment evidenceFragment;
    private TripFragment tripFragment;
    private String privateToken;

    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private FrameLayout flMainContainer;
    private RouteData routeInfo;
    private ServiceModel serviceInfo;
    private TripService tripService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(myReceiver, new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER));

        DataBindingUtil.setContentView(this, R.layout.activity_trip);

        if (!getSharedTripStatus(this)) {
            routeInfo = getSavedRouteData(getIntent().getIntExtra("route_info_id", -1));
            serviceInfo = getSavedServiceModel(getIntent().getIntExtra("service_info_id", -1));
        } else {
            routeInfo = getSavedRouteData(getSharedRouteId(this));
            serviceInfo = getSavedServiceModel(getSharedServiceId(this));
        }

        if (getSharedUpdatingTrip(TripActivity.this)) {
            if (loaderUpdatingTrip != null && !loaderUpdatingTrip.isShowing()) {
                loaderUpdatingTrip.show();
            }
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);
        flMainContainer = findViewById(R.id.fl_main_container);

        bottomNavigationItems.add(new AHBottomNavigationItem("Rutas", R.drawable.icn_route, R.color.colorMainBackground));
        bottomNavigationItems.add(new AHBottomNavigationItem("Agenda", R.drawable.icn_agenda, R.color.colorMainBackground));
        bottomNavigationItems.add(new AHBottomNavigationItem("Evidences", R.drawable.icn_evidences, R.color.colorMainBackground));
        bottomNavigationItems.add(new AHBottomNavigationItem("Chat", R.drawable.icn_chat, R.color.colorMainBackground));
        bottomNavigationItems.add(new AHBottomNavigationItem("Perfil", R.drawable.icn_profile_lightgray, R.color.colorMainBackground));

        bottomNavigation.addItems(bottomNavigationItems);

        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorMainBackground));

        chatFragment = new ChatFragment("Chat " + routeInfo.getName(), serviceInfo.getId());
        scheduleFragment = new ScheduleFragment(routeInfo, serviceInfo, "Agenda " + routeInfo.getName());
        evidenceFragment = new EvidenceFragment("Evidencias " + routeInfo.getName(), serviceInfo);

        if (!getSharedTripStatus(this)) {
            bottomNavigation.hideBottomNavigation();
            tripFragment = new TripFragment(routeInfo, serviceInfo, false);
            profileFragment = new ProfileFragment(false);
        } else {
            tripFragment = new TripFragment(routeInfo, serviceInfo, true);
            profileFragment = new ProfileFragment(true);
            startTripService();
        }

        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main_container, tripFragment, "trip")
                .commitAllowingStateLoss();

        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_main_container, tripFragment, "trip")
                            .commit();
                    break;

                case 1:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_main_container, scheduleFragment, "schedule")
                            .commit();

                    break;

                case 2:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_main_container, evidenceFragment, "evidence")
                            .commit();

                    break;

                case 3:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_main_container, chatFragment, "chat")
                            .commit();
                    break;

                case 4:

                    ProfileFragment savedProfileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("profile");

                    if (savedProfileFragment != null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_main_container, savedProfileFragment, "profile")
                                .commit();
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_main_container, profileFragment, "profile")
                                .commit();
                    }

                    break;

                default:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_main_container, tripFragment, "trip")
                            .commit();

                    break;
            }
            return true;
        });

    }

    public void showCurrentTrip() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main_container, tripFragment, "trip")
                .commitAllowingStateLoss();
    }

    public void startTripService() {
        if (!isMyServiceRunning(TripService.class, TripActivity.this)) {

            Intent intent = new Intent(this, TripService.getInstance().getClass());
            intent.putExtra("trip_info", (ServiceModel) getIntent().getParcelableExtra("trip_info"));
            intent.putExtra("route_info", (RouteData) getIntent().getParcelableExtra("route_info"));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSharedTripStatus(this)) {
            bindTripService();
        }
    }

    public void bindTripService() {
        Intent intentData = new Intent(this, TripService.class);
        bindService(intentData, this, BIND_AUTO_CREATE);
    }

    public void showTabs() {
        bottomNavigation.restoreBottomNavigation(true);
    }

    public ProfileFragment getProfileFragment() {
        return profileFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 201:
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    File file = ImagePicker.Companion.getFile(data);
                    evidenceFragment.startAddEvidence(data);

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public RouteData getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(RouteData routeInfo) {
        this.routeInfo = routeInfo;
    }

    public ServiceModel getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(ServiceModel serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        tripService = ((TripService.TripBinder) service).getService();
        tripService.setCallBack(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onMessageReceived(boolean firstUpdate, int i) {
        if (chatFragment != null) {
            chatFragment.updateChat(firstUpdate, i);
        }
    }

    @Override
    public void setMap(Location location) {
        if (tripFragment != null) {
            tripFragment.setMainMapInfo(location);
        }
    }

    @Override
    public void updateDistance() {
        if (tripFragment != null) {
            tripFragment.updateDistance();
        }
    }

    @Override
    public void updateRoute(RouteData newRouteData) {
        if (tripFragment != null) {
            tripFragment.updateRoute(newRouteData);
        }
    }

    @Override
    public void updateSchedulePositions(ArrayList<Float> distances) {
        if (scheduleFragment != null) {
            scheduleFragment.updateDistances(distances);
        }
    }

    @Override
    public void updateScheduleAdapter() {
        if (scheduleFragment != null) {
            scheduleFragment.updateSchedulePositions();
        }
    }

    @Override
    public void showCancelTrip() {
        if (tripFragment != null) {
            tripFragment.showCancelTrip();
        }
    }

    @Override
    public void showFinishTrip() {
        if (tripFragment != null) {
            tripFragment.showFinishTrip();
        }
    }

    @Override
    public void showStopTrip() {
        if (tripFragment != null) {
            tripFragment.showStopTrip();
        }
    }

    @Override
    public void showResumeTrip() {
        if (tripFragment != null) {
            tripFragment.showResumeTrip();
        }
    }

    private void updateTripInfo(Intent intent) {
        switch (intent.getStringExtra("type")) {
            case "the_end":
                if (isAppOnForeground(TripActivity.this, getPackageName())) {
                    BaseDialog baseDialog = new BaseDialog(TripActivity.this);
                    baseDialog.setCancelable(false);
                    baseDialog.setCanceledOnTouchOutside(false);
                    baseDialog.setOnAcceptClicListener(v1 -> {
                        baseDialog.dismiss();
                    });
                    baseDialog
                            .setIvDialog(getDrawable(R.drawable.icn_alerts))
                            .setTvButton("ACEPTAR")
                            .setTvMainInfo("Su viaje ha terminado")
                            .show();
                }
                break;

            case "route_changed":
                loaderUpdatingTrip = new LoaderDialog(TripActivity.this);

                if (isAppOnForeground(TripActivity.this, getPackageName())) {
                    BaseDialog baseDialog = new BaseDialog(TripActivity.this);
                    baseDialog.setCancelable(false);
                    baseDialog.setCanceledOnTouchOutside(false);
                    baseDialog.setOnAcceptClicListener(v1 -> {
                        baseDialog.dismiss();
                        setSharedUpdatingTrip(TripActivity.this, true);
                    });
                    baseDialog
                            .setIvDialog(getDrawable(R.drawable.icn_alerts))
                            .setTvButton("ACEPTAR")
                            .setTvMainInfo("La ruta de su viaje ha cambiado")
                            .show();
                } else {
                    setSharedUpdatingTrip(TripActivity.this, true);
                }

                loaderUpdatingTrip.setCanceledOnTouchOutside(false);
                loaderUpdatingTrip.setCancelable(false);
                loaderUpdatingTrip.show();

                break;
            case "estimations":
                if (tripFragment != null)
                    tripFragment.setInfoText();
                break;

            case "delayed":
                if (!getSharedShowedDelayedAlert(TripActivity.this) && !getSharedIsStopped(getApplicationContext())) {
                    setSharedShowedDelayedAlert(TripActivity.this, true);
                    if (isAppOnForeground(TripActivity.this, getPackageName())) {
                        BaseDialog baseDialog = new BaseDialog(TripActivity.this);
                        baseDialog.setCancelable(false);
                        baseDialog.setCanceledOnTouchOutside(false);
                        baseDialog.setOnAcceptClicListener(v1 -> {
                            baseDialog.dismiss();
                        });
                        baseDialog
                                .setIvDialog(getDrawable(R.drawable.icn_alerts))
                                .setTvButton("ACEPTAR")
                                .setTvMainInfo("Se encuentra en ruta retrasado")
                                .show();
                    }
                }

                break;

            case "push_init_service":
                saveStartTripModel(
                        new StartTripModel(serviceInfo.getId(),
                                intent.getStringExtra("service_token")
                        ));
                break;
        }

    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTripInfo(intent);
        }
    };


    @Override
    public void onBackPressed() {
        if (!getSharedTripStatus(this)) {
            finish();
        } else {
            if (bottomNavigation != null) {
                bottomNavigation.setCurrentItem(0);
            }

            if (tripFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_main_container, tripFragment, "trip")
                        .commitAllowingStateLoss();
            }
        }
    }
}

