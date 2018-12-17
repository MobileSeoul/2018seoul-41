package com.kkh.safetaxi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MapGuideDialog extends Dialog {
    private Context mContext;

    public MapGuideDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    private TextView mMessageView;
    private Button mOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_map_guide);
        mMessageView = findViewById(R.id.msg);
        mMessageView.setText(mContext.getString(R.string.dialog_msg_guide));

        mOkBtn = findViewById(R.id.ok);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
