package com.mobica.womi.pushnots.model;

import java.io.Serializable;

/**
 * Created by womi on 2016-03-17.
 */
public class CompleteNotificationInfo implements Serializable {

    private DelayModel delayModel;
    private NotificationModel notificationModel;

    public CompleteNotificationInfo(DelayModel delayModel, NotificationModel notificationModel) {
        this.delayModel = delayModel;
        this.notificationModel = notificationModel;
    }

    public DelayModel getDelayModel() {
        return delayModel;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

}
