package io.pixan.systramer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.MainActivity;
import io.pixan.systramer.callbacks.RouteCallback;
import io.pixan.systramer.data.RouteData;
import io.pixan.systramer.models.EvidenceContainerModel;
import io.pixan.systramer.models.FirebasePositionModel;
import io.pixan.systramer.models.InfoModel;
import io.pixan.systramer.models.InitTripModel;
import io.pixan.systramer.models.MessageModel;
import io.pixan.systramer.models.ScheduleListModel;
import io.pixan.systramer.models.ScheduleModel;
import io.pixan.systramer.models.ServiceModel;
import io.pixan.systramer.models.StartTripModel;
import io.pixan.systramer.responses.TimesResponse;
import io.realm.Realm;
import io.realm.RealmList;

import static io.pixan.systramer.utils.Constants.SERVICE_STATUS_CANCELED;
import static io.pixan.systramer.utils.Constants.SERVICE_STATUS_FINISHED;
import static io.pixan.systramer.utils.Constants.SERVICE_STATUS_IN_TRANSIT;
import static io.pixan.systramer.utils.Constants.SERVICE_STATUS_NEW;
import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.carSpeed;
import static io.pixan.systramer.utils.Globals.loaderUpdatingTrip;
import static io.pixan.systramer.utils.Globals.messages;
import static io.pixan.systramer.utils.Globals.scheduleModels;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.SharedPreferences.getSharedDestinationId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.getSharedOriginId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedRouteId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDelayed;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDestinationId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDistance;
import static io.pixan.systramer.utils.SharedPreferences.setSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.setSharedOriginId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedRouteId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedStopNumber;
import static io.pixan.systramer.utils.SharedPreferences.setSharedStopTime;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDuration;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTotalDistance;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTotalTime;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTripStatus;
import static io.pixan.systramer.utils.SharedPreferences.setSharedUpdatingTrip;
import static io.pixan.systramer.utils.Utils.calculateSchedulePositions;
import static io.pixan.systramer.utils.Utils.getSavedRouteData;
import static io.pixan.systramer.utils.Utils.getSavedServiceModel;
import static io.pixan.systramer.utils.Utils.getSavedUser;
import static io.pixan.systramer.utils.Utils.getScheduleListModel;
import static io.pixan.systramer.utils.Utils.saveRouteData;
import static io.pixan.systramer.utils.Utils.saveScheduleList;
import static io.pixan.systramer.utils.Utils.showSmallNotification;
import static io.pixan.systramer.utils.Utils.updateFirebaseLocation;
import static io.pixan.systramer.utils.WebServices.wsGetRouteInfo;

public class TripService extends Service {

    private static final String NOTIFICATION_CHANEL_NAME = "Systramer channel";
    private static final String NOTIFICATION_CHANEL_ID = "scanning_service_channel_id";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private CollectionReference messageListener;
    private DocumentReference statusListener;
    private Notification notification;
    private Timer timer;
    private int status;

    private TripBinder mLocalbinder = new TripBinder();
    private TripService.CallBack mCallBack;
    private ServiceModel serviceInfo;
    private RouteData routeInfo;

    Context context;

    public static TripService getInstance() {
        return new TripService();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        registerReceiver(myReceiver, new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER));

        routeInfo = getSavedRouteData(getSharedRouteId(this));
        serviceInfo = getSavedServiceModel(getSharedServiceId(this));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

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
                        serviceLocation = location;
                        if (mCallBack != null) {
                            mCallBack.setMap(location);
                        }
                    }
                }
            }
        };

        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Handler handler = new Handler();

        messages = new ArrayList<>();

        if (messageListener == null) {

            messageListener = db
                    .collection("services")
                    .document(Integer.toString(serviceInfo.getId()))
                    .collection("messages");


            messageListener.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    Log.w("MessageResponse", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.size() > 0) {
                    List<DocumentChange> documentChange = snapshot.getDocumentChanges();
                    boolean repeated = false;

                    for (DocumentChange change : documentChange) {
                        HashMap item = (HashMap) change.getDocument().getData();

                        messages.add(new MessageModel(
                                Integer.parseInt(Long.toString((Long) item.get("created_by"))),
                                (boolean) item.get("is_read"),
                                (String) item.get("message"),
                                change.getDocument().getTimestamp("created_at").toDate()
                        ));

                        Collections.sort(messages,
                                (o1, o2) -> o1.getCreated_at().compareTo(o2.getCreated_at()));


                        if (messages.size() > 1) {
                            if (messages.get(messages.size() - 1).getCreated_at().equals(
                                    messages.get(messages.size() - 2).getCreated_at())) {
                                messages.remove(messages.size() - 1);
                                repeated = true;
                            }
                        }
                    }

                    if (mCallBack != null && !repeated) {
                        if (messages.get(messages.size() - 1).getCreated_by() != getSavedUser().getId()) {
                            showSmallNotification(getApplicationContext(), "Nuevo mensaje",
                                    messages.get(messages.size() - 1).getMessage(),
                                    "messages_channel");
                        }
                        mCallBack.onMessageReceived(false, messages.size() - 1);
                    }

                } else {
                    Log.d("MessageResponse", "Current data: null");
                }
            });
        }

        if (statusListener == null) {

            statusListener = db
                    .collection("services")
                    .document(Integer.toString(serviceInfo.getId()));

            statusListener.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.w("MessageResponse", "Listen failed.", e);
                    return;
                } else {
                    status = (int) ((long) documentSnapshot.get("status"));

                    switch (status) {
                        case SERVICE_STATUS_NEW:
                            break;

                        case SERVICE_STATUS_IN_TRANSIT:
                            break;

                        case SERVICE_STATUS_FINISHED:
                            finishTrip();
                            break;

                        case SERVICE_STATUS_CANCELED:
                            finishTrip();
                            break;

                        default:
                            break;
                    }

                }
            });
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    if (carLocation != null) {


                        updateFirebaseLocation((
                                        serviceInfo.getId()),
                                new FirebasePositionModel(
                                        Calendar.getInstance().getTime(),
                                        carLocation,
                                        String.format(Locale.getDefault(),
                                                "%.0f", ((carSpeed) * 3.6) / 1)
                                ));


                        if (getSharedDestinationId(getApplicationContext()) != 0 && mCallBack != null) {
                            Location userLocation = new Location("");
                            Location currentRouteLocation = new Location("");

                            userLocation.setLatitude(carLocation.getLatitude());
                            userLocation.setLongitude(carLocation.getLongitude());

                            Location lastLocation;
                            Float userDistance;

                            InfoModel currentRoute = null;

                            for (InfoModel location : routeInfo.getLocations()) {
                                if (location.getId() == getSharedDestinationId(getApplicationContext())) {
                                    currentRoute = location;
                                    break;
                                }
                            }

                            lastLocation = new Location("");
                            lastLocation.setLatitude(routeInfo.getLocations().get(routeInfo.getLocations().size() - 1).getLocation().getCoordinates().get(1));
                            lastLocation.setLongitude(routeInfo.getLocations().get(routeInfo.getLocations().size() - 1).getLocation().getCoordinates().get(0));

                            currentRouteLocation.setLatitude(currentRoute.getLocation().getCoordinates().get(1));
                            currentRouteLocation.setLongitude(currentRoute.getLocation().getCoordinates().get(0));

                            userDistance = userLocation.distanceTo(lastLocation);


                            if (!getSharedIsStopped(TripService.this)) {
                                if (userDistance <= 60 &&
                                        getSharedDestinationId(getApplicationContext()) == (routeInfo.getLocations().get(routeInfo.getLocations().size() - 1).getId())) {
                                    mCallBack.showFinishTrip();
                                } else {
                                    mCallBack.showCancelTrip();
                                }
                            } else {
                                mCallBack.showStopTrip();
                            }

                            mCallBack.updateDistance();
                            mCallBack.updateSchedulePositions(calculateSchedulePositions(userDistance, routeInfo.getLocations()));
                        }
                    }

                });
            }
        }, 0, 10000);

        CollectionReference positionListener = db
                .collection("services")
                .document(Integer.toString(serviceInfo.getId()))
                .collection("positions");

        positionListener.addSnapshotListener((snapshot, e) ->

        {
            if (e != null) {
                Log.w("PositionResponse", "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.size() > 0) {
                Log.d("PositionResponse", "Current data: " + snapshot.size());
            } else {
                Log.d("PositionResponse", "Current data: null");
            }
        });

        startInForeground(routeInfo);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
// Getting the instance of Binder
        return mLocalbinder;
    }

    // callback Interface
    public interface CallBack {
        void onMessageReceived(boolean firstInsert, int i);

        void setMap(Location location);

        void updateDistance();

        void updateRoute(RouteData newRouteInfo);

        void updateSchedulePositions(ArrayList<Float> distances);

        void updateScheduleAdapter();

        void showCancelTrip();

        void showFinishTrip();

        void showStopTrip();

        void showResumeTrip();
    }

    private void startInForeground(RouteData route) {
        // Create notification intent
        final Intent notificationIntent = new Intent();
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                0
        );

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        // Build notification


        InfoModel currentRoute = null;

        for (InfoModel location : routeInfo.getLocations()) {
            if (location.getId() == getSharedDestinationId(getApplicationContext())) {
                currentRoute = location;
                break;
            }
        }

        notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANEL_ID)
                .setContentTitle(route.getName())
                .setContentText(currentRoute != null ? "Siguiente parada: " + currentRoute.getAddress() : "Parada en progreso")
                .setSmallIcon(R.drawable.icn_main_logo)
                .setContentIntent(pendingIntent)
                .build();

        // Start foreground service
        startForeground(1, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        final NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANEL_ID,
                NOTIFICATION_CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        notificationManager.createNotificationChannel(channel);
    }

    //Custom Binder class
    public class TripBinder extends Binder {
        public TripService getService() {
            return TripService.this;
        }
    }

    // Callback Setter
    public void setCallBack(TripService.CallBack callBack) {
        mCallBack = callBack;

        if (messages != null && messages.size() > 0) {
            mCallBack.onMessageReceived(true, messages.size());
        }

        if (serviceLocation != null) {
            mCallBack.setMap(new Location(serviceLocation));
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        unregisterReceiver(myReceiver);
        return super.onUnbind(intent);
    }

    private void finishTrip() {
        timer.cancel();
        //FinishTrip
        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.delete(RouteData.class);
        mRealm.delete(EvidenceContainerModel.class);
        mRealm.delete(TimesResponse.class);
        mRealm.delete(StartTripModel.class);
        mRealm.delete(ServiceModel.class);
        mRealm.delete(ScheduleListModel.class);
        mRealm.delete(InitTripModel.class);
        mRealm.commitTransaction();
        mRealm.close();

        setSharedTripStatus(context, false);
        setSharedDelayed(context, false);
        setSharedTotalDistance(context, 0);
        setSharedOriginId(context, 0);
        setSharedDestinationId(context, 0);
        setSharedTotalTime(context, 0);
        setSharedDuration(context, 0);
        setSharedStopNumber(context, -1);
        setSharedDistance(context, 0);
        setSharedRouteId(context, 0);
        setSharedServiceId(context, 0);
        setSharedUpdatingTrip(context, false);
        setSharedIsStopped(context, false);
        setSharedStopTime(context, 0);
        setSharedUpdatingTrip(context, false);
        stopSelf();

        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

    }

    private void updateTripInfo(Intent intent) {

        switch (intent.getStringExtra("type")) {
            case "route_changed":
                setSharedUpdatingTrip(getApplicationContext(), true);
                wsGetRouteInfo(context, getSharedRouteId(getApplicationContext()), true, new RouteCallback() {
                    @Override
                    public void baseResponse(RouteData data) {
                        saveRouteData(data);
                        mCallBack.updateRoute(data);
                        if (loaderUpdatingTrip != null && loaderUpdatingTrip.isShowing()) {
                            loaderUpdatingTrip.cancel();
                        }
                        setSharedUpdatingTrip(getApplicationContext(), false);
                    }

                    @Override
                    public void baseError() {
                        setSharedUpdatingTrip(getApplicationContext(), false);
                        if (loaderUpdatingTrip != null && loaderUpdatingTrip.isShowing()) {
                            loaderUpdatingTrip.cancel();
                        }
                    }
                });


                break;
            case "estimations":
                if (intent.getIntExtra("destinationId", -1) != -1 &&
                        intent.getIntExtra("destinationId", -1) !=
                                intent.getIntExtra("currentDestinationId", -1)) {

                    final Intent notificationIntent = new Intent();
                    final PendingIntent pendingIntent = PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            notificationIntent,
                            0
                    );

                    InfoModel currentRoute = null;
                    InfoModel originRoute = null;

                    for (InfoModel location : routeInfo.getLocations()) {
                        if (location.getId() == getSharedDestinationId(getApplicationContext())) {
                            currentRoute = location;
                            break;
                        }
                    }

                    int originId = getSharedOriginId(getApplicationContext());

                    for (InfoModel location : routeInfo.getLocations()) {
                        if (location.getId() == originId) {
                            originRoute = location;
                            break;
                        }
                    }

                    notification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANEL_ID)
                            .setContentTitle(routeInfo.getName())
                            .setContentText(currentRoute != null ? "Siguiente parada: " + currentRoute.getAddress() : "Parada en progreso")
                            .setSmallIcon(R.drawable.icn_main_logo)
                            .setContentIntent(pendingIntent)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, notification);


                    ScheduleListModel localScheduleListModel = new ScheduleListModel(serviceInfo.getId(), new RealmList<>());

                    for (ScheduleModel scheduleModel : getScheduleListModel(serviceInfo.getId()).getScheduleModels()) {
                        localScheduleListModel.getScheduleModels().add(scheduleModel);
                    }

                    localScheduleListModel.getScheduleModels().add(new ScheduleModel(
                            originRoute.getAddress(),
                            Calendar.getInstance().getTime().toString(),
                            Float.toString(carSpeed)));

                    saveScheduleList(localScheduleListModel);
                    scheduleModels = new ScheduleListModel(serviceInfo.getId(), getScheduleListModel(serviceInfo.getId()).getScheduleModels());

                    if (mCallBack != null) {
                        mCallBack.updateScheduleAdapter();
                    }
                }

                break;

            case "delayed":


                break;
        }

    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTripInfo(intent);
        }
    };
}


