package com.kkh.safetaxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.EmbassyData;

import java.util.ArrayList;

public class EmbassyListActivity extends AppCompatActivity {

    private static final String TAG = "[KKH}EmbassyListActivity";
    public static final String EMBASSY_AREA = "embassy_area";

    private Context mContext;
    private GridView mGridView;
    private String mArea;

    private ArrayList<EmbassyData> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embassy_list);
        mContext = this;
        Intent intent = getIntent();
        mArea = intent.getStringExtra(EMBASSY_AREA);

        //TODO
        mList = DatabaseManager.getInstance(mContext).getEmbassyDataList("");
        initViews();

    }

    private void initViews() {
        mGridView = findViewById(R.id.grid);
        GridAdapter gridAdapter = new GridAdapter(mContext, mList);
        mGridView.setAdapter(gridAdapter);
    }
}
