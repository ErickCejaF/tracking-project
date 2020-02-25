package io.pixan.systramer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;

public class InfoModel extends RealmObject  implements Parcelable {
    private int route_id;
    private String address;
    private boolean is_destination;
    private boolean is_site_of_interest;
    private String name;
    private int order;
    private MainLocationModel location;
    private int id;


    public InfoModel(){
    }

    protected InfoModel(Parcel in) {
        route_id = in.readInt();
        address = in.readString();
        is_destination = in.readByte() != 0;
        is_site_of_interest = in.readByte() != 0;
        name = in.readString();
        order = in.readInt();
        location = in.readParcelable(MainLocationModel.class.getClassLoader());
        id = in.readInt();
    }

    public static final Creator<InfoModel> CREATOR = new Creator<InfoModel>() {
        @Override
        public InfoModel createFromParcel(Parcel in) {
            return new InfoModel(in);
        }

        @Override
        public InfoModel[] newArray(int size) {
            return new InfoModel[size];
        }
    };

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIs_destination() {
        return is_destination;
    }

    public void setIs_destination(boolean is_destination) {
        this.is_destination = is_destination;
    }

    public boolean isIs_site_of_interest() {
        return is_site_of_interest;
    }

    public void setIs_site_of_interest(boolean is_site_of_interest) {
        this.is_site_of_interest = is_site_of_interest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public MainLocationModel getLocation() {
        return location;
    }

    public void setLocation(MainLocationModel location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(route_id);
        dest.writeString(address);
        dest.writeByte((byte) (is_destination ? 1 : 0));
        dest.writeByte((byte) (is_site_of_interest ? 1 : 0));
        dest.writeString(name);
        dest.writeInt(order);
        dest.writeParcelable(location, flags);
        dest.writeInt(id);
    }
}
