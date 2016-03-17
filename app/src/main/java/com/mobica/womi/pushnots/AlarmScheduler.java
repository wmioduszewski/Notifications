package com.mobica.womi.pushnots;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.Toast;

import com.mobica.womi.pushnots.model.DelayModel;

/**
 * Created by womi on 2016-03-17.
 */
public class AlarmScheduler {

    private AlarmManager alarmManager;
    private Context context;

    public AlarmScheduler(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleAlarm(PendingIntent pendingIntent, DelayModel delayModel) {

        long interval = delayModel.getRepetitionIntervalInMinutes() * 1000 * 60;
        long start = delayModel.getStartTime().getTimeInMillis();

        if (delayModel.isRepeatable()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start, interval, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, start, pendingIntent);
        }

        Toast.makeText(context, "Notification sent for appearance", Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm(PendingIntent pendingIntent) {
        alarmManager.cancel(pendingIntent);
    }
}
