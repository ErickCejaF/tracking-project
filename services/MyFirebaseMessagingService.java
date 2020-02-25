package io.pixan.systramer.services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import static io.pixan.systramer.utils.SharedPreferences.getSharedDestinationId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.getSharedShowedDelayed;
import static io.pixan.systramer.utils.SharedPreferences.getSharedStopNumber;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDelayed;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDestinationId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDistance;
import static io.pixan.systramer.utils.SharedPreferences.setSharedIsStopped;
import static io.pixan.systramer.utils.SharedPreferences.setSharedOriginId;
import static io.pixan.systramer.utils.SharedPreferences.setSharedShowedDelayed;
import static io.pixan.systramer.utils.SharedPreferences.setSharedShowedDelayedAlert;
import static io.pixan.systramer.utils.SharedPreferences.setSharedStopNumber;
import static io.pixan.systramer.utils.SharedPreferences.setSharedStopTime;
import static io.pixan.systramer.utils.SharedPreferences.setSharedDuration;
import static io.pixan.systramer.utils.SharedPreferences.setSharedToken;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTotalDistance;
import static io.pixan.systramer.utils.SharedPreferences.setSharedTotalTime;
import static io.pixan.systramer.utils.Utils.showSmallNotification;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "Remote Message";
    public static final String INTENT_FILTER = "INTENT_FILTER";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
        setSharedToken(getApplicationContext(), s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data type " + remoteMessage.getData().get("type"));
            try {
                Log.d(TAG, "Message data time " + remoteMessage.getData().get("duration"));
                Log.d(TAG, "Message data distance " + remoteMessage.getData().get("distance"));
                Log.d(TAG, "Message data delayed " + remoteMessage.getData().get("delayed"));
                Log.d(TAG, "Message data destinationId " + remoteMessage.getData().get("destinationId"));
                Log.d(TAG, "Message data originId " + remoteMessage.getData().get("originId"));
                Log.d(TAG, "Message data isStop " + remoteMessage.getData().get("isStop"));
                Log.d(TAG, "Message data stopDuration " + remoteMessage.getData().get("stopDuration"));

            } catch (Exception ignored) {
            }

            Intent intent = new Intent(INTENT_FILTER);
            intent.putExtra("type", remoteMessage.getData().get("type"));

            if (remoteMessage.getData().get("destinationId") != null) {
                intent.putExtra("currentDestinationId", Integer.parseInt(remoteMessage.getData().get("destinationId")));
                intent.putExtra("destinationId", getSharedDestinationId(getApplicationContext()));
            }

            switch (remoteMessage.getData().get("type")) {
                case "the_end":
                    showSmallNotification(getApplicationContext(), "Viaje terminado", "Su viaje ha terminado", "finished_channel");
                    setSharedShowedDelayedAlert(getApplicationContext(), false);
                    setSharedShowedDelayed(getApplicationContext(), false);
                    break;
                case "route_changed":
                    showSmallNotification(getApplicationContext(), "Ruta nueva", "Su ruta ha sido cambiada", "changed_route_channel");
                    setSharedShowedDelayed(getApplicationContext(), false);
                    setSharedShowedDelayedAlert(getApplicationContext(), false);
                    saveMainInfo(remoteMessage);
                    break;
                case "estimations":
                    saveMainInfo(remoteMessage);
                    break;

                case "delayed":
                    if (!getSharedShowedDelayed(getApplicationContext()) && !getSharedIsStopped(getApplicationContext())) {
                        setSharedShowedDelayed(getApplicationContext(), true);
                        showSmallNotification(getApplicationContext(), "Retraso", "Se encuentra con retraso", "delayed_channel");
                    }

                    saveMainInfo(remoteMessage);
                    break;

                case "push_init_service":
                    intent.putExtra("service_token", remoteMessage.getData().get("service_token"));
                    break;
                default:
                    break;
            }

            sendBroadcast(intent);
        }
    }

    private void sendDefaultFunction() {
        Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra("type", "default_type");
        sendBroadcast(intent);
    }

    private void changeStop(RemoteMessage remoteMessage) {

        if (Integer.parseInt(remoteMessage.getData().get("destinationId"))
                != getSharedDestinationId(getApplicationContext())) {
            setSharedDestinationId(MyFirebaseMessagingService.this, Integer.parseInt(remoteMessage.getData().get("destinationId")));
            setSharedShowedDelayed(getApplicationContext(), false);
            setSharedShowedDelayedAlert(getApplicationContext(), false);
            setSharedStopNumber(getApplicationContext(), getSharedStopNumber(getApplicationContext()) + 1);
        }


    }

    private void saveMainInfo(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().get("stopDuration") != null) {
            if (!remoteMessage.getData().get("stopDuration").equals("null")) {
                Calendar rightNow = Calendar.getInstance();
                rightNow.add(Calendar.SECOND, Integer.parseInt(remoteMessage.getData().get("stopDuration")));
                long hour = rightNow.getTime().getTime();

                setSharedStopTime(MyFirebaseMessagingService.this, hour);

                if (Integer.parseInt(remoteMessage.getData().get("stopDuration")) > 0) {
                    setSharedIsStopped(MyFirebaseMessagingService.this, true);
                } else {
                    setSharedIsStopped(MyFirebaseMessagingService.this, false);
                    changeStop(remoteMessage);
                }
            } else {
                changeStop(remoteMessage);
            }

        } else {
            changeStop(remoteMessage);
        }

        if (remoteMessage.getData().get("distance") != null)
            setSharedDistance(MyFirebaseMessagingService.this, Integer.parseInt(remoteMessage.getData().get("distance")));

        if (remoteMessage.getData().get("duration") != null) {
            setSharedDuration(MyFirebaseMessagingService.this, Integer.parseInt(remoteMessage.getData().get("duration")));
        }

        if (remoteMessage.getData().get("finishDistance") != null) {
            setSharedTotalDistance(MyFirebaseMessagingService.this, Integer.parseInt(remoteMessage.getData().get("finishDistance")));
        }

        if (remoteMessage.getData().get("finishDuration") != null) {
            setSharedTotalTime(MyFirebaseMessagingService.this, Integer.parseInt(remoteMessage.getData().get("finishDuration")));
        }

        if (remoteMessage.getData().get("originId") != null)
            setSharedOriginId(MyFirebaseMessagingService.this, Integer.parseInt(remoteMessage.getData().get("originId")));

        if (remoteMessage.getData().get("delayed") != null)
            setSharedDelayed(MyFirebaseMessagingService.this, Boolean.parseBoolean(remoteMessage.getData().get("delayed")));

    }


}