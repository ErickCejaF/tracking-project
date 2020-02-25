package io.pixan.systramer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ServiceModel extends RealmObject implements Parcelable {

    @PrimaryKey
    private int id;
    private int ServiceModel;
    private int route_id;
    private String operator_phone;
    private String declare_value;
    private String departure_date;
    private String departed_at;
    private int client_id;
    private int status;
    private int unit_id;
    private int created_by;
    private int updated_by;
    private String created_at;
    private String updated_at;
    private int unix_departure_date;
    private RouteModel route;

    public ServiceModel(){

    }


    protected ServiceModel(Parcel in) {
        id = in.readInt();
        ServiceModel = in.readInt();
        route_id = in.readInt();
        operator_phone = in.readString();
        declare_value = in.readString();
        departure_date = in.readString();
        departed_at = in.readString();
        client_id = in.readInt();
        status = in.readInt();
        unit_id = in.readInt();
        created_by = in.readInt();
        updated_by = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        unix_departure_date = in.readInt();
        route = in.readParcelable(RouteModel.class.getClassLoader());
    }

    public static final Creator<ServiceModel> CREATOR = new Creator<ServiceModel>() {
        @Override
        public ServiceModel createFromParcel(Parcel in) {
            return new ServiceModel(in);
        }

        @Override
        public ServiceModel[] newArray(int size) {
            return new ServiceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(ServiceModel);
        dest.writeInt(route_id);
        dest.writeString(operator_phone);
        dest.writeString(declare_value);
        dest.writeString(departure_date);
        dest.writeString(departed_at);
        dest.writeInt(client_id);
        dest.writeInt(status);
        dest.writeInt(unit_id);
        dest.writeInt(created_by);
        dest.writeInt(updated_by);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeInt(unix_departure_date);
        dest.writeParcelable(route, flags);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceModel() {
        return ServiceModel;
    }

    public void setServiceModel(int serviceModel) {
        ServiceModel = serviceModel;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getOperator_phone() {
        return operator_phone;
    }

    public void setOperator_phone(String operator_phone) {
        this.operator_phone = operator_phone;
    }

    public String getDeclare_value() {
        return declare_value;
    }

    public void setDeclare_value(String declare_value) {
        this.declare_value = declare_value;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getDeparted_at() {
        return departed_at;
    }

    public void setDeparted_at(String departed_at) {
        this.departed_at = departed_at;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public int getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(int updated_by) {
        this.updated_by = updated_by;
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

    public int getUnix_departure_date() {
        return unix_departure_date;
    }

    public void setUnix_departure_date(int unix_departure_date) {
        this.unix_departure_date = unix_departure_date;
    }

    public RouteModel getRoute() {
        return route;
    }

    public void setRoute(RouteModel route) {
        this.route = route;
    }

    public static Creator<io.pixan.systramer.models.ServiceModel> getCREATOR() {
        return CREATOR;
    }
}
