package com.kkh.safetaxi.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import com.kkh.safetaxi.LocationWorkService;

import java.util.List;

/**
 * Created by Administrator on 2018-08-14.
 */

public class LocationSettingManager {

    private static final String TAG = "[KKH]LocationSettingManager";

    private final int MIN_DISTANCE_METER = 100;
    private final int MIN_TIME_PERIOD = 1000 * 10;
    private final int WAIT_LIMIT_TIME = 3000;

    public static LocationSettingManager sLocationSettingManager;
    private LocationManager mLocationManager;
    private static Context sContext;
    private double[] mLocationInfo = {0, 0};
    private boolean mIsOnLocationChanged = false;
    private LocationWorkService.ILocationEvent mLocationEvent;


    @SuppressLint({"MissingPermission", "LongLogTag"})
    public void requestLocations() {
        Log.d(TAG, "requestLocations");
        Thread thread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                unRegisterReceiver();
                searchLocation();
                Looper.loop();
                super.run();
            }
        };
        thread.start();
    }

    @SuppressLint({"MissingPermission", "LongLogTag"})
    private void searchLocation() {
        Log.d(TAG, "searchLocation");
        mLocationManager = (LocationManager) sContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        if (mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
         mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_PERIOD, MIN_DISTANCE_METER, mLocationListener, Looper.myLooper());
         mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_PERIOD, MIN_DISTANCE_METER, mLocationListener, Looper.myLooper());
    }

    public void setLocationEventListener(LocationWorkService.ILocationEvent event) {
        mLocationEvent = event;
    }


    public static LocationSettingManager getInstance(Context context) {
        if (sLocationSettingManager == null) {
            sLocationSettingManager = new LocationSettingManager();
        }
        sContext = context;
        return sLocationSettingManager;
    }


    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                Log.e(TAG, "onLocationChanged but location is null");
                return;
            }
            mLocationInfo[0] = location.getLatitude();
            mLocationInfo[1] = location.getLongitude();
            mIsOnLocationChanged = true;
            Log.d(TAG, "onLocationChanged lat:" + mLocationInfo[0] + ",lon:" + mLocationInfo[1]);
            if (mLocationEvent != null) {
                mLocationEvent.locationEventListener(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void unRegisterReceiver() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager = null;
        }
    }

    @SuppressLint("MissingPermission")
    public double[] getLocationInformation() {
        Log.e(TAG, "getLocationInformation");
        mLocationInfo[0] = -1;
        mLocationInfo[1] = -1;
        if (mLocationManager != null) {
            Location loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                mLocationInfo[0] = loc.getLatitude();
                mLocationInfo[1] = loc.getLongitude();
            } else {
                //retrySearchLocation();
            }
        } else {
            //retrySearchLocation();
        }

        Log.d(TAG, "getLocationInformation : " + mLocationInfo[0] + "," + mLocationInfo[1]);
        return mLocationInfo;
    }

    private void retrySearchLocation() {
        requestLocations();
        startTimer();
        mIsOnLocationChanged = false;
        try {
            while (!mIsOnLocationChanged) {
                if (mLocationManager == null) {
                    break;
                }
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    break;
                }
            }
        } catch (NullPointerException e) {
            return;
        }

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private CountDownTimer mCountDownTimer;

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(WAIT_LIMIT_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mIsOnLocationChanged = true;
            }
        };
        mCountDownTimer.start();
    }
}
