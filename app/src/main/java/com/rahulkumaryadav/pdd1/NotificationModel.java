package com.rahulkumaryadav.pdd1;

public class NotificationModel {

    private String image, body;
    private boolean read;

    public NotificationModel(String image, String body, boolean read) {
        this.image = image;
        this.body = body;
        this.read = read;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
