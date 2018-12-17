package com.kkh.safetaxi.data;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-09-19.
 */

@SuppressLint("ParcelCreator")
public class ImageData implements Parcelable{
    private String mId;
    private String mCategory;
    private String mName;
    private String mNameEng;
    private String mNameImage;
    private String mUrlImage;

    public ImageData(){

    }

    protected ImageData(Parcel in) {
        mId = in.readString();
        mCategory = in.readString();
        mName = in.readString();
        mNameEng = in.readString();
        mNameImage = in.readString();
        mUrlImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mCategory);
        dest.writeString(mName);
        dest.writeString(mNameEng);
        dest.writeString(mNameImage);
        dest.writeString(mUrlImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNameEng() {
        return mNameEng;
    }

    public void setmNameEng(String mNameEng) {
        this.mNameEng = mNameEng;
    }

    public String getmNameImage() {
        return mNameImage;
    }

    public void setmNameImage(String mNameImage) {
        this.mNameImage = mNameImage;
    }

    public String getmUrlImage() {
        return mUrlImage;
    }

    public void setmUrlImage(String mUrlImage) {
        this.mUrlImage = mUrlImage;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "mId='" + mId + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mName='" + mName + '\'' +
                ", mNameEng='" + mNameEng + '\'' +
                ", mNameImage='" + mNameImage + '\'' +
                ", mUrlImage='" + mUrlImage + '\'' +
                '}';
    }
}
