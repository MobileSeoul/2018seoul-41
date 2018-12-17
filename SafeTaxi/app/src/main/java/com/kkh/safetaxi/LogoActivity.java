package com.kkh.safetaxi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kkh.safetaxi.common.Util;

import static com.kkh.safetaxi.common.Constant.NOTI_CHANNEL_ID;
import static com.kkh.safetaxi.data.DatabaseManager.DB_NAME;

public class LogoActivity extends AppCompatActivity {

    private static final String TAG = "[KKH]LogoActivity";
    private Context mContext;

    private FrameLayout mFrame;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        Log.d(TAG, "onCreate");
        mContext = this;
        Util.initDatabase(mContext, DB_NAME);
        mFrame = findViewById(R.id.frame);
        mFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(mContext, MainActivity.class));
                return false;
            }
        });

        mImageView = findViewById(R.id.image);
        final Animation anim_in = AnimationUtils.loadAnimation(mContext, R.anim.logo_anim_fade_in);
        final Animation anim_out = AnimationUtils.loadAnimation(mContext, R.anim.logo_anim_fade_out);

        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageView.startAnimation(anim_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageView.startAnimation(anim_in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(anim_in);
    }
}
