package com.fazrilabs.neardeal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fazrilabs.neardeal.util.MyRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by blastocode on 4/7/16.
 */
public class GcmIntentService extends IntentService {
    private String TAG = "GcmIntentService";

    public  GcmIntentService() {
        super(GcmIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e(TAG, "GCM Registration Token: " + token);

            // sending the registration id to our server
            sendRegistrationToServer(token);

            int appVersionCode = BuildConfig.VERSION_CODE;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("APP_VERSION_CODE", appVersionCode);
            editor.putBoolean("GCM_TOKEN", true);
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean("GCM_TOKEN", false).apply();
        }
    }

    private void sendRegistrationToServer(final String token) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getDeviceId();

        Log.d("GcmIntentService", "Registering imei:" + imei);
        final String URL = "http://api.fazrilabs.com/neardeal/register_gcm.php";


        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d("GcmIntentService", "Register success");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("GcmIntentService", "error");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("imei", imei);
                params.put("gcm_id", token);

                return params;
            }
        };

        MyRequest.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
