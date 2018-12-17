package com.kkh.safetaxi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kkh.safetaxi.common.NetworkUtil;

import static com.kkh.safetaxi.common.Constant.NOTI_CHANNEL_ID;

public class TestActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mContext = this;
        Button notiBtn = findViewById(R.id.noti);
        notiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noti();
            }
        });

        Button notiDeleteBtn = findViewById(R.id.noti_delete);
        notiDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notiDelete();
            }
        });
        Button exchangeBtn= findViewById(R.id.exchange);
        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkUtil.getsInstance().requestExchangeData();
                    }
                }).start();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notiDelete() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.deleteNotificationChannel(NOTI_CHANNEL_ID);
    }

    private void noti() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notifyID = 1;
            //send
            Intent mMyIntent = new Intent(this, MapActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(
                    this, 1, mMyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(mContext)
                    .setContentTitle("New Message")
                    .setContentText("You've received new messages.")
                    .setSmallIcon(R.drawable.safe_taxi)
                    .setChannelId(NOTI_CHANNEL_ID)
                    .setContentIntent(mPendingIntent)
                    .build();
            mNotificationManager.notify(notifyID, notification);
        } else {
        }
    }
}
