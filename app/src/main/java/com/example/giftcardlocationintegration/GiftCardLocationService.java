package com.example.giftcardlocationintegration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

public class GiftCardLocationService extends Service {
    private static final String TAG = "GiftCardLocationService";
    public static String CHANNEL_ID = "giftcardlocation";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    public static boolean useLocationServices = true;
    private String prevString = "";
    private List<String> nearbyLocations = new ArrayList<>();
    private int lastNearbyLocations = nearbyLocations.size();
    public static List<String> currentUserGiftCards = new ArrayList<>();
    public static String USE_LOCATION_SERVICES = "uselocationservices";
    public static final String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    public static final String endUrl = "&radius=1500&type=restaurant&key=AIzaSyDhsoZx1j9eqOm5SmRbSOfnUi6ZgKGPFV8";
    public static final String testUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.1020,-88.2272&radius=1500&type=restaurant&key=AIzaSyD_bEHcl4NAJzxYtsGChrGS9dNRNYAdfqo";


    private String createUrlBasedOnLocation(double lat, double lon) {
        return  baseUrl + lat + "," + lon + endUrl;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intent.getBooleanExtra(LOCATION_SERVICE, false);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        startForeground(1, buildNotification("No nearby stores...=("));


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.i(TAG, "Current coordinates:" + location.getLongitude() + "," + location.getLongitude());
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });




        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.i(TAG, "Updated coordinates:" + location.getLatitude() + "," + location.getLongitude());
                    getData(location.getLatitude(), location.getLongitude());
                }
            }
        };

        createLocationRequest();
        startLocationUpdates();


    }

    private Notification buildNotification(String name) {




        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra(USE_LOCATION_SERVICES, useLocationServices);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (name.length() == 0) {
            name = "No nearby stores...=(";
        }


        Notification builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal_background)
                .setContentTitle("GiftCard Location Tracker")
                .setContentText(name)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();

        return builder;
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public void getData(double lat, double lon) {
        if (useLocationServices == false) {
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, createUrlBasedOnLocation(lat, lon), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //nearbyLocations.clear();
                            JSONArray object = response.getJSONArray("results");
                            String storeName;
                            nearbyLocations.clear();
                            for (int i = 0; i < object.length(); i++) {
                                JSONObject layer = object.getJSONObject(i);
                                storeName = layer.getString("name");
                                Log.i(TAG, storeName);
                                if (!nearbyLocations.contains(storeName) && currentUserGiftCards.contains(storeName)) {
                                    Log.i(TAG, "You have a gift card for " + storeName + "!");
                                    nearbyLocations.add(storeName);
                                }
                            }
                            updateNotification(nearbyLocations);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "error occured with volley");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);
    }

    private void updateNotification(List<String> nearbyStores) {
        Log.i(TAG, "updateNotification called");


        String result = "";
         for (int i = 0; i < nearbyStores.size(); i++) {
            result += "You are near a " + nearbyStores.get(i) + "!\n";
        }
         if (result.equals(prevString)) {
             return;
         }
         prevString = "";
         for (int i = 0; i < nearbyStores.size(); i++) {
             prevString += "You are near a " + nearbyStores.get(i) + "!\n";
         }

        Notification notification = buildNotification(result);

         if (notification == null) {
             return;
         }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
    }


    }

