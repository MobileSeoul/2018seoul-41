package com.kkh.safetaxi;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.kkh.safetaxi.common.Constant;
import com.kkh.safetaxi.common.LocationSettingManager;
import com.kkh.safetaxi.worker.LocationWorker;

import static com.kkh.safetaxi.common.Constant.INTENT_LOCATION_CHANGE_EVENT;
import static com.kkh.safetaxi.common.Constant.LOCATION_TIMER_PERIOD;

public class LocationWorkService extends Service {

    private static final String TAG = "[KKH]LocationWorkService";
    private Context mContext;
    private LocationSettingManager mLocationSettingManager;
    private double[] mLocationInfo = {0, 0};

    private CountDownTimer mCountDownTimer;

    public interface ILocationEvent {
        void locationEventListener(Location location);
    }


    private ILocationEvent mLocationEvent = new ILocationEvent() {
        @SuppressLint("LongLogTag")
        @Override
        public void locationEventListener(Location location) {
            if (location != null) {
//                mLocationInfo[0] = location.getLatitude();
//                mLocationInfo[1] = location.getLongitude();
//                Log.d(TAG, "locationEventListener :" + mLocationInfo[0] + "," + mLocationInfo[1]);
//                updateCurrentLocation();
            }

        }
    };


    private void updateCurrentLocation() {
        Log.e(TAG, "updateCurrentLocation :" + mLocationInfo[0] + "," + mLocationInfo[1]);
        Intent intent = new Intent(INTENT_LOCATION_CHANGE_EVENT);
        intent.putExtra(Constant.PREF_LATITUDE, mLocationInfo[0]);
        intent.putExtra(Constant.PREF_LONGITUDE, mLocationInfo[1]);
        sendBroadcast(intent);
    }


    @SuppressLint("LongLogTag")
    public LocationWorkService() {
        Log.d(TAG, "LocationWorkService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.d(TAG, "onCreate");
        mLocationSettingManager = LocationSettingManager.getInstance(mContext);
        mLocationSettingManager.setLocationEventListener(mLocationEvent);
    }

    @Override
    @SuppressLint("LongLogTag")
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        mCountDownTimer = new CountDownTimer(30 * 60 * 1000, LOCATION_TIMER_PERIOD) {

            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick");
                //mLocationInfo = mLocationSettingManager.getLocationInformation();
                LocationWorker worker = new LocationWorker(mContext);
                worker.setHandler(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mLocationInfo = mLocationSettingManager.getLocationInformation();
                        updateCurrentLocation();
                    }
                }, 0);

                worker.execute();

            }

            public void onFinish() {
                Log.d(TAG, "onFinish");
                mCountDownTimer.start();
            }

        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mCountDownTimer.cancel();
        mLocationSettingManager.setLocationEventListener(null);
        mLocationSettingManager.unRegisterReceiver();
    }
}
