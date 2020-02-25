package io.pixan.systramer.models;

import java.io.File;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EvidenceModel extends RealmObject {
    private String imageUrl;
    private String comment;

    public EvidenceModel(){
    }

    public EvidenceModel(String imageUrl, String comment) {
        this.imageUrl = imageUrl;
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
