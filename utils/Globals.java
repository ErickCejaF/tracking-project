package io.pixan.systramer.utils;

import android.location.Location;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.firestore.GeoPoint;

import java.time.temporal.Temporal;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.adapters.ChatAdapter;
import io.pixan.systramer.dialog.LoaderDialog;
import io.pixan.systramer.fragments.TripFragment;
import io.pixan.systramer.models.MessageModel;
import io.pixan.systramer.models.ScheduleListModel;
import io.pixan.systramer.responses.TimesResponse;
import io.realm.RealmList;

public class Globals {
    public static ArrayList<MessageModel> messages;
    public static RecyclerView rvMessages;
    public static float carSpeed = 0;
    public static GeoPoint carLocation;
    public static Location serviceLocation;
    public static ScheduleListModel scheduleModels;
    public static LoaderDialog loaderUpdatingTrip;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////GLOBAL TRIP INFO

}
