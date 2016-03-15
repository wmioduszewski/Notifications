package com.mobica.womi.pushnots;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by womi on 2016-03-11.
 */
public class Notifier {

    private static int id = 0;

    public static void notify(Context context, Notification notification) {
        NotificationManager mNM = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNM.notify(id++, notification);
    }
}
