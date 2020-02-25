package io.pixan.systramer.models;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RowModel extends RealmObject {
    RealmList<ElementModel> elements;

    public RealmList<ElementModel> getElements() {
        return elements;
    }

    public void setElements(RealmList<ElementModel> elements) {
        this.elements = elements;
    }
}
