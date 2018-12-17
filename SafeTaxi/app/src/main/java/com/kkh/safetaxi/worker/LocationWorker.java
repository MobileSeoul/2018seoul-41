package com.kkh.safetaxi.worker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.kkh.safetaxi.common.LocationSettingManager;
import com.kkh.safetaxi.common.Pref;

import static com.kkh.safetaxi.common.Constant.PREF_LATITUDE;
import static com.kkh.safetaxi.common.Constant.PREF_LONGITUDE;

public class LocationWorker extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "[KKH]LocationWorker";
    private Context mContext;
    private double mLocation[];
    private Handler mHandler;
    private int mMsg;

    public LocationWorker(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        LocationSettingManager.getInstance(mContext).requestLocations();
        int count = 0;
        while (true) {
            count++;
            Log.d(TAG, "count : " + count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (count == 10) {
                break;
            }

            mLocation = LocationSettingManager.getInstance(mContext).getLocationInformation();
            if (mLocation[0] == -1 || mLocation[1] == -1) {
                continue;
            } else {
                break;
            }
        }

        Pref.setPreferenceString(PREF_LATITUDE, String.valueOf(mLocation[0]), mContext);
        Pref.setPreferenceString(PREF_LONGITUDE, String.valueOf(mLocation[0]), mContext);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mHandler.sendEmptyMessage(mMsg);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void setHandler(Handler handler, int msg) {
        mHandler = handler;
        mMsg = msg;
    }
}
