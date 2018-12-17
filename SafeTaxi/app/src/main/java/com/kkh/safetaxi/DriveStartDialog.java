package com.kkh.safetaxi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kkh.safetaxi.common.Util;

public class DriveStartDialog extends Dialog {

    private static final String TAG = "[KKH]DriveStartDialog";

    private Context mContext;
    private Activity mActivity;

    private TextView mFromView;
    private TextView mToView;
    private TextView mTimeView;
    private TextView mMessageView;
    private Button mOkBtn;
    private Button mCancelBtn;

    private String mFrom;
    private String mTo;
    private long mTime;
    private MapActivity.IDriveEvent mIDriveEvent;

    public DriveStartDialog(Context context, String from, String to, long time, MapActivity.IDriveEvent iiDriveEvent) {
        super(context);
        mContext = context;
        mActivity = (Activity) mContext;
        mFrom = from;
        mTo = to;
        mTime = time;
        mIDriveEvent = iiDriveEvent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_drvie_start);
        setCancelable(false);
        mFromView = findViewById(R.id.from);
        mToView = findViewById(R.id.to);
        mTimeView = findViewById(R.id.time);
        mOkBtn = findViewById(R.id.ok);
        mCancelBtn = findViewById(R.id.cancel);
        mMessageView = findViewById(R.id.message);

        mFromView.setText(String.format(mContext.getString(R.string.label_from), mFrom));
        mToView.setText(String.format(mContext.getString(R.string.label_to), mTo));
        mTimeView.setText(String.format(mContext.getString(R.string.label_start_time), Util.getTimeFormatText2(mTime)));
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIDriveEvent.onClickStartEvent(true);
                dismiss();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIDriveEvent.onClickStartEvent(false);
                dismiss();
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(mContext.getString(R.string.message_drive_alert));
        sb.append("\n\n");
        sb.append(mContext.getString(R.string.message_drive_alert_en));
        mMessageView.setText(sb.toString());

    }


}
