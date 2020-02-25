package io.pixan.systramer.responses;

import io.pixan.systramer.models.UserModel;
import io.realm.RealmObject;

public class LoginResponse extends RealmObject {
    private UserModel data;

    public UserModel getData() {
        return data;
    }

    public void setData(UserModel data) {
        this.data = data;
    }
}