package com.fazrilabs.neardeal;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by blastocode on 4/8/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register Gcm
        int currectAppVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int appVersionCode = sharedPreferences.getInt("APP_VERSION_CODE", 1);
        boolean gcmTokenExist = sharedPreferences.getBoolean("GCM_TOKEN", false);

        if(!gcmTokenExist || appVersionCode < currectAppVersionCode) {
            Intent intent = new Intent(this, GcmIntentService.class);
            startService(intent);
        }
    }
}
