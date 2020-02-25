package io.pixan.systramer.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thanosfisherman.mayi.MayI;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.LoginActivity;
import io.pixan.systramer.callbacks.PermissionCallback;
import io.pixan.systramer.data.RouteData;
import io.pixan.systramer.models.EvidenceContainerModel;
import io.pixan.systramer.models.FirebasePositionModel;
import io.pixan.systramer.models.InfoModel;
import io.pixan.systramer.models.InitTripModel;
import io.pixan.systramer.models.MessageModel;
import io.pixan.systramer.models.ScheduleListModel;
import io.pixan.systramer.models.ServiceModel;
import io.pixan.systramer.models.StartTripModel;
import io.pixan.systramer.models.UserModel;
import io.pixan.systramer.responses.TimesResponse;
import io.pixan.systramer.rest.ApiRestAdapter;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static io.pixan.systramer.utils.SharedPreferences.setSharedDelayed;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDestinationId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDistance;
import static io.pixan.systramer.utils.SharedPreferences.setSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.setSharedOriginId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedRouteId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedShowedDelayed;
import static io.pixan.systramer.utils.SharedPreferences.setSharedShowedDelayedAlert;
import static io.pixan.systramer.utils.SharedPreferences.setSharedStopNumber;
import static io.pixan.systramer.utils.SharedPreferences.setSharedStopTime;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDuration;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTotalDistance;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTotalTime;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTripStatus;
import static io.pixan.systramer.utils.SharedPreferences.setSharedUpdatingTrip;

public class Utils {

    public static void saveUser(UserModel userModel) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(userModel);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public static void saveRouteData(RouteData routeData) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(routeData);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public static void saveTimesData(TimesResponse timesResponse) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(timesResponse);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public static void saveServiceData(ServiceModel serviceModel) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(serviceModel);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public static void saveStartTripModel(StartTripModel startTripModel) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(startTripModel);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public static void saveScheduleList(ScheduleListModel scheduleListModel) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(scheduleListModel);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public static void saveInitTripModel(InitTripModel initTripModel) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(initTripModel);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public static StartTripModel getStartTripModel(int serviceId) {
        StartTripModel startTripModel = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        StartTripModel searching = mRealm.where(StartTripModel.class)
                .equalTo("serviceId", serviceId)
                .findFirst();

        if (searching != null) {
            startTripModel = searching;
        }

        mRealm.commitTransaction();
        mRealm.close();
        return startTripModel;
    }

    public static InitTripModel getSavedInitTripModel(int id) {
        InitTripModel initTripModel = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        RealmResults<InitTripModel> searching = mRealm.where(InitTripModel.class)
                .equalTo("id", id)
                .findAll();

        if (searching != null && !searching.isEmpty()) {
            initTripModel = searching.get(0);
        }

        mRealm.commitTransaction();
        mRealm.close();
        return initTripModel;
    }

    public static ScheduleListModel getScheduleListModel(int serviceId) {
        ScheduleListModel scheduleListModel = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        ScheduleListModel searching = mRealm.where(ScheduleListModel.class)
                .equalTo("serviceId", serviceId)
                .findFirst();

        if (searching != null) {
            scheduleListModel = searching;
        }

        mRealm.commitTransaction();
        mRealm.close();
        return scheduleListModel;
    }

    public static TimesResponse getSavedTimes(int stopId) {
        TimesResponse timesResponse = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        RealmResults<TimesResponse> searching = mRealm.where(TimesResponse.class)
                .equalTo("stopId", stopId)
                .findAll();

        if (searching != null && !searching.isEmpty()) {
            timesResponse = searching.get(0);
        }

        mRealm.commitTransaction();
        mRealm.close();
        return timesResponse;
    }

    public static EvidenceContainerModel getSavedEvidence(String name) {
        EvidenceContainerModel evidence = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        RealmResults<EvidenceContainerModel> searching = mRealm.where(EvidenceContainerModel.class)
                .equalTo("name", name)
                .findAll();

        if (searching != null && !searching.isEmpty()) {
            evidence = searching.get(0);
        }

        mRealm.commitTransaction();
        mRealm.close();
        return evidence;
    }

    public static RouteData getSavedRouteData(int id) {
        RouteData routeData = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        RealmResults<RouteData> searching = mRealm.where(RouteData.class)
                .equalTo("id", id)
                .findAll();

        if (searching != null && !searching.isEmpty()) {
            routeData = searching.get(0);
        }

        mRealm.commitTransaction();
        mRealm.close();

        return routeData;
    }

    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }


    public static ServiceModel getSavedServiceModel(int id) {
        ServiceModel serviceModel = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        RealmResults<ServiceModel> searching = mRealm.where(ServiceModel.class)
                .equalTo("id", id)
                .findAll();

        if (searching != null && !searching.isEmpty()) {
            serviceModel = searching.get(0);
        }

        mRealm.commitTransaction();
        mRealm.close();
        return serviceModel;
    }

    public static void logOut(Context context) {
        new ApiRestAdapter().getApiRest().wsLogout();
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();

        setSharedTripStatus(context, false);
        setSharedShowedDelayedAlert(context, false);
        setSharedShowedDelayed(context, false);
        setSharedDelayed(context, false);
        setSharedTotalDistance(context, 0);
        setSharedOriginId(context, 0);
        setSharedDestinationId(context, 0);
        setSharedDuration(context, 0);
        setSharedStopNumber(context, -1);
        setSharedDistance(context, 0);
        setSharedRouteId(context, 0);
        setSharedTotalTime(context, 0);
        setSharedServiceId(context, 0);
        setSharedIsStopped(context, false);
        setSharedStopTime(context, 0);
        setSharedUpdatingTrip(context, false);


        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void sendMessage(int id, MessageModel message) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db
                .collection("services")
                .document(Integer.toString(id))
                .collection("messages")
                .add(message)
                .addOnFailureListener(e -> {

                })
                .addOnSuccessListener(documentReference -> {

                });

    }

    public static void updateFirebaseLocation(int id, FirebasePositionModel position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db
                .collection("services")
                .document(Integer.toString(id))
                .collection("positions")
                .add(position)
                .addOnFailureListener(e -> {

                })
                .addOnSuccessListener(documentReference -> {

                });

    }

    public static UserModel getSavedUser() {
        UserModel userModel = null;

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        RealmResults<UserModel> searching = mRealm.where(UserModel.class)
                .findAll();

        if (searching != null && !searching.isEmpty()) {
            userModel = searching.get(0);
        }

        mRealm.commitTransaction();

        return userModel;
    }

    public static String formatDate(String serverDate, SimpleDateFormat outputFormat) {
        String date = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            //Conversion of input String to date
            //old date format to new date format
            date = outputFormat.format(df.parse(serverDate));
            System.out.println(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }


        return date;
    }

    public static float findAverageWithoutUsingStream(Float[] array) {
        Float sum = 0.0f;

        for (Float i : array) {
            sum += i;
        }
        return sum / array.length;
    }

    public static void callEmergency(Activity context) {

        MayI.Companion.withActivity(context)
                .withPermission(Manifest.permission.CALL_PHONE)
                .onResult((Function1<PermissionBean, Unit>) permissionBean -> {
                    permissionResultSingle(context, permissionBean, new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "911"));
                            context.startActivity(intent);
                        }

                        @Override
                        public void permissionFailed() {
                        }
                    });
                    return null;
                })
                .onRationale((permissionBean, permissionToken) -> {
                    permissionRationaleSingle(context, permissionBean, permissionToken, new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "911"));
                            context.startActivity(intent);
                        }

                        @Override
                        public void permissionFailed() {
                        }
                    });
                    return null;
                }).check();
    }


    public static void permissionResultSingle(Context context, PermissionBean permission, PermissionCallback permissionCallback) {
        if (permission.isGranted()) {
            permissionCallback.permissionGranted();
        } else {
            permissionCallback.permissionFailed();
        }
    }

    public static void permissionRationaleSingle(Context context, PermissionBean bean, PermissionToken token, PermissionCallback permissionCallback) {
//        Toast.makeText(context, "Should show rationale for " + bean.getSimpleName() + " permission", Toast.LENGTH_LONG).show();
//        token.skipPermissionRequest();
        if (bean.isGranted()) {
            permissionCallback.permissionGranted();
        } else {
            permissionCallback.permissionFailed();
        }
    }


    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void takeScreenshot(Activity context) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = context.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

//            openScreenshot(context, imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }


    public static String getFormatedHour(int seconds) {
        try {
            Calendar rightNow = Calendar.getInstance();
            rightNow.add(Calendar.SECOND, seconds);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            return sdf.format(rightNow.getTime());
        } catch (Exception e) {
            return null;
        }
    }


    public static void openScreenshot(Context context, File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);
    }


    public static ArrayList<Float> calculateSchedulePositions(Float userDistance, RealmList<InfoModel> infoModels) {
        ArrayList<Float> distances = new ArrayList<>();
        Location lastLocation;

        lastLocation = new Location("");
        lastLocation.setLatitude(infoModels.get(infoModels.size() - 1).getLocation().getCoordinates().get(1));
        lastLocation.setLongitude(infoModels.get(infoModels.size() - 1).getLocation().getCoordinates().get(0));

        distances.add(userDistance);

        for (InfoModel infoModel : infoModels) {
            Location location = new Location("");
            location.setLatitude(infoModel.getLocation().getCoordinates().get(1));
            location.setLongitude(infoModel.getLocation().getCoordinates().get(0));
            distances.add(location.distanceTo(lastLocation));
        }

        return distances;
    }

    public static boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }

    public static void showSmallNotification(Context context, String title, String message, String channelId) {
        String CHANNEL_NAME = "Notification";

        // I removed one of the semi-colons in the next line of code
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // I would suggest that you use IMPORTANCE_DEFAULT instead of IMPORTANCE_HIGH
            NotificationChannel channel = new NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.enableLights(true);
            // Did you mean to set the property to enable Show Badge?
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), channelId)
                .setVibrate(new long[]{0, 100})
                .setPriority(Notification.PRIORITY_MAX)
                .setLights(Color.BLUE, 3000, 3000)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(inboxStyle)
                .setSmallIcon(R.drawable.icn_main_logo)
                .setContentText(message);
        // Removed .build() since you use it below...no need to build it twice

        // Don't forget to set the ChannelID!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(channelId);
        }

        notificationManager.notify(channelId, 1, notificationBuilder.build());
    }


//    public void startUserLocation(Activity activity) {
//        MayI.Companion.withActivity(activity)
//                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                .onResult((Function1<PermissionBean, Unit>) permissionBean -> {
//                    permissionResultSingle(activity, permissionBean, new PermissionCallback() {
//                        @Override
//                        public void permissionGranted() {
//                            startLocationUpdates(activity);
//                        }
//
//                        @Override
//                        public void permissionFailed() {
//                        }
//                    });
//                    return null;
//                })
//                .onRationale((permissionBean, permissionToken) -> {
//                    permissionRationaleSingle(activity, permissionBean, permissionToken, new PermissionCallback() {
//                        @Override
//                        public void permissionGranted() {
//                            startLocationUpdates(activity);
//                        }
//                        @Override
//                        public void permissionFailed() {
//                        }
//                    });
//                    return null;
//                }).check();
//    }
//
//    private void startLocationUpdates(Activity activity) {
//
//    }


}
