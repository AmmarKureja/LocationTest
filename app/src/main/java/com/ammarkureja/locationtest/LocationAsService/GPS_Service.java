package com.ammarkureja.locationtest.LocationAsService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ammar on 8/10/2017.
 */

public class GPS_Service extends Service {

    LocationListener mLocationListener;
    LocationManager mLocationManager;
    String LOCATION_UPDATE = "locationUpdate";
    String COORDINATES = "coordinates";
    private static final String TAG = "GPS_Service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: starts");
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Intent intent = new Intent(LOCATION_UPDATE);
                intent.putExtra(COORDINATES, location.getLongitude() + " " +location.getLatitude());
                sendBroadcast(intent);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, mLocationListener);
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onDestroy() {
    super.onDestroy();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }



}
