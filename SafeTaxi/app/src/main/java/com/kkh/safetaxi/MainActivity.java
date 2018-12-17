package com.kkh.safetaxi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.DatabaseManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "[KKH]LogoActivity";
    public static final String EXTRA_CODE = "extra_code";

    public static final String[] PERMISSION_ARR = {
            //Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.BROADCAST_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.CAMERA,
            //Manifest.permission.READ_PHONE_STATE,

    };

    private Context mContext;

    private Button mMenuMap;
    private Button mMenuHelp;
    private Button mMenuCamera;
    private Button mMenuDriveHistory;
    private Button mMenuExchange;
    private TextView mVersion;
    private ImageView mTest;

    private String mCurrentLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mContext = this;

        permissionCheck();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.checkNetworkStatus(mContext);

    }

    private void permissionCheck() {
        ActivityCompat.requestPermissions(this,
                PERMISSION_ARR, 0);
    }

    private void initViews() {
        mMenuMap = findViewById(R.id.menu_map);
        mMenuHelp = findViewById(R.id.menu_help);
        mMenuCamera = findViewById(R.id.menu_camera);
        mMenuDriveHistory = findViewById(R.id.menu_drive_history);
        mMenuExchange = findViewById(R.id.menu_exchange);
        mVersion = findViewById(R.id.version);
        mTest = findViewById(R.id.test);
        mVersion.setText("ver:" + Util.getAppVersion(mContext));
        mMenuMap.setOnClickListener(mOnClickListener);
        mMenuHelp.setOnClickListener(mOnClickListener);
        mMenuCamera.setOnClickListener(mOnClickListener);
        mMenuDriveHistory.setOnClickListener(mOnClickListener);
        mMenuExchange.setOnClickListener(mOnClickListener);
        mTest.setOnClickListener(mOnClickListener);

        mMenuCamera.setVisibility(View.GONE);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.menu_map:
                    Intent intent = new Intent(mContext, MapActivity.class);
                    String code = DatabaseManager.getInstance(mContext).getLocationKorDataByCode(mCurrentLocationName);
                    intent.putExtra(EXTRA_CODE, code);
                    startActivity(intent);
                    break;
                case R.id.menu_help:
                    startActivity(new Intent(mContext, HelpActivity.class));
                    break;
                case R.id.menu_drive_history:
                    startActivity(new Intent(mContext, TaxiDriveHistoryActivity.class));
                    break;
                case R.id.menu_camera:
                    startActivity(new Intent(mContext, CameraActivity.class));
                    break;
                case R.id.menu_exchange:
                    startActivity(new Intent(mContext, ExchangeActivity.class));
                    break;
                case R.id.test:
                    startActivity(new Intent(mContext, TestActivity.class));
                    break;
            }
        }
    };

}
