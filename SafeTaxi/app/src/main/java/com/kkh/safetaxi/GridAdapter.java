package com.kkh.safetaxi;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.EmbassyData;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EmbassyData> mList = new ArrayList<>();
    private LayoutInflater mInflater;

    public GridAdapter(Context context, ArrayList<EmbassyData> list) {
        mContext = context;
        mList = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EmbassyData data = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_layout_item, null);
            CardView layout = convertView.findViewById(R.id.layout);
            ImageView image = convertView.findViewById(R.id.image);
            TextView name = convertView.findViewById(R.id.name);

            image.setBackground(Util.getEmbassyImage(mContext, data.getmName()));
            name.setText(data.getmName());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmbassyDialog dialog = new EmbassyDialog(mContext,data);
                    dialog.show();
                }
            });

        }
        return convertView;
    }
}
