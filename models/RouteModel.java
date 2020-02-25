package io.pixan.systramer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RouteModel extends RealmObject implements Parcelable {

    @PrimaryKey
    private int id;
    private String name;
    private int route_type_id;
    private int client_id;
    private String created_at;
    private String updated_at;

    public RouteModel(){
    }

    protected RouteModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        route_type_id = in.readInt();
        client_id = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<RouteModel> CREATOR = new Creator<RouteModel>() {
        @Override
        public RouteModel createFromParcel(Parcel in) {
            return new RouteModel(in);
        }

        @Override
        public RouteModel[] newArray(int size) {
            return new RouteModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoute_type_id() {
        return route_type_id;
    }

    public void setRoute_type_id(int route_type_id) {
        this.route_type_id = route_type_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(route_type_id);
        dest.writeInt(client_id);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}
