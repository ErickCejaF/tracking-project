package io.pixan.systramer.models;

import java.util.Date;

public class MessageModel {
    private int created_by;
    private boolean is_read;
    private String message;
    private Date created_at;

    public MessageModel(int created_by, boolean is_read, String message, Date created_at) {
        this.created_by = created_by;
        this.is_read = is_read;
        this.message = message;
        this.created_at = created_at;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
