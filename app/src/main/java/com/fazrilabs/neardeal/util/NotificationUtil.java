package com.fazrilabs.neardeal.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.fazrilabs.neardeal.Map2Activity;
import com.fazrilabs.neardeal.R;

/**
 * Created by blastocode on 4/7/16.
 */
public class NotificationUtil {
    public static void showNotification(Context context, PendingIntent pendingIntent, String message) {
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_map)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }
}
