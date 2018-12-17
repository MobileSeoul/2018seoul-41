package com.kkh.safetaxi.worker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.kkh.safetaxi.data.TaxiInfo;

public class TaxiInfoWorker extends AsyncTask<Void,Void,Void> {

    private Context mContext;
    private TaxiInfo mTaxiInfo;
    private Handler mHandler;

    public TaxiInfoWorker(Context context, TaxiInfo taxiInfo){
        mContext = context;
        mTaxiInfo = taxiInfo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    public void setFinishHandler(Handler handler){
        mHandler = handler;
    }
}
