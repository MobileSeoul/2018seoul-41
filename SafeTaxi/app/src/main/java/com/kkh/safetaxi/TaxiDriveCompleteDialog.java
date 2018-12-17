package com.kkh.safetaxi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.TaxiInfo;

public class TaxiDriveCompleteDialog extends Dialog {

    private static final String TAG = "[KKH]TaxiDriveCompleteDialog";

    public static final int REQ_CODE_GALLERY = 0;
    public static final int TYPE_NONE = -1;
    public static final int TYPE_NEW = 0;
    public static final int TYPE_MODIFY = 1;

    private Context mContext;
    public TaxiInfo mTaxiInfo;
    private EditText mFeeView;
    private EditText mReviewView;
    private Button mGalleryBtn;
    private TextView mStart;
    private TextView mDestination;
    private TextView mStartTime;
    private TextView mDestinationTime;
    private RadioButton mScoreButton1;
    private RadioButton mScoreButton2;
    private RadioButton mScoreButton3;
    private RadioButton mScoreButton4;
    private RadioButton mScoreButton5;
    private Button mReportBtn;
    private Button mOkBtn;
    private ImageView mImage;
    private Activity mActivity;

    private int mType;

    public TaxiDriveCompleteDialog(Context context, TaxiInfo taxiInfo, int type) {
        super(context);
        mContext = context;
        mTaxiInfo = taxiInfo;
        mActivity = (Activity) mContext;
        mType = type;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_taxi_drvie_complete);
        Log.d(TAG, "onCreate");
        if (mType == TYPE_NEW) {
            setCancelable(false);
        } else {
            setCancelable(true);
        }

        initViews();
        setData();

    }

    @Override
    public void show() {
        super.show();
        Log.d(TAG, "show()");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d(TAG, "dismiss()");
    }

    private void initViews() {
        mFeeView = findViewById(R.id.fee);
        mGalleryBtn = findViewById(R.id.number);
        mStart = findViewById(R.id.start);
        mDestination = findViewById(R.id.destination);
        mStartTime = findViewById(R.id.start_time);
        mDestinationTime = findViewById(R.id.destination_time);
        mScoreButton1 = findViewById(R.id.score_1);
        mScoreButton2 = findViewById(R.id.score_2);
        mScoreButton3 = findViewById(R.id.score_3);
        mScoreButton4 = findViewById(R.id.score_4);
        mScoreButton5 = findViewById(R.id.score_5);
        mReviewView = findViewById(R.id.review);
        mReportBtn = findViewById(R.id.report);
        mOkBtn = findViewById(R.id.ok);
        mImage = findViewById(R.id.image);

        mScoreButton1.setChecked(true);
        mScoreButton1.setOnClickListener(mScoreBtnClickListener);
        mScoreButton2.setOnClickListener(mScoreBtnClickListener);
        mScoreButton3.setOnClickListener(mScoreBtnClickListener);
        mScoreButton4.setOnClickListener(mScoreBtnClickListener);
        mScoreButton5.setOnClickListener(mScoreBtnClickListener);
    }

    private void setData() {
        mStart.setText(String.format(mContext.getString(R.string.label_from), mTaxiInfo.getmStartPlace()));
        mDestination.setText(String.format(mContext.getString(R.string.label_to), mTaxiInfo.getmDestinationPlace()));
        mStartTime.setText(String.format(mContext.getString(R.string.label_start_time), Util.getTimeFormatText2(Long.parseLong(mTaxiInfo.getmStartTime()))));
        mDestinationTime.setText(String.format(mContext.getString(R.string.label_end_time), Util.getTimeFormatText2(Long.parseLong(mTaxiInfo.getmEndTime()))));
        mReportBtn.setVisibility(View.GONE);
        mReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTaxiInfo();
            }
        });

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTaxiInfo();
                dismiss();
            }
        });
        mGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mActivity.startActivityForResult(intent, REQ_CODE_GALLERY);
            }
        });

        if (mType == TYPE_MODIFY) {
            mFeeView.setText(String.valueOf(mTaxiInfo.getmFee()));
            int score = mTaxiInfo.getmReviewScore();
            if (score == 1) {
                mScoreButton1.setChecked(true);
            } else if (score == 2) {
                mScoreButton2.setChecked(true);
            } else if (score == 3) {
                mScoreButton3.setChecked(true);
            } else if (score == 4) {
                mScoreButton4.setChecked(true);
            } else if (score == 5) {
                mScoreButton5.setChecked(true);
            }

            mReviewView.setText(mTaxiInfo.getmReview());
            mImage.setVisibility(View.VISIBLE);
            mImage.setImageBitmap(Util.getBitmapFromFile(mContext, mTaxiInfo.getmImagePath()));
        }
    }

    @SuppressLint("LongLogTag")
    public void setImage(Bitmap bitmap) {
        Log.d(TAG, "setImage");
        if (mImage != null) {
            mImage.setVisibility(View.VISIBLE);
            mImage.setImageBitmap(bitmap);
        }
    }


    @SuppressLint("LongLogTag")
    private void saveTaxiInfo() {

        String review = mReviewView.getText().toString();
        mTaxiInfo.setmReview(review);
        String feeStr = mFeeView.getText().toString();
        int fee = 0;
        try {
            fee = Integer.parseInt(feeStr);
        } catch (NumberFormatException e) {
            fee = 0;
        }
        mTaxiInfo.setmFee(fee);

        Log.e(TAG, "saveTaxiInfo mTaxiInfo : " + mTaxiInfo.toString());
        if (mType == TYPE_NEW) {
            DatabaseManager.getInstance(mContext).insertTaxiData(mTaxiInfo);
        } else {
            DatabaseManager.getInstance(mContext).updateTaxiData(mTaxiInfo);
        }

        if (mIItemEvent != null) {
            mIItemEvent.onModified();
        }
    }

    private View.OnClickListener mScoreBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int score = 0;
            switch (view.getId()) {
                case R.id.score_1:
                    score = 1;
                    break;
                case R.id.score_2:
                    score = 2;
                    break;
                case R.id.score_3:
                    score = 3;
                    break;
                case R.id.score_4:
                    score = 4;
                    break;
                case R.id.score_5:
                    score = 5;
                    break;
            }
            mTaxiInfo.setmReviewScore(score);
        }
    };

    public void setImagePath(String path) {
        mTaxiInfo.setmImagePath(path);
    }

    TaxiDriveHistoryActivity.IItemEvent mIItemEvent;

    public void setItemEvent(TaxiDriveHistoryActivity.IItemEvent itemEvent) {
        mIItemEvent = itemEvent;
    }
}
