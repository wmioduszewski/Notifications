package com.mobica.womi.pushnots;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mobica.womi.pushnots.model.NotificationModel;

/**
 * Created by womi on 2016-03-15.
 */
public class NotificationBuilder {

    private static final String shortContent = "This is short Mobica notification";
    private static final String longContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    private NotificationModel model;
    private Notification.Builder builder;
    private Context context;

    public NotificationBuilder(NotificationModel model) {
        this.model = model;
    }

    public Notification build(Context context) {
        this.context = context;
        builder = new Notification.Builder(this.context);
        setContent();
        setTitle();
        setAutoCancelling();
        setSmallIcon();
        setLargeIcon();
        setContentIntent();

        return builder.build();
    }

    private void setContentIntent() {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        builder.setContentIntent(pendingIntent);
    }

    private void setSmallIcon() {
        if (model.getSmallImageId() != -1)
            builder.setSmallIcon(model.getSmallImageId());
    }

    private void setLargeIcon() {
        if (model.getLargeImageId() != -1) {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    model.getLargeImageId());
            builder.setLargeIcon(icon);
        }
    }

    private void setAutoCancelling() {
        builder.setAutoCancel(model.isAutoCancel());
    }

    private void setTitle() {
        builder.setContentTitle(model.getTitle());
    }

    private void setContent() {
        if (model.isLongContent()) {
            builder.setStyle(new Notification.BigTextStyle().bigText(longContent));
        } else {
            builder.setContentText(shortContent);
        }

    }
}
