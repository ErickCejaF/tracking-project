package io.pixan.systramer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MainLocationModel extends RealmObject implements Parcelable {
    private String type;
    private RealmList<Double> coordinates;

    public MainLocationModel(){

    }

    protected MainLocationModel(Parcel in) {
        type = in.readString();
    }

    public static final Creator<MainLocationModel> CREATOR = new Creator<MainLocationModel>() {
        @Override
        public MainLocationModel createFromParcel(Parcel in) {
            return new MainLocationModel(in);
        }

        @Override
        public MainLocationModel[] newArray(int size) {
            return new MainLocationModel[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RealmList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(RealmList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
    }
}
