package io.pixan.systramer.responses;

import java.util.ArrayList;

import io.pixan.systramer.models.RowModel;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TimesResponse extends RealmObject {
    @PrimaryKey
    private int stopId;
    private RealmList<String> destination_addresses;
    private RealmList<String> origin_addresses;
    private RealmList<RowModel> rows;

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public RealmList<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(RealmList<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public RealmList<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(RealmList<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public RealmList<RowModel> getRows() {
        return rows;
    }

    public void setRows(RealmList<RowModel> rows) {
        this.rows = rows;
    }
}
