package io.pixan.systramer.models;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EvidenceContainerModel extends RealmObject {

    @PrimaryKey
    private String name;
    private RealmList<EvidenceModel> evidences;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<EvidenceModel> getEvidences() {
        return evidences;
    }

    public void setEvidences(RealmList<EvidenceModel> evidences) {
        this.evidences = evidences;
    }
}
