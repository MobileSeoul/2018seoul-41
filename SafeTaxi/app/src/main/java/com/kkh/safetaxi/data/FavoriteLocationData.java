package com.kkh.safetaxi.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteLocationData implements Parcelable {

    private String mName;
    private String mMemo;
    private String mImagePath;
    private String mLatitude;
    private String mLongitude;
    private String mAddress;
    private Drawable mDrawable;

    public FavoriteLocationData() {

    }

    protected FavoriteLocationData(Parcel in) {
        mName = in.readString();
        mMemo = in.readString();
        mImagePath = in.readString();
        mLatitude = in.readString();
        mLongitude = in.readString();
        mAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mMemo);
        dest.writeString(mImagePath);
        dest.writeString(mLatitude);
        dest.writeString(mLongitude);
        dest.writeString(mAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavoriteLocationData> CREATOR = new Creator<FavoriteLocationData>() {
        @Override
        public FavoriteLocationData createFromParcel(Parcel in) {
            return new FavoriteLocationData(in);
        }

        @Override
        public FavoriteLocationData[] newArray(int size) {
            return new FavoriteLocationData[size];
        }
    };

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmMemo() {
        return mMemo;
    }

    public void setmMemo(String mMemo) {
        this.mMemo = mMemo;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    @Override
    public String toString() {
        return "FavoriteLocationData{" +
                "mName='" + mName + '\'' +
                ", mMemo='" + mMemo + '\'' +
                ", mImagePath='" + mImagePath + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mDrawable=" + mDrawable +
                '}';
    }
}
