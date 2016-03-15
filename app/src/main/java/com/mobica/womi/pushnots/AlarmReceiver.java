package com.mobica.womi.pushnots;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by womi on 2016-03-10.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationModel notificationModel = (NotificationModel) intent.getExtras().getSerializable(MainActivity.ARGUMENT_ID);
        NotificationBuilder builder = new NotificationBuilder(notificationModel);
        Notification notf = builder.build(context);
        Notifier.notify(context, notf);
    }
}
