package com.fazrilabs.neardeal;

import android.graphics.Bitmap;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fazrilabs.neardeal.cache.StoreCache;
import com.fazrilabs.neardeal.model.Store;
import com.fazrilabs.neardeal.util.BitmapUtil;
import com.fazrilabs.neardeal.util.MyRequest;
import com.fazrilabs.neardeal.util.PopupUtil;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView nameTextView;
    TextView discountTextView;
    TextView openHourTextView;
    TextView telpTextView;
    TextView addressTextView;
    TextView promotionEndTextView;
    TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int storeId = getIntent().getIntExtra("store_id", 0);

        imageView = (ImageView) findViewById(R.id.imageView);
        nameTextView = (TextView) findViewById(R.id.name);
        discountTextView = (TextView) findViewById(R.id.discount);
        openHourTextView = (TextView) findViewById(R.id.openHour);
        telpTextView = (TextView) findViewById(R.id.telp);
        addressTextView = (TextView) findViewById(R.id.address);
        promotionEndTextView = (TextView) findViewById(R.id.promotionEnd);
        descriptionTextView = (TextView) findViewById(R.id.description);

        getStoreDetail(storeId);
    }

    private void getStoreDetail(int storeId) {
        Log.d("DetailActivity", "get detail:" + storeId);
        PopupUtil.showLoading(this, "Loading store ...");

        String url = "http://api.fazrilabs.com/neardeal/get_detail.php?id=" + storeId;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, "", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        PopupUtil.dismiss();

                        try {
                            JSONObject storeJSON = response.getJSONObject("store");

                            String photo = storeJSON.getString("photo");

                            if(photo != null && !photo.isEmpty()) {
                                Bitmap bitmap = BitmapUtil.decode(photo);
                                imageView.setImageBitmap(bitmap);
                            }

                            String promotionEnd = storeJSON.getString("promotion_end");
                            promotionEndTextView.setVisibility(ImageView.GONE);

                            if(promotionEnd != null && !promotionEnd.isEmpty()) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date now = new Date();

                                try {
                                    Date promotionEndDate = simpleDateFormat.parse(promotionEnd);
                                    int restDay = (int) ((promotionEndDate.getTime()-now.getTime())/(24*60*60*1000));
                                    promotionEndTextView.setText("Berakhir " + restDay + " hari lagi");
                                    promotionEndTextView.setVisibility(View.VISIBLE);
                                }
                                catch (Exception e) {
                                    Log.e("DetailActivity", "error parsing date string");
                                }
                            }

                            nameTextView.setText(storeJSON.getString("name"));
                            discountTextView.setText("Diskon " + storeJSON.getInt("discount") + "%");
                            openHourTextView.setText(storeJSON.getString("open_hour"));
                            telpTextView.setText(storeJSON.getString("telp"));
                            addressTextView.setText(storeJSON.getString("address"));
                            descriptionTextView.setText(storeJSON.getString("description"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PopupUtil.dismiss();
                        error.printStackTrace();
                    }
                });

        
        MyRequest.getInstance(this).addToRequestQueue(jsonRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
