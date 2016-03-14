package com.mobica.womi.pushnots;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by womi on 2016-03-11.
 */
public class Notifier {

    private static int id = 0;

    public static void notify(Context context, Notification notification) {
        NotificationManager mNM = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNM.notify(id++, notification);
    }

    public static Notification buildNotification(Context context, String title, String content) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.panda);

        return new Notification.Builder(context)
                .setContentTitle(title)
                //.setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.star)
                .setContentIntent(pendingIntent)
                .setLargeIcon(icon)
                .setStyle(new Notification.BigTextStyle().bigText(content))
                .build();
    }

}
