package io.pixan.systramer.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.PolyUtil;
import com.psoffritti.slidingpanel.PanelState;
import com.psoffritti.slidingpanel.SlidingPanel;
import com.thanosfisherman.mayi.MayI;
import com.thanosfisherman.mayi.PermissionBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.TripActivity;
import io.pixan.systramer.adapters.TripAdapter;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.callbacks.InitTripCallback;
import io.pixan.systramer.callbacks.ResumeCallback;
import io.pixan.systramer.data.RouteData;
import io.pixan.systramer.dialog.AlertDialog;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.models.InfoModel;
import io.pixan.systramer.models.InitTripModel;
import io.pixan.systramer.models.ScheduleListModel;
import io.pixan.systramer.models.ServiceModel;
import io.pixan.systramer.responses.ResponseResume;
import io.pixan.systramer.utils.CustomMapView;
import io.realm.RealmList;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.Constants.ALERT_CANCEL_REQUEST_ID;
import static io.pixan.systramer.utils.Constants.ALERT_FINISH_REQUEST_ID;
import static io.pixan.systramer.utils.Constants.STOPS;
import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.carSpeed;
import static io.pixan.systramer.utils.Globals.scheduleModels;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.SharedPreferences.getSharedDestinationId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedDistance;
import static io.pixan.systramer.utils.SharedPreferences.getSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedStopNumber;
import static io.pixan.systramer.utils.SharedPreferences.getSharedStopTime;
import static io.pixan.systramer.utils.SharedPreferences.getSharedDuration;
import static io.pixan.systramer.utils.SharedPreferences.getSharedToken;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTotalDistance;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.SharedPreferences.setSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.setSharedRouteId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTripStatus;
import static io.pixan.systramer.utils.Utils.bitmapDescriptorFromVector;
import static io.pixan.systramer.utils.Utils.callEmergency;
import static io.pixan.systramer.utils.Utils.formatDate;
import static io.pixan.systramer.utils.Utils.getFormatedHour;
import static io.pixan.systramer.utils.Utils.getStartTripModel;
import static io.pixan.systramer.utils.Utils.isLocationEnabled;
import static io.pixan.systramer.utils.Utils.saveInitTripModel;
import static io.pixan.systramer.utils.Utils.saveRouteData;
import static io.pixan.systramer.utils.Utils.saveScheduleList;
import static io.pixan.systramer.utils.Utils.saveServiceData;
import static io.pixan.systramer.utils.WebServices.wsPostAlert;
import static io.pixan.systramer.utils.WebServices.wsResumeTrip;
import static io.pixan.systramer.utils.WebServices.wsStartTripWhitCode;
import static io.pixan.systramer.utils.WebServices.wsStartTripWithoutCode;
import static io.pixan.systramer.utils.WebServices.wsStopRoute;

public class TripFragment extends Fragment {

    private SwipeButton sbStartTrip;
    private SwipeButton sbCancelTrip;
    private GoogleMap gmSmallMap;
    private GoogleMap gmMainMap;
    private RecyclerView rvTrips;
    private TripAdapter tripAdapter;
    private RealmList<InfoModel> infoModels;
    private SlidingPanel slidingPanel;
    private LinearLayout slidingView;
    private CustomMapView mvTrip;
    private CustomMapView mvTraveling;
    private NestedScrollView nsvView;
    private TextView tvDistanceKilometers;
    private LinearLayout llCornered;
    private View itemStart;
    private TextView tvTime;
    private View itemEnd;
    private ImageView ivCenterMap;
    private TextView tvStopHour;
    private TextView tvTimeToArrive;
    private TextView tvMainStart;
    private LinearLayout llStartTrip;
    private String showingFinish;
    private RelativeLayout rlContainer;
    private PolylineOptions mPathOptions;
    private boolean isCentered;
    private ImageView ivArrowDown;
    private TextView tvMainEnd;
    private Space sStartTrip;
    private LinearLayout llDragView;
    private TextView tvDate;
    private TextView tvHour;
    private Marker mUserMainLocation;
    private Marker mUserSmallLocation;
    private LinearLayout llTripContainer;
    private LinearLayout llTimesContainer;
    private LinearLayout llStopContainer;
    private View vCollapsedArrow;
    private ImageView ivSpeed;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ImageView ivCenterItem;
    private TextView tvSpeed;
    private RouteData routeInfo;
    private Float lastBearing;
    private TextView tvContinueTime;
    private ServiceModel serviceInfo;
    private Boolean addPadding;
    private Location lastSavedLocation;
    private RelativeLayout lItemEmergency;
    private TextView tvStopDescription;
    private LinearLayout llEmergencyButton;
    private LinearLayout llItemSos;
    private RelativeLayout lItemSos;
    private ArrayList<MarkerOptions> sMarkerOptions;
    private ArrayList<MarkerOptions> bMarkerOptions;


    public TripFragment() {
    }

    @SuppressLint("ValidFragment")
    public TripFragment(RouteData routeInfo, ServiceModel serviceInfo, Boolean addPadding) {
        this.routeInfo = routeInfo;
        this.serviceInfo = serviceInfo;
        this.addPadding = addPadding;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = DataBindingUtil.inflate(inflater, R.layout.fragment_trip, container, false).getRoot();
        setRetainInstance(true);

        sbStartTrip = view.findViewById(R.id.sb_start_trip);
        mvTrip = view.findViewById(R.id.mv_route);
        rvTrips = view.findViewById(R.id.rv_routes);
        slidingPanel = view.findViewById(R.id.sliding_panel);
        slidingView = view.findViewById(R.id.sliding_view);
        llTripContainer = view.findViewById(R.id.ll_trip_container);
        llTimesContainer = view.findViewById(R.id.ll_times_container);
        llStopContainer = view.findViewById(R.id.ll_stop_container);
        ivCenterItem = view.findViewById(R.id.iv_center_user);
        sStartTrip = view.findViewById(R.id.s_start_trip);
        tvStopHour = view.findViewById(R.id.tv_stop_hour);
        mvTraveling = view.findViewById(R.id.mv_travelling);
        llDragView = view.findViewById(R.id.drag_view);
        sbCancelTrip = view.findViewById(R.id.sb_cancel_trip);
        nsvView = view.findViewById(R.id.nsv_view);
        tvDistanceKilometers = view.findViewById(R.id.tv_distance_kilometers);
        ivArrowDown = view.findViewById(R.id.iv_arrow_down);
        vCollapsedArrow = view.findViewById(R.id.v_collapsed_arrow);
        rlContainer = view.findViewById(R.id.rl_container);
        tvSpeed = view.findViewById(R.id.tv_speed);
        itemStart = view.findViewById(R.id.item_start);
        tvStopDescription = view.findViewById(R.id.tv_stop_description);
        itemEnd = view.findViewById(R.id.item_end);
        tvDate = view.findViewById(R.id.tv_date);
        tvHour = view.findViewById(R.id.tv_hour);
        tvTimeToArrive = view.findViewById(R.id.tv_time_to_arrive);
        ivSpeed = view.findViewById(R.id.iv_speed);
        tvMainStart = itemStart.findViewById(R.id.tv_main_text);
        tvTime = view.findViewById(R.id.tv_time);
        tvMainEnd = itemEnd.findViewById(R.id.tv_main_text);
        llStartTrip = view.findViewById(R.id.ll_start_trip);
        tvContinueTime = view.findViewById(R.id.tv_continue_time);
        ivCenterMap = view.findViewById(R.id.iv_centar_map);
        llCornered = view.findViewById(R.id.ll_cornered);

        showingFinish = "";

        infoModels = new RealmList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (routeInfo != null) {
            try {
                tvMainStart.setText(routeInfo.getLocations().get(0).getAddress());
                tvMainEnd.setText(routeInfo.getLocations().get(routeInfo.getLocations().size() - 1).getAddress());
            } catch (Exception e) {
                tvMainStart.setText(routeInfo.getName());
            }
        }

        if (sMarkerOptions == null) {
            sMarkerOptions = new ArrayList<>();
        }

        if (bMarkerOptions == null) {
            bMarkerOptions = new ArrayList<>();
        }


        if (addPadding != null) {
            if (addPadding || getSharedTripStatus(getActivity())) {
                rlContainer.setPadding(0, 0, 0, 147);
            }
        }


        lItemEmergency = view.findViewById(R.id.item_emergency);
        lItemSos = view.findViewById(R.id.item_sos);
        llItemSos = lItemSos.findViewById(R.id.ll_button);
        llEmergencyButton = lItemEmergency.findViewById(R.id.ll_button);

        llEmergencyButton.setOnClickListener(v -> callEmergency(getActivity()));
        llItemSos.setOnClickListener(v -> {
            if (carLocation != null && getSharedTripStatus(getContext())) {
                float speed = 0;
                if (serviceLocation != null)
                    speed = serviceLocation.getSpeed();

                wsPostAlert(getActivity(),
                        getSharedServiceId(getActivity()),
                        11,
                        carLocation.getLatitude(),
                        carLocation.getLongitude(),
                        (int) speed,
                        true,
                        new BaseCallback() {
                            @Override
                            public void baseResponse(ResponseBody responseBody) {
                                BaseDialog baseDialog = new BaseDialog(getContext());
                                baseDialog.setCancelable(false);
                                baseDialog.setCanceledOnTouchOutside(false);
                                baseDialog.setOnAcceptClicListener(v1 -> {
                                    baseDialog.dismiss();
                                });
                                baseDialog
                                        .setIvDialog(getResources().getDrawable(R.drawable.icn_red_alert))
                                        .setTvButton("ACEPTAR")
                                        .setTvMainInfo("Se mandó un mensaje a la central de alertas")
                                        .show();
                            }

                            @Override
                            public void baseError() {
                                Toast.makeText(getActivity(), "Ocurrio un error al mandar tu alerta, intentalo nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        ivCenterItem.setOnClickListener(v -> {
            ivCenterItem.setVisibility(View.GONE);
            isCentered = true;
            if (carLocation != null) {
                gmMainMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(new LatLng(carLocation.getLatitude(), carLocation.getLongitude()))
                                .zoom(17)
                                .build()));
            }
        });

        ivCenterMap.setOnClickListener(v -> {
            new AlertDialog(getContext(), (getActivity()), serviceInfo.getId()).show();
//            isCentered = false;
//            if (carLocation != null) {
//                centerMap(true);
//            }
        });

        Glide.with(this)
                .load(R.drawable.style_main_black)
                .apply(new RequestOptions()
                        .circleCrop())
                .into(ivSpeed);


        sbStartTrip.setOnActiveListener(() -> {
            if (getActivity() != null) {
                if (isLocationEnabled(getActivity())) {
                    if (!getSharedTripStatus(getActivity()) && carLocation != null) {
                        wsStartTripWithoutCode(
                                getActivity(),
                                serviceInfo.getId(),
                                getSharedToken(getActivity()),
                                carLocation.getLatitude(),
                                carLocation.getLongitude(),
                                true,
                                new BaseCallback() {
                                    @Override
                                    public void baseResponse(ResponseBody responseBody) {

                                        if (getActivity() != null) {
                                            BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
                                            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_code_input, null);
                                            mBottomSheetDialog.setContentView(sheetView);
                                            mBottomSheetDialog.show();

                                            TextView tvAccept = mBottomSheetDialog.findViewById(R.id.tv_accept);
                                            TextView tvCancel = mBottomSheetDialog.findViewById(R.id.tv_cancel);

                                            EditText etDigit = mBottomSheetDialog.findViewById(R.id.et_digit);

                                            tvCancel.setOnClickListener(v -> mBottomSheetDialog.dismiss());
                                            tvAccept.setOnClickListener(v -> {
                                                String code = etDigit.getText().toString();


                                                wsStartTripWhitCode(
                                                        getActivity(),
                                                        serviceInfo.getId(),
                                                        getSharedToken(getActivity()),
                                                        getStartTripModel(serviceInfo.getId()).getPushToken(),
                                                        code,
                                                        carLocation.getLatitude(),
                                                        carLocation.getLongitude(),
                                                        true,
                                                        new InitTripCallback() {
                                                            @Override
                                                            public void baseResponse(InitTripModel responseBody1) {

                                                                mBottomSheetDialog.dismiss();

                                                                setSharedTripStatus(getActivity(), true);
                                                                scheduleModels = new ScheduleListModel();
                                                                scheduleModels.setServiceId(serviceInfo.getId());
                                                                scheduleModels.setScheduleModels(new RealmList<>());
                                                                saveScheduleList(scheduleModels);

                                                                llStartTrip.setVisibility(View.GONE);
                                                                llTimesContainer.setVisibility(View.VISIBLE);
                                                                llStopContainer.setVisibility(View.GONE);
                                                                sStartTrip.setVisibility(View.GONE);
                                                                sbStartTrip.toggleState();

                                                                ((TripActivity) getActivity()).showTabs();
                                                                rlContainer.setPadding(0, 0, 0, 147);

                                                                setSharedRouteId(getActivity(), routeInfo.getId());
                                                                setSharedServiceId(getActivity(), serviceInfo.getId());

                                                                saveInitTripModel(responseBody1);
                                                                saveRouteData(routeInfo);
                                                                saveServiceData(serviceInfo);

                                                                addPadding = true;

                                                                mFusedLocationClient.removeLocationUpdates(locationCallback);

                                                                if (addPadding && ((TripActivity) getActivity()).getProfileFragment() != null) {
                                                                    ((TripActivity) getActivity()).getProfileFragment().setBottomPadding();
                                                                }

                                                                ((TripActivity) getActivity()).startTripService();
                                                                ((TripActivity) getActivity()).bindTripService();

                                                                slidingPanel.slideTo(PanelState.COLLAPSED);
                                                            }

                                                            @Override
                                                            public void baseError() {
                                                                setSharedTripStatus(getActivity(), false);
                                                                Toast.makeText(getContext(),
                                                                        "Ocurrio un error al iniciar tu servicio",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            });
                                        }
                                    }

                                    @Override
                                    public void baseError() {

                                    }
                                });
                    }
                } else {
                    BaseDialog baseDialog = new BaseDialog(getActivity());
                    baseDialog.setCancelable(false);
                    baseDialog.setCanceledOnTouchOutside(false);
                    baseDialog.setOnAcceptClicListener(v1 -> baseDialog.dismiss());
                    baseDialog.setIvDialog((getActivity()).getDrawable(R.drawable.icn_red_alert))
                            .setTvButton("ACEPTAR")
                            .setTvMainInfo("Por favor activa tu ubicación")
                            .show();
                }
            }
        });

        sbCancelTrip.setOnActiveListener(() -> {
            int alertid;

            switch (showingFinish) {

                case "RESUME_TRIP":
                    break;

                case "STOP_TRIP":
                    break;

                case "FINISH_TRIP":
                    alertid = ALERT_FINISH_REQUEST_ID;
                    wsPostAlertUser(alertid);
                    break;

                case "CANCEL_TRIP":
                    alertid = ALERT_CANCEL_REQUEST_ID;
                    wsPostAlertUser(alertid);

                    break;
            }
        });


        if (!getSharedTripStatus(getActivity())) {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            setMainMapInfo(location);
                        }
                    }
                }
            };

            llDragView.setClickable(false);
            llDragView.setFocusable(false);
            llDragView.setEnabled(false);

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        } else {
            setInfoText();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return null;
            }
        }

        // Create a PolylineOptions to draw the paths
        mPathOptions = new PolylineOptions();
        mPathOptions.color(getResources().getColor(R.color.main_light_gray));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        if (serviceInfo != null) {
            tvDate.setText(formatDate(serviceInfo.getDeparture_date(), dateFormat));
            tvHour.setText(formatDate(serviceInfo.getDeparture_date(), hourFormat));
        }

        slidingPanel.addSlideListener((slidingPanel, panelState, aFloat) -> {
            switch (panelState) {
                case SLIDING:
                    nsvView.smoothScrollTo(0, 0);
                    slidingView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    llCornered.setBackground(getResources().getDrawable(R.drawable.style_without_corners_black));
                    ivArrowDown.setVisibility(View.GONE);
                    vCollapsedArrow.setVisibility(View.VISIBLE);
                    break;
                case COLLAPSED:
                    nsvView.smoothScrollTo(0, 0);
                    slidingView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    llCornered.setBackground(getResources().getDrawable(R.drawable.drawable_drag_view_destinations));
                    ivArrowDown.setVisibility(View.GONE);
                    vCollapsedArrow.setVisibility(View.VISIBLE);
                    break;
                case EXPANDED:
                    slidingView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    llCornered.setBackground(getResources().getDrawable(R.drawable.style_without_corners_black));
                    ivArrowDown.setVisibility(View.VISIBLE);
                    vCollapsedArrow.setVisibility(View.GONE);
                    break;
            }
            return null;
        });

        if (routeInfo != null && routeInfo.getLocations() != null) {
            infoModels = routeInfo.getLocations();
        }

        // Gets the MapView from the XML layout and creates it
        mvTrip.onCreate(savedInstanceState);
        mvTraveling.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mvTrip.getMapAsync(googleMap -> {
            gmSmallMap = googleMap;

            tripAdapter = new TripAdapter(getActivity(), infoModels, gmSmallMap);
            rvTrips.setAdapter(tripAdapter);
            rvTrips.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvTrips.setNestedScrollingEnabled(false);

            gmSmallMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    MayI.Companion.withActivity(getActivity())
                            .withPermissions(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION)
                            .onResult((Function1<PermissionBean, Unit>) permissionBean -> null)
                            .onRationale((permissionBean, permissionToken) -> null).check();
                    return;
                }
            }

            if (lastSavedLocation != null) {
                if (gmSmallMap != null && sMarkerOptions != null && sMarkerOptions.size() > 0) {
                    gmSmallMap.clear();
                    for (int i = 0; i < sMarkerOptions.size(); i++) {
                        if (i == sMarkerOptions.size() - 1) {
                            mUserSmallLocation = gmSmallMap.addMarker(sMarkerOptions.get(i));
                        } else {
                            gmSmallMap.addMarker(sMarkerOptions.get(i));
                        }
                    }
                }
            }

            gmSmallMap.setMyLocationEnabled(false);

            final UiSettings uiSettings = gmSmallMap.getUiSettings();
            uiSettings.setCompassEnabled(false);
            uiSettings.setZoomControlsEnabled(false);

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(getActivity());

            centerMap(false);


        });


        mvTraveling.getMapAsync(googleMap -> {
            gmMainMap = googleMap;
            gmMainMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    MayI.Companion.withActivity(getActivity())
                            .withPermissions(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION)
                            .onResult((Function1<PermissionBean, Unit>) permissionBean -> null)
                            .onRationale((permissionBean, permissionToken) -> null).check();
                    return;
                }
            }

            if (lastSavedLocation != null) {
                if (gmMainMap != null && bMarkerOptions != null && bMarkerOptions.size() > 0) {
                    gmMainMap.clear();
                    for (int i = 0; i < bMarkerOptions.size(); i++) {
                        if (i == bMarkerOptions.size() - 1) {
                            mUserMainLocation = gmMainMap.addMarker(bMarkerOptions.get(i));
                        } else {
                            gmMainMap.addMarker(bMarkerOptions.get(i));
                        }
                    }
                }
            }

            gmMainMap.setOnCameraMoveStartedListener(i -> {
                if (i == 1) {
                    ivCenterItem.setVisibility(View.VISIBLE);
                    isCentered = false;
                }
            });

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(getActivity());


            // Enabling MyLocation Layer of Google Map
            gmMainMap.setMyLocationEnabled(false);

            if (routeInfo.getPoints() != null) {
                final LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                final List<LatLng> pathPoints = PolyUtil.decode(routeInfo.getPoints());
                gmMainMap.addPolyline(mPathOptions).setPoints(pathPoints);
                for (LatLng point : pathPoints) {
                    boundsBuilder.include(point);
                }

                animateCameraToBounds(boundsBuilder.build(), gmMainMap, mvTraveling);
            }


        });
        new CountDownTimer(200, 100) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (getActivity() != null) {
                    if (getSharedTripStatus(getActivity())) {

                        if (!getSharedIsStopped(getActivity())) {
                            //                            llTimesContainer.setVisibility(View.GONE);
                            llTimesContainer.setVisibility(View.VISIBLE);
                            llStopContainer.setVisibility(View.GONE);
                        } else {
                            llTimesContainer.setVisibility(View.GONE);
                            llStopContainer.setVisibility(View.VISIBLE);
                        }

                        llStartTrip.setVisibility(View.GONE);
                        sStartTrip.setVisibility(View.GONE);
                        sbStartTrip.toggleState();

                        setSharedTripStatus(getActivity(), true);
                        setSharedRouteId(getActivity(), routeInfo.getId());
                        setSharedServiceId(getActivity(), serviceInfo.getId());

                        slidingPanel.slideTo(PanelState.COLLAPSED);
                    } else {
                        slidingPanel.slideTo(PanelState.EXPANDED);
                    }
                }
            }
        }.start();

        return view;
    }

    private void centerMap(boolean isBig) {
        if (routeInfo.getPoints() != null) {
            final LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            final List<LatLng> pathPoints = PolyUtil.decode(routeInfo.getPoints());
            gmSmallMap.addPolyline(mPathOptions).setPoints(pathPoints);
            for (LatLng point : pathPoints) {
                boundsBuilder.include(point);
            }

            if (isBig) {
                animateCameraToBounds(boundsBuilder.build(), gmMainMap, mvTraveling);
            } else {
                animateCameraToBounds(boundsBuilder.build(), gmSmallMap, mvTrip);
            }
        }
    }

    public void setMainMapInfo(Location location) {
        lastSavedLocation = location;

        if (mUserMainLocation != null) {

            float bearing = location.getBearing();

            mUserMainLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));

            if (carSpeed > 1.5) {
                mUserMainLocation.setRotation(location.getBearing());
                lastBearing = bearing;
            } else {
                if (lastBearing != null) {
                    mUserMainLocation.setRotation(lastBearing);
                }
            }

            if (isCentered) {
                gmMainMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .bearing(location.getBearing())
                                        .zoom(17)
                                        .build()));
            }
        } else {
            if (gmMainMap != null)
                setMapInfo(location, gmMainMap, false);
        }

        if (mUserSmallLocation != null) {
            mUserSmallLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
//            mUserSmallLocation.setRotation(location.getBearing());
        } else {
            if (gmSmallMap != null)
                setMapInfo(location, gmSmallMap, true);
        }

        carLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

        if (gmMainMap != null) {
            carSpeed = location.getSpeed();
            tvSpeed.setText(String.format(Locale.getDefault(),
                    "%.0f", ((carSpeed) * 3.6) / 1));
        }
    }

    public void setInfoText() {
        if (getActivity() != null) {
            if (getSharedIsStopped(getActivity())) {
                showContinueTime();

            } else {

                if (getSharedTotalDistance(getActivity()) != 0) {
                    tvDistanceKilometers.setText(String.format(Locale.getDefault(), "%.2f",
                            (getSharedTotalDistance(getActivity()) / 1000)));
                }

                if (getSharedDuration(getActivity()) != 0) {
                    tvDistanceKilometers.setText(String.format(Locale.getDefault(), "%.2f",
                            (getSharedTotalDistance(getActivity()) / 1000)));
                    try {
                        tvTimeToArrive.setText(getFormatedHour(getSharedDuration(getActivity())));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    private void showContinueTime() {
        llTimesContainer.setVisibility(View.GONE);
        llStopContainer.setVisibility(View.VISIBLE);

        long stopHour = getSharedStopTime(getActivity());

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(stopHour);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        tvContinueTime.setText(sdf.format(rightNow.getTime()));
    }


    private void wsPostAlertUser(int alertid) {
        wsPostAlert(getActivity(),
                serviceInfo.getId(),
                alertid,
                carLocation.getLatitude(),
                carLocation.getLongitude(),
                (int) carSpeed,
                true,
                new BaseCallback() {
                    @Override
                    public void baseResponse(ResponseBody responseBody) {
                        String textMainInfo;

                        if (getActivity() != null && alertid == ALERT_CANCEL_REQUEST_ID) {
                            textMainInfo = "Se solicitó la cancelación de su viaje";
                        } else {
                            textMainInfo = "Se solicitó la finalización de su viaje";
                        }

                        BaseDialog baseDialog = new BaseDialog(getActivity());
                        baseDialog.setCancelable(false);
                        baseDialog.setCanceledOnTouchOutside(false);
                        baseDialog.setOnAcceptClicListener(v1 -> baseDialog.dismiss());
                        baseDialog.setIvDialog((getActivity()).getDrawable(R.drawable.icn_red_alert))
                                .setTvButton("ACEPTAR")
                                .setTvMainInfo(textMainInfo)
                                .show();
                    }

                    @Override
                    public void baseError() {
                        if (getActivity() != null) {
                            if (alertid == ALERT_CANCEL_REQUEST_ID) {
                                Toast.makeText(getActivity(), "Ocurrio un error al solicitar la cancelación, intentalo nuevamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Ocurrio un error al solicitar la finalización de su viaje, intentalo nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setMapInfo(Location location, GoogleMap googleMap, boolean isSmall) {
        if (getActivity() != null) {

            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            if (!isSmall) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                googleMap.animateCamera(cameraUpdate);
            }

            if (routeInfo.getPoints() != null) {
                final LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                final List<LatLng> pathPoints = PolyUtil.decode(routeInfo.getPoints());
                googleMap.addPolyline(mPathOptions).setPoints(pathPoints);
                for (LatLng point : pathPoints) {
                    boundsBuilder.include(point);
                }

                if (routeInfo.getLocations() != null) {
                    for (int i = 0; i < routeInfo.getLocations().size(); i++) {
                        BitmapDescriptor icon;

                        if (i == routeInfo.getLocations().size() - 1) {
                            icon = bitmapDescriptorFromVector(getActivity(), R.drawable.icn_destination);
                        } else {
                            icon = bitmapDescriptorFromVector(getActivity(), R.drawable.icn_circle);
                        }

                        MarkerOptions lMarkerOptions;

                        lMarkerOptions = new MarkerOptions()
                                .position(new LatLng(
                                        routeInfo.getLocations().get(i).getLocation().getCoordinates().get(1),
                                        routeInfo.getLocations().get(i).getLocation().getCoordinates().get(0)
                                ))
                                .flat(true)
                                .icon(icon);


                        lMarkerOptions.anchor(0.5f, 0.5f);
                        googleMap.addMarker(lMarkerOptions);

                        if (isSmall) {
                            sMarkerOptions.add(lMarkerOptions);
                        } else {
                            bMarkerOptions.add(lMarkerOptions);
                        }
                    }
                }
            }


            BitmapDescriptor icon = bitmapDescriptorFromVector(getActivity(), R.drawable.icn_truck);
            MarkerOptions lMarkerOptions = new MarkerOptions()
                    .position(latLng)
                    .flat(true)
                    .anchor(0.5f, 0.5f)
                    .icon(icon);

            if (isSmall) {
                mUserSmallLocation = googleMap.addMarker(lMarkerOptions);
                sMarkerOptions.add(lMarkerOptions);
            } else {
                mUserMainLocation = googleMap.addMarker(lMarkerOptions);
                bMarkerOptions.add(lMarkerOptions);
            }
        }
    }

    public void updateRoute(RouteData newRouteData) {
        if (gmMainMap != null) {
            gmMainMap.clear();
        }
        if (gmSmallMap != null) {
            gmSmallMap.clear();
        }

        routeInfo = newRouteData;

        mUserSmallLocation = null;
        mUserMainLocation = null;
    }


    private void animateCameraToBounds(final LatLngBounds bounds, GoogleMap mGoogleMap, MapView mapView) {
        try {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        } catch (IllegalStateException e) {
            // layout not yet initialized
            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(() ->
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50)));
            }
        }
    }

    public void updateDistance() {
        if (getActivity() != null) {

            int pos = 0;
            for (; pos < routeInfo.getLocations().size(); pos++) {
                if (routeInfo.getLocations().get(pos).getId() == getSharedDestinationId(getActivity())) {
                    break;
                }
            }

            if (pos <= STOPS.length) {
                if (getSharedStopNumber(getActivity()) != -1) {
                    tvStopDescription.setText(STOPS[getSharedStopNumber(getActivity())] + " parada");
                }
            }


            tvTime.setText(String.format(Locale.getDefault(), "%.2f",
                    (((float) getSharedDistance(getActivity()) / 1000))) + " Km");

            tvStopHour.setText(String.format(Locale.getDefault(), "%.2f",
                    (((float) getSharedDistance(getActivity()) / 1000))) + " Km");

//            tvTime.setText(((float) getSharedDistance(getActivity()) / 1000) + " Km");
//
//            tvStopHour.setText(((float) getSharedDistance(getActivity()) / 1000) + " Km");
        }
    }

    public void showStopTrip() {
        sbCancelTrip.setVisibility(View.INVISIBLE);
        llStopContainer.setVisibility(View.VISIBLE);
        llTimesContainer.setVisibility(View.GONE);
        setInfoText();
    }

    public void showFinishTrip() {
        sbCancelTrip.setVisibility(View.VISIBLE);
        sbCancelTrip.setText("Terminar viaje");
        showingFinish = "FINISH_TRIP";

        setInfoText();
    }

    public void showCancelTrip() {
        sbCancelTrip.setVisibility(View.VISIBLE);

        llStopContainer.setVisibility(View.GONE);
        llTimesContainer.setVisibility(View.VISIBLE);
        sbCancelTrip.setText("Cancelar viaje");
        showingFinish = "CANCEL_TRIP";

        setInfoText();
    }

    public void showResumeTrip() {
        sbCancelTrip.setVisibility(View.VISIBLE);

        llStopContainer.setVisibility(View.GONE);
        llTimesContainer.setVisibility(View.VISIBLE);
        setInfoText();
    }

    public void setRouteInfo(RouteData routeInfo) {
        this.routeInfo = routeInfo;
    }

    @Override
    public void onResume() {
        mvTrip.onResume();
        mvTraveling.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mvTrip.onPause();
        mvTraveling.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvTrip.onDestroy();
        mvTraveling.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvTrip.onLowMemory();
        mvTraveling.onLowMemory();
    }
}
