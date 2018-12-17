package com.kkh.safetaxi.worker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kkh.safetaxi.MapActivity;
import com.kkh.safetaxi.common.NetworkUtil;
import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.PlaceData;
import com.kkh.safetaxi.data.PlaceSearchData;

/**
 * Created by Administrator on 2018-08-15.
 */

public class PlaceWorker extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "[KKH]PlaceWorker";

    private Context mContext;
    private PlaceData mPlaceData;
    private ProgressDialog mProgressDialog;
    private PlaceSearchData mData;
    private MapActivity.IPlaceSearchCompleteListener mPlaceSearchCompleteListener;

    public PlaceWorker(Context context, PlaceData place) {
        mContext = context;
        mPlaceData = place;
        mProgressDialog = Util.getProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressDialog.dismiss();
        mPlaceSearchCompleteListener.onCompleted(mData,mPlaceData);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mProgressDialog.dismiss();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground");
        mData = NetworkUtil.requestPlaceSearch(mPlaceData.getmName().toString());
        return null;
    }

    public void setCompleteListener(MapActivity.IPlaceSearchCompleteListener placeSearchCompleteListener) {
        mPlaceSearchCompleteListener = placeSearchCompleteListener;
    }
}
