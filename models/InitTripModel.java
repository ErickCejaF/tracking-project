package io.pixan.systramer.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class InitTripModel extends RealmObject {

    @PrimaryKey
    private int id;
    private int service_type_id;
    private int route_id;
    private int operator_id;
    private String operator_phone;
    private String declared_value;
    private String departure_date;
    private int client_id;
    private int status;
    private int created_by;
    private int updated_by;
    private String created_at;
    private String updated_at;
    private int unix_departure_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(int service_type_id) {
        this.service_type_id = service_type_id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(int operator_id) {
        this.operator_id = operator_id;
    }

    public String getOperator_phone() {
        return operator_phone;
    }

    public void setOperator_phone(String operator_phone) {
        this.operator_phone = operator_phone;
    }

    public String getDeclared_value() {
        return declared_value;
    }

    public void setDeclared_value(String declared_value) {
        this.declared_value = declared_value;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
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
}




