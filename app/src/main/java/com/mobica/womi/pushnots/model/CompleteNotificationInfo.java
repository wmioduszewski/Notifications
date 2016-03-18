package com.mobica.womi.pushnots.model;

import java.io.Serializable;

/**
 * Created by womi on 2016-03-17.
 */
public class CompleteNotificationInfo implements Serializable {

    private DelayModel delayModel;
    private NotificationModel notificationModel;

    public DelayModel getDelayModel() {
        return delayModel;
    }

    public void setDelayModel(DelayModel delayModel) {
        this.delayModel = delayModel;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    public void setNotificationModel(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }
}
