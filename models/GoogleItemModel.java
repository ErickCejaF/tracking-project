package io.pixan.systramer.models;

import io.realm.RealmObject;

public class GoogleItemModel extends RealmObject {
    private String text;
    private int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}