package io.pixan.systramer.utils;

import android.content.Context;

/**
 * Created by sergioyanez on 1/12/18.
 */

public class SharedPreferences {
    private static String SHARED_PREFERENCES = "SHARED_SYSTRAMER";
    private static String SHARED_TRIP_STATUS = "SHARED_TRIP_STATUS";
    private static String SHARED_SERVICE_ID = "SHARED_SERVICE_ID";
    private static String SHARED_TOKEN = "SHARED_TOKEN";
    private static String SHARED_ROUTE_ID = "SHARED_ROUTE_ID";
    private static String SHARED_DISTANCE = "SHARED_DISTANCE";
    private static String SHARED_DURATION = "SHARED_DURATION";
    private static String SHARED_DELAYED = "SHARED_DELAYED";
    private static String SHARED_DESTINATION_ID = "SHARED_DESTINATION_ID";
    private static String SHARED_ORIGIN_ID = "SHARED_ORIGIN_ID";
    private static String SHARED_SHOWED_DELAYED = "SHARED_SHOWED_DELAYED";
    private static String SHARED_SHOWED_DELAYED_ALERT = "SHARED_SHOWED_DELAYED_ALERT";
    private static String SHARED_UPDATING_TRIP = "SHARED_UPDATING_TRIP";
    private static String SHARED_CURRENT_STOP_NUMBER = "SHARED_CURRENT_STOP_NUMBER";
    private static String SHARED_STOP_TIME = "SHARED_STOP_TIME";
    private static String SHARED_IS_STOPPED = "SHARED_IS_STOPPED";
    private static String SHARED_TOTAL_TIME = "SHARED_TOTAL_TIME";
    private static String SHARED_TOTAL_DISTANCE = "SHARED_TOTAL_DISTANCE";
    private static String SHARED_TIMEOUT_STOP = "SHARED_TIMEOUT_STOP";


    public static int getSharedStopNumber(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(SHARED_CURRENT_STOP_NUMBER, -1);
    }

    public static void setSharedStopNumber(Context context, int data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(SHARED_CURRENT_STOP_NUMBER, data).apply();
    }


    public static float getSharedTotalTime(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getFloat(SHARED_TOTAL_TIME, 0f);
    }

    public static void setSharedTotalTime(Context context, float data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putFloat(SHARED_TOTAL_TIME, data).apply();
    }


    public static float getSharedTotalDistance(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getFloat(SHARED_TOTAL_DISTANCE, 0f);
    }

    public static void setSharedTotalDistance(Context context, float data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putFloat(SHARED_TOTAL_DISTANCE, data).apply();
    }

    public static boolean getSharedUpdatingTrip(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean(SHARED_UPDATING_TRIP, false);
    }

    public static void setSharedUpdatingTrip(Context context, boolean data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(SHARED_UPDATING_TRIP, data).apply();
    }

    public static boolean getSharedShowedDelayedAlert(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean(SHARED_SHOWED_DELAYED_ALERT, false);
    }

    public static void setSharedShowedDelayedAlert(Context context, boolean data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(SHARED_SHOWED_DELAYED_ALERT, data).apply();
    }

    public static boolean getSharedShowedDelayed(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean(SHARED_SHOWED_DELAYED, false);
    }

    public static void setSharedShowedDelayed(Context context, boolean data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(SHARED_SHOWED_DELAYED, data).apply();
    }

    public static boolean getSharedDelayed(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean(SHARED_DELAYED, false);
    }

    public static void setSharedDelayed(Context context, boolean data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(SHARED_DELAYED, data).apply();
    }

    public static int getSharedOriginId(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(SHARED_ORIGIN_ID, 0);
    }

    public static void setSharedOriginId(Context context, int data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(SHARED_ORIGIN_ID, data).apply();
    }

    public static int getSharedDestinationId(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(SHARED_DESTINATION_ID, -1);
    }

    public static void setSharedDestinationId(Context context, int data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(SHARED_DESTINATION_ID, data).apply();
    }

    public static int getSharedDuration(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(SHARED_DURATION, 0);
    }

    public static void setSharedDuration(Context context, int data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(SHARED_DURATION, data).apply();
    }

    public static int getSharedDistance(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(SHARED_DISTANCE, 0);
    }

    public static void setSharedDistance(Context context, int data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(SHARED_DISTANCE, data).apply();
    }

    public static boolean getSharedTripStatus(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean(SHARED_TRIP_STATUS, false);
    }

    public static void setSharedTripStatus(Context context, boolean data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(SHARED_TRIP_STATUS, data).apply();
    }

    public static int getSharedRouteId(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(SHARED_ROUTE_ID, 0);
    }

    public static void setSharedRouteId(Context context, int data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(SHARED_ROUTE_ID, data).apply();
    }

    public static int getSharedServiceId(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(SHARED_SERVICE_ID, 0);
    }

    public static void setSharedServiceId(Context context, int data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(SHARED_SERVICE_ID, data).apply();
    }

    public static String getSharedToken(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(SHARED_TOKEN, "");
    }

    public static void setSharedToken(Context context, String data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putString(SHARED_TOKEN, data).apply();
    }


    public static long getSharedStopTime(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getLong(SHARED_STOP_TIME, 0);
    }

    public static void setSharedStopTime(Context context, long data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putLong(SHARED_STOP_TIME, data).apply();
    }

    public static boolean getSharedIsStopped(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean(SHARED_IS_STOPPED, false);
    }

    public static void setSharedIsStopped(Context context, boolean data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putBoolean(SHARED_IS_STOPPED, data).apply();
    }

    public static Long getSharedTimeoutStop(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getLong(SHARED_CURRENT_STOP_NUMBER, 0);
    }

    public static void setSharedTimeoutStop(Context context, Long data) {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().putLong(SHARED_CURRENT_STOP_NUMBER, data).apply();
    }

}
