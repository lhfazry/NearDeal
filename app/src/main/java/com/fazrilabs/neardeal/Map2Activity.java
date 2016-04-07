package com.fazrilabs.neardeal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fazrilabs.neardeal.cache.StoreCache;
import com.fazrilabs.neardeal.model.Store;
import com.fazrilabs.neardeal.util.MyRequest;
import com.fazrilabs.neardeal.util.PopupUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Map2Activity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        PopupUtil.showLoading(this, "Loading stores ...");
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationManager.removeUpdates(this);
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLatLng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15f));

        getStores(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 10, this);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationManager = (LocationManager) getApplicationContext()
                .getSystemService(LOCATION_SERVICE);

        Log.d("Map2Activity", "request location");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 10, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.add_store) {

        }
        else if(itemId == R.id.list_mode) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getStores(double lat, double lng) {
        Log.d("Map2Activity", "get store:" + lat + ", " + lng);

        String url = "http://api.fazrilabs.com/neardeal/get.php?lat=" + lat + "&lng=" + lng;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, "", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        PopupUtil.dismiss();
                        try {
                            JSONArray stores = response.getJSONArray("stores");

                            for(int i=0; i<stores.length(); i++) {
                                JSONObject storeJSON = stores.getJSONObject(i);
                                int id = storeJSON.getInt("id");
                                double lat = storeJSON.getDouble("lat");
                                double lng = storeJSON.getDouble("lng");
                                String name = storeJSON.getString("name");
                                String address = storeJSON.getString("address");
                                int discount = storeJSON.getInt("discount");

                                LatLng myLatLng = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions().position(myLatLng).title(name)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.store)));

                                Store store = new Store();
                                store.id = id;
                                store.name = name;
                                store.lat = lat;
                                store.lng = lng;
                                store.address = address;
                                store.discount = discount;
                                StoreCache.cache.add(store);
                            }

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
}
