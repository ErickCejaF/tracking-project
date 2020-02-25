package io.pixan.systramer.models;

import android.graphics.drawable.Drawable;

public class AlertModel {
    private Drawable icon;
    private String alertTitle;
    private AlertItem[] alertItems;
    private int alertId;

    public AlertModel(Drawable icon, String alertTitle, int alertId, AlertItem[] alertItems) {
        this.icon = icon;
        this.alertTitle = alertTitle;
        this.alertId = alertId;
        this.alertItems = alertItems;
    }

    public AlertItem[] getAlertItems() {
        return alertItems;
    }

    public void setAlertItems(AlertItem[] alertItems) {
        this.alertItems = alertItems;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIconId(Drawable icon) {
        this.icon = icon;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }
}
