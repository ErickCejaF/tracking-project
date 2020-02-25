package io.pixan.systramer.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScheduleListModel extends RealmObject {
    @PrimaryKey
    private int serviceId;
    private RealmList<ScheduleModel> scheduleModels;

    public ScheduleListModel(){
    }

    public ScheduleListModel(int serviceId, RealmList<ScheduleModel> scheduleModels) {
        this.serviceId = serviceId;
        this.scheduleModels = scheduleModels;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public RealmList<ScheduleModel> getScheduleModels() {
        return scheduleModels;
    }

    public void setScheduleModels(RealmList<ScheduleModel> scheduleModels) {
        this.scheduleModels = scheduleModels;
    }
}
