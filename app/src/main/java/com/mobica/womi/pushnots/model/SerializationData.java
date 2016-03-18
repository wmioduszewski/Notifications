package com.mobica.womi.pushnots.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by womi on 2016-03-17.
 */
public class SerializationData implements Serializable {

    private ArrayList<CompleteNotificationInfo> notifications;

    public SerializationData() {
        notifications = new ArrayList<CompleteNotificationInfo>();
    }

    public ArrayList<CompleteNotificationInfo> getNotifications() {
        return notifications;
    }

    public SerializationData removeOldEntries() {
        long now = Calendar.getInstance().getTimeInMillis();
        Iterator<CompleteNotificationInfo> it = notifications.iterator();
        while (it.hasNext()) {
            CompleteNotificationInfo cni = it.next();
            if (cni.getDelayModel() == null || cni.getDelayModel().getStartTime() == null || cni.getDelayModel().getStartTime().getTimeInMillis() < now) {
                it.remove();
            }
        }
        return this;
    }
}
