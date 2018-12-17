package com.kkh.safetaxi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.TaxiInfo;
import com.kkh.safetaxi.sms.MessageUtil;

public class ReportDialog extends Dialog {

    private static final String TAG = "[KKH]ReportDialog";

    private Context mContext;
    private TaxiInfo mTaxiInfo;

    private EditText mInputNameView;
    private ImageView mImageView;
    private Button mImageButton;
    private TextView mStartPlaceView;
    private TextView mDestinationPlaceView;
    private TextView mStartTimeView;
    private TextView mDestinationTimeView;
    private TextView mResponse;
    private EditText mInputDetailView;
    private Button mOkBtn;
    private Button mCancelBtn;
    private String mContents = "";
    private TaxiDriveHistoryActivity.IItemEvent mItemEvent;

    public ReportDialog(@NonNull Context context, TaxiInfo taxiInfo) {
        super(context);
        mContext = context;
        mTaxiInfo = taxiInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.dialog_layout_report);
        setCancelable(true);

        initViews();
        setData();

    }

    private void initViews() {
        mInputNameView = findViewById(R.id.input_name);
        mImageView = findViewById(R.id.image);
        mImageButton = findViewById(R.id.image_btn);
        mInputDetailView = findViewById(R.id.input_detail);
        mStartPlaceView = findViewById(R.id.start_place);
        mDestinationPlaceView = findViewById(R.id.destination_place);
        mStartTimeView = findViewById(R.id.start_time);
        mDestinationTimeView = findViewById(R.id.destination_time);
        mResponse = findViewById(R.id.report_response);
        mOkBtn = findViewById(R.id.ok);
        mCancelBtn = findViewById(R.id.cancel);
        mImageButton = findViewById(R.id.image_btn);
    }

    private void setData() {
        mImageView.setVisibility(View.VISIBLE);
        mImageView.setImageBitmap(Util.getBitmapFromFile(mContext, mTaxiInfo.getmImagePath()));
        mStartPlaceView.setText(mTaxiInfo.getmStartPlace());
        mDestinationPlaceView.setText(mTaxiInfo.getmDestinationPlace());
        mStartTimeView.setText(Util.getTimeFormatText2(Long.parseLong(mTaxiInfo.getmStartTime())));
        mDestinationTimeView.setText(Util.getTimeFormatText2(Long.parseLong(mTaxiInfo.getmEndTime())));
        mInputDetailView.setText(mTaxiInfo.getmReview());

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTaxiInfo!=null){
                }

                if (mInputNameView.getText().length() <= 0) {
                    Toast.makeText(mContext, mContext.getString(R.string.report_name_err), Toast.LENGTH_SHORT).show();
                } else if ("".equalsIgnoreCase(mTaxiInfo.getmImagePath()) || mTaxiInfo.getmImagePath() == "null") {
                    Toast.makeText(mContext, mContext.getString(R.string.report_image_err), Toast.LENGTH_SHORT).show();
                } else {
                    mContents += mContext.getString(R.string.report_title) + "\n";
                    mContents += "1)" + mInputNameView.getText().toString() + "\n";
                    mContents += "3)" + "From:"+mStartPlaceView.getText().toString() + "," + "To:"+mDestinationPlaceView.getText().toString() + ",Start:" + mStartTimeView.getText().toString() + ",End:" + mDestinationTimeView.getText().toString() + "\n";
                    mContents += "4)" + mInputDetailView.getText().toString() + "\n";
                    mContents += "5)" + mResponse.getText().toString();
                    Toast.makeText(mContext, mContext.getString(R.string.report_msg), Toast.LENGTH_SHORT).show();
                    MessageUtil.sendMessageWithImage(mContext, mTaxiInfo, mContents);
                    mItemEvent.onReported();
                    dismiss();
                }

            }
        });
    }

    public void setItemEvent(TaxiDriveHistoryActivity.IItemEvent itemEvent) {
        mItemEvent = itemEvent;
    }
}
