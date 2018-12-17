package com.kkh.safetaxi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static com.kkh.safetaxi.EmbassyListActivity.EMBASSY_AREA;

public class HelpActivity extends AppCompatActivity {

    private static final String TAG = "[KKH]HelpActivity";
    private Context mContext;

    private Button mEmbassyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Log.d(TAG, "onCreate");
        mContext = this;

        initViews();

    }

    private void initViews() {
        mEmbassyBtn = findViewById(R.id.embassy);
        mEmbassyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectArea();
                Intent intent = new Intent(mContext, EmbassyListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void selectArea() {
        final String items[] = new String[]{"Asia", "Oceania", "Europe", "America", "Africa"};
        final int[] index = {0};
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle(mContext.getString(R.string.dialog_msg_location));
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        index[0] = i;
                    }
                }).setPositiveButton(mContext.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(mContext, EmbassyListActivity.class);
                        intent.putExtra(EMBASSY_AREA, items[index[0]]);
                        startActivity(intent);
                    }
                });
        ab.setCancelable(true);
        ab.show();
    }
}
