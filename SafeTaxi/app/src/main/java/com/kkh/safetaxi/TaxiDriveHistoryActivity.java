package com.kkh.safetaxi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.TaxiInfo;

import java.io.IOException;
import java.util.ArrayList;

import static com.kkh.safetaxi.TaxiDriveCompleteDialog.REQ_CODE_GALLERY;

public class TaxiDriveHistoryActivity extends AppCompatActivity {

    private static final String TAG = "[KKH]TaxiDriveHistoryActivity";
    private Context mContext;

    private RecyclerView mRecyclerView;
    private TaxiDriveHistoryListAdapter mAdapter;
    private ArrayList<TaxiInfo> mList = new ArrayList<>();

    private TaxiDriveCompleteDialog mTaxiDriveCompleteDialog;


    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_drive_history);
        Log.d(TAG, "onCreate");
        mContext = this;
        initViews();
        refreshList();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.list);
    }

    @SuppressLint("LongLogTag")
    private void refreshList() {
        Log.d(TAG, "refreshList");
        mList = DatabaseManager.getInstance(mContext).getTaxiDataList();
        mAdapter = new TaxiDriveHistoryListAdapter(mContext, mList);
        mAdapter.setItemEvent(mIItemEvent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    interface IItemEvent {
        void onRemoved();

        void onModified();

        void onReported();
    }

    IItemEvent mIItemEvent = new IItemEvent() {
        @Override
        public void onRemoved() {
            Log.d(TAG, "onRemoved");
            refreshList();
        }

        @Override
        public void onModified() {
            Log.d(TAG, "onModified");
            refreshList();
        }

        @Override
        public void onReported(){
            Log.d(TAG, "onReported");
            refreshList();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        if (requestCode == REQ_CODE_GALLERY) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            if (uri != null) {
                String path = Util.getPath(mContext, uri);
                if (mAdapter != null) {
                    mAdapter.setImagePath(path);
                }
                Log.e(TAG, "selected image path : " + path);
            }
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mAdapter != null && bitmap != null) {
                mAdapter.setDialogBitmap(bitmap);
            }
        }
    }
}
