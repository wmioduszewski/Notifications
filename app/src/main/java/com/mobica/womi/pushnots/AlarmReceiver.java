package com.mobica.womi.pushnots;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobica.womi.pushnots.model.CompleteNotificationInfo;
import com.mobica.womi.pushnots.model.NotificationModel;
import com.mobica.womi.pushnots.model.SerializationData;
import com.mobica.womi.pushnots.storage.StorageManager;

import java.io.IOException;

/**
 * Created by womi on 2016-03-10.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == null) return;

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            scheduleAlarmsFromBackup(context);
        } else if (intent.getAction().equals(MainActivity.ACTION_ID)) {
            NotificationModel notificationModel = (NotificationModel) intent.getExtras().getSerializable(MainActivity.ARGUMENT_ID);
            NotificationBuilder builder = new NotificationBuilder(notificationModel);
            Notification notification = builder.build(context);
            Notifier.notify(context, notification);
        }
    }

    private void scheduleAlarmsFromBackup(Context context) {
        SerializationData serializationData = null;
        try {
            serializationData = StorageManager.readData(context);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        AlarmScheduler scheduler = new AlarmScheduler(context);

        if (serializationData.getNotifications() != null && serializationData.getNotifications().size() > 0)
            for (CompleteNotificationInfo cni : serializationData.getNotifications()) {
                PendingIntent pendingAlarmIntent = scheduler.getPendingIntentForAlarm(cni.getNotificationModel());
                scheduler.scheduleAlarm(cni, pendingAlarmIntent);
            }
    }
}
