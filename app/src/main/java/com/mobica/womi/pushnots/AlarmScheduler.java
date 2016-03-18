package com.mobica.womi.pushnots;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mobica.womi.pushnots.model.CompleteNotificationInfo;
import com.mobica.womi.pushnots.model.DelayModel;
import com.mobica.womi.pushnots.model.NotificationModel;
import com.mobica.womi.pushnots.storage.StorageManager;

/**
 * Created by womi on 2016-03-17.
 */
public class AlarmScheduler {

    private AlarmManager alarmManager;
    private Context context;
    private PendingIntent pendingAlarmScheduled;

    public AlarmScheduler(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleAlarm(CompleteNotificationInfo completeNotificationInfo, PendingIntent pendingIntent) {
        StorageManager.add(context, completeNotificationInfo);

        DelayModel delayModel = completeNotificationInfo.getDelayModel();

        long interval = delayModel.getRepetitionIntervalInMinutes() * 1000 * 60;
        long start = delayModel.getStartTime().getTimeInMillis();

        if (delayModel.isRepeatable()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start, interval, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, start, pendingIntent);
        }

        Toast.makeText(context, "Notification sent for appearance", Toast.LENGTH_LONG).show();
    }

    public PendingIntent getPendingIntentForAlarm(NotificationModel notificationDetails) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(MainActivity.ARGUMENT_ID, notificationDetails);
        alarmIntent.setAction(MainActivity.ACTION_ID);
        pendingAlarmScheduled = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingAlarmScheduled;
    }

    public void cancelAlarm(PendingIntent pendingIntent) {
        //hack: to canccel rescheduled intent after reboot
        if(pendingIntent==null)
            pendingIntent = getPendingIntentForAlarm(null);

        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
        StorageManager.cancelAll(context);
    }
}
