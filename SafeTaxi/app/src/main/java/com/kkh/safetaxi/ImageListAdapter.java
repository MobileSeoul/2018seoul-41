package com.kkh.safetaxi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kkh.safetaxi.data.ImageData;

import java.util.ArrayList;

import static com.kkh.safetaxi.ImageListActivity.EXTRA_IMAGE_DATA;

/**
 * Created by Administrator on 2018-09-19.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder> {
    private static final String TAG = "[KKH]ImageListAdapter";
    public static final int TYPE_NAME = 0;
    public static final int TYPE_DETAIL = 1;
    private int mType = -1;
    private Context mContext;
    private ArrayList<ImageData> mList = new ArrayList<>();

    public ImageListAdapter(Context context, ArrayList<ImageData> list, int type) {
        mContext = context;
        mList = list;
        mType = type;
    }

    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ImageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageListViewHolder holder, int position) {
        final ImageData data = mList.get(position);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (mType == TYPE_NAME) {
                    intent = new Intent(mContext, ImageListActivity.class);
                    intent.putExtra(EXTRA_IMAGE_DATA, data);
                    mContext.startActivity(intent);
                }

            }
        });
        holder.mCategoryView.setText(data.getmCategory());
        holder.mNameView.setText(data.getmNameEng()+"("+data.getmName()+")");
        Glide.with(mContext).load(data.getmUrlImage()).placeholder(R.drawable.loading).crossFade().override(1200, 800).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ImageListViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLayout;
        public TextView mCategoryView;
        public TextView mNameView;
        public ImageView mImageView;

        public ImageListViewHolder(View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.layout);
            mCategoryView = itemView.findViewById(R.id.category);
            mNameView = itemView.findViewById(R.id.name);
            mImageView = itemView.findViewById(R.id.image);
        }
    }
}
