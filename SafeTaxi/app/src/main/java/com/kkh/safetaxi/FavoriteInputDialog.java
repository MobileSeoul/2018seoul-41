package com.kkh.safetaxi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.FavoriteLocationData;

public class FavoriteInputDialog extends Dialog {

    private static final String TAG = "[KKH]TaxiDriveCompleteDialog";

    public static final int REQ_CODE_GALLERY = 0;
    private Context mContext;
    private Activity mActivity;
    private FavoriteLocationData mFavoriteLocationData;

    private TextView mNameView;
    private ImageView mImageView;
    private TextView mAddressView;
    private EditText mMemoView;
    private Button mOkBtn;


    public FavoriteInputDialog(Context context, FavoriteLocationData info) {
        super(context);
        mContext = context;
        mActivity = (Activity) mContext;
        mFavoriteLocationData = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_favorite_input);

        setCancelable(false);

        mNameView = findViewById(R.id.name);
        mImageView = findViewById(R.id.image);
        mMemoView = findViewById(R.id.memo);
        mAddressView = findViewById(R.id.address);
        mOkBtn = findViewById(R.id.ok);
        mNameView.setText(mFavoriteLocationData.getmName());
        Glide.with(mContext).load(mFavoriteLocationData.getmImagePath()).into(mImageView);
        mAddressView.setText(mFavoriteLocationData.getmAddress());

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFavoriteLocationData.setmMemo(mMemoView.getText().toString());
                DatabaseManager.getInstance(mContext).insertFavoriteLocation(mFavoriteLocationData);
                dismiss();
            }
        });
    }
}
