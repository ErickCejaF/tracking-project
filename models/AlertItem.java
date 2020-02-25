package io.pixan.systramer.models;

public class AlertItem {
    private int id;
    private String Description;

    public AlertItem(int id, String description) {
        this.id = id;
        Description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
