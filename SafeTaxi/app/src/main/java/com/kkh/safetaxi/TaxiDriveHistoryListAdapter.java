package com.kkh.safetaxi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.TaxiInfo;

import java.util.ArrayList;

import static com.kkh.safetaxi.common.Constant.EXTRA_TAXI_INFO;

public class TaxiDriveHistoryListAdapter extends RecyclerView.Adapter<TaxiDriveHistoryListAdapter.TaxiDriveHistoryViewHolder> {

    private static final String TAG = "[KKH]TaxiDriveHistoryListAdapter";

    private Context mContext;
    private ArrayList<TaxiInfo> mList;
    private TaxiDriveHistoryActivity.IItemEvent mIItemEvent;
    private TaxiDriveCompleteDialog mTaxiDriveCompleteDialog;

    public TaxiDriveHistoryListAdapter(Context context, ArrayList<TaxiInfo> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public TaxiDriveHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taxi_drive_history_item, parent, false);
        return new TaxiDriveHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaxiDriveHistoryViewHolder holder, final int position) {
        final TaxiInfo data = mList.get(position);
        Log.e(TAG, "data : " + data.toString());
        holder.mImageView.setImageBitmap(Util.getBitmapFromFile(mContext, data.getmImagePath()));
        holder.mStartPlace.setText(String.format(mContext.getString(R.string.label_from), data.getmStartPlace()));
        holder.mStartTime.setText(String.format(mContext.getString(R.string.start_time), Util.getTimeFormatText2(Long.parseLong(data.getmStartTime()))));
        holder.mDestinationPlace.setText(String.format(mContext.getString(R.string.label_to), data.getmDestinationPlace()));
        holder.mDestinationTime.setText(String.format(mContext.getString(R.string.end_time), Util.getTimeFormatText2(Long.parseLong(data.getmEndTime()))));

        holder.mDistance.setText(String.format(mContext.getString(R.string.distance), String.format("%.2f", data.getmDistance())));
        holder.mTravelTime.setText(String.format(mContext.getString(R.string.travel_time), String.valueOf(Util.mliToMinute(data.getmTravelTime()))));
        holder.mFee.setText(String.format(mContext.getString(R.string.fee), String.valueOf(data.getmFee())));
        holder.mReviewScore.setText(String.format(mContext.getString(R.string.review_score), String.valueOf(data.getmReviewScore())));
        holder.mReview.setText(String.format(mContext.getString(R.string.review), String.valueOf(data.getmReview())));
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog(data);
            }
        });
    }

    private void showSelectDialog(final TaxiInfo data) {
        final int[] index = {0};
        final String items[] = {
                mContext.getString(R.string.dialog_msg_history_view),
                mContext.getString(R.string.dialog_msg_history_report),
                mContext.getString(R.string.dialog_msg_history_modify),
                mContext.getString(R.string.dialog_msg_history_delete)};
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle(mContext.getString(R.string.dialog_msg_history));
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        index[0] = i;
                    }
                }).setPositiveButton(mContext.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (index[0] == 0) {
                            Intent intent = new Intent(mContext, HistoryMapActivity.class);
                            intent.putExtra(EXTRA_TAXI_INFO, data);
                            mContext.startActivity(intent);
                        } else if (index[0] == 1) {
                            if ("".equalsIgnoreCase(data.getmImagePath()) || data.getmImagePath().contains("null")) {
                                Toast.makeText(mContext, mContext.getString(R.string.report_image_err), Toast.LENGTH_SHORT).show();
                            } else {
                                showReportDialog(data);
                            }
                        } else if (index[0] == 2) {
                            showEditDialog(data);
                        } else if (index[0] == 3) {
                            showConfirmDialog(data);
                        }

                    }
                }).setNegativeButton(mContext.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        ab.setCancelable(true);
        ab.show();
    }

    private void showReportDialog(TaxiInfo data) {
        ReportDialog reportDialog = new ReportDialog(mContext, data);
        reportDialog.setItemEvent(mIItemEvent);
        reportDialog.show();
    }

    private void showEditDialog(TaxiInfo data) {
        mTaxiDriveCompleteDialog = new TaxiDriveCompleteDialog(mContext, data, TaxiDriveCompleteDialog.TYPE_MODIFY);
        mTaxiDriveCompleteDialog.setItemEvent(mIItemEvent);
        mTaxiDriveCompleteDialog.show();
    }

    private void showConfirmDialog(final TaxiInfo data) {
        AlertDialog.Builder confirm_ab = new AlertDialog.Builder(mContext);
        confirm_ab.setTitle(mContext.getString(R.string.dialog_msg_history_delete_confirm));
        confirm_ab.setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(mContext.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DatabaseManager.getInstance(mContext).deleteTaxiInfo(data);
                        mIItemEvent.onRemoved();
                    }
                }).setNegativeButton(mContext.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        confirm_ab.setCancelable(true);
        confirm_ab.show();

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setItemEvent(TaxiDriveHistoryActivity.IItemEvent itemEvent) {
        mIItemEvent = itemEvent;
    }

    public void setDialogBitmap(Bitmap bitmap) {
        mTaxiDriveCompleteDialog.setImage(bitmap);
    }

    public void setImagePath(String path) {
        mTaxiDriveCompleteDialog.setImagePath(path);
    }

    public class TaxiDriveHistoryViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mLayout;
        public ImageView mImageView;
        public TextView mStartPlace;
        public TextView mStartTime;
        public TextView mDestinationPlace;
        public TextView mDestinationTime;
        public TextView mDistance;
        public TextView mTravelTime;
        public TextView mFee;
        public TextView mReviewScore;
        public TextView mReview;

        public TaxiDriveHistoryViewHolder(View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.layout);
            mImageView = itemView.findViewById(R.id.image);
            mStartPlace = itemView.findViewById(R.id.start_place);
            mStartTime = itemView.findViewById(R.id.start_time);
            mDestinationPlace = itemView.findViewById(R.id.destination_place);
            mDestinationTime = itemView.findViewById(R.id.destination_time);
            mDistance = itemView.findViewById(R.id.distance);
            mTravelTime = itemView.findViewById(R.id.travel_time);
            mFee = itemView.findViewById(R.id.fee);
            mReviewScore = itemView.findViewById(R.id.review_score);
            mReview = itemView.findViewById(R.id.review);
        }
    }
}
