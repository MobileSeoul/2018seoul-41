package com.kkh.safetaxi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.EmbassyData;

public class EmbassyDialog extends Dialog {

    private static final String TAG = "[KKH]EmbassyDialog";
    private Context mContext;
    private EmbassyData mEmbassyData;
    private ImageView mImageView;
    private TextView mName;
    private TextView mAddress;
    private TextView mPhone;

    public EmbassyDialog(@NonNull Context context, EmbassyData data) {
        super(context);
        mContext = context;
        mEmbassyData = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_embassy);

        mImageView = findViewById(R.id.image);
        mName = findViewById(R.id.name);
        mAddress = findViewById(R.id.address);
        mPhone = findViewById(R.id.phone);


        mImageView.setBackground(Util.getEmbassyImage(mContext, mEmbassyData.getmName()));
        mName.setText(mEmbassyData.getmName());
        mAddress.setText(mEmbassyData.getmAddress());
        mPhone.setText(mEmbassyData.getmPhone());
    }
}
