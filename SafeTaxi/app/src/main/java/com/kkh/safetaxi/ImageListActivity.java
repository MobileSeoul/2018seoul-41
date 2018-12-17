package com.kkh.safetaxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.ImageData;

import java.util.ArrayList;

import static com.kkh.safetaxi.ImageListAdapter.TYPE_DETAIL;
import static com.kkh.safetaxi.ImageListAdapter.TYPE_NAME;

public class ImageListActivity extends AppCompatActivity {

    private static final String TAG = "[KKH]ImageListActivity";
    public static final String EXTRA_IMAGE_DATA = "extra_image_data";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private Button mViewMapBtn;
    private TextView mTitle;
    private ImageListAdapter mAdapter;
    private ArrayList<ImageData> mList = new ArrayList<>();
    private ImageData mImageData;
    private int mType = TYPE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        mContext = this;
        Intent intent = getIntent();
        mImageData = intent.getParcelableExtra(EXTRA_IMAGE_DATA);

        if (mImageData != null) {
            mList = DatabaseManager.getInstance(mContext).getImageDataList(mImageData);
            mType = TYPE_DETAIL;
        } else {
            mList = DatabaseManager.getInstance(mContext).getImageDataListByName();

        }

        //mList = DatabaseManager.getInstance(mContext).getImageDataList();
        initViews();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.list);
        mTitle = findViewById(R.id.title);
        mViewMapBtn = findViewById(R.id.view_map);
        mAdapter = new ImageListAdapter(mContext, mList, mType);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        if (mType == TYPE_DETAIL) {
            mTitle.setVisibility(View.GONE);
            mViewMapBtn.setVisibility(View.VISIBLE);
            mViewMapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MapActivity.class);
                    intent.putExtra(EXTRA_IMAGE_DATA, mImageData);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mViewMapBtn.setVisibility(View.GONE);
        }


    }
}
