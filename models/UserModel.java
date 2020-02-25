package io.pixan.systramer.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserModel extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private String access_token;
    private float expires_in;

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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public float getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(float expires_in) {
        this.expires_in = expires_in;
    }
}
