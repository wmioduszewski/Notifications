package com.mobica.womi.pushnots.model;

import com.mobica.womi.pushnots.R;

import java.io.Serializable;

/**
 * Created by womi on 2016-03-15.
 */
public class NotificationModel implements Serializable {
    private boolean isLongContent = false;
    private String title;

    //small image is set by default because notification won't be shown without it
    //you can change it to another with setSmallImage method
    private int smallImageId = R.drawable.star;
    private int largeImageId = -1;
    private boolean isAutoCancel = false;

    public boolean isLongContent() {
        return isLongContent;
    }

    public void setIsLongContent(boolean isLongContent) {
        this.isLongContent = isLongContent;
    }

    public String getTitle() {
        return title == null ? "Default title" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSmallImageId() {
        return smallImageId;
    }

    public void setSmallImageId(int smallImageId) {
        this.smallImageId = smallImageId;
    }

    public int getLargeImageId() {
        return largeImageId;
    }

    public void setLargeImageId(int largeImageId) {
        this.largeImageId = largeImageId;
    }

    public boolean isAutoCancel() {
        return isAutoCancel;
    }

    public void setIsAutoCancel(boolean isAutoCancel) {
        this.isAutoCancel = isAutoCancel;
    }

    public static NotificationModel getSampleNotificationModel(String title) {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setIsAutoCancel(true);
        notificationModel.setTitle(title);
        notificationModel.setIsLongContent(false);
        return notificationModel;
    }
}
