package com.fazrilabs.neardeal;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.fazrilabs.neardeal.util.NotificationUtil;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by blastocode on 4/8/16.
 */
public class PushReceiver extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        int id = data.getInt("id");
        String name = data.getString("name");
        int discount = data.getInt("discount");

        String message = "New store:" + name + ", discount: " + discount + "%";

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("store_id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationUtil.showNotification(getApplicationContext(), pendingIntent, message);
    }
}
