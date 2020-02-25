package io.pixan.systramer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class DatesServicesModels implements Parcelable {
    private ArrayList<ServiceModel> serviceModels;
    private Date date;

    public DatesServicesModels(Date date, ServiceModel serviceModel) {
        this.date = date;
        this.serviceModels = new ArrayList<>();
        this.serviceModels.add(serviceModel);
    }

    public DatesServicesModels() {
    }

    public DatesServicesModels(ArrayList<ServiceModel> serviceModels, Date date) {
        this.serviceModels = serviceModels;
        this.date = date;
    }

    protected DatesServicesModels(Parcel in) {
        serviceModels = in.createTypedArrayList(ServiceModel.CREATOR);
    }

    public static final Creator<DatesServicesModels> CREATOR = new Creator<DatesServicesModels>() {
        @Override
        public DatesServicesModels createFromParcel(Parcel in) {
            return new DatesServicesModels(in);
        }

        @Override
        public DatesServicesModels[] newArray(int size) {
            return new DatesServicesModels[size];
        }
    };

    public ArrayList<ServiceModel> getServiceModels() {
        return serviceModels;
    }

    public void setServiceModels(ArrayList<ServiceModel> serviceModels) {
        this.serviceModels = serviceModels;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(serviceModels);
    }
}
