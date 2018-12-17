package com.kkh.safetaxi.worker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.kkh.safetaxi.common.NetworkUtil;
import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.DataPref;
import com.kkh.safetaxi.data.ExchangeData;

import java.util.ArrayList;

public class ExchangeWorker extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "[KKH]ExchangeWorker";
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private Handler mHandler;
    private ArrayList<ExchangeData> mList = new ArrayList<>();

    public ExchangeWorker(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = Util.getProgressDialog(mContext);
        mProgressDialog.show();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        mList = NetworkUtil.requestExchangeData();
        Log.d(TAG, "doInBackground mList : " + mList.size());
        DataPref.sExchangeDataList = mList;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressDialog.dismiss();
        mHandler.sendEmptyMessage(0);
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }
}
