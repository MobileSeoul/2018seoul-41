package com.kkh.safetaxi.data;

/**
 * Created by Administrator on 2018-08-15.
 */

public class PlaceSearchData {
    private String mPhotoWidth;
    private String mPhotoHeight;
    private String mPhotoReference;
    private String mRating;
    private String mFormattedAddress;
    private String mName;
    private String mImageUrl;

    public String getmPhotoWidth() {
        return mPhotoWidth;
    }

    public void setmPhotoWidth(String mPhotoWidth) {
        this.mPhotoWidth = mPhotoWidth;
    }

    public String getmPhotoHeight() {
        return mPhotoHeight;
    }

    public void setmPhotoHeight(String mPhotoHeight) {
        this.mPhotoHeight = mPhotoHeight;
    }

    public String getmPhotoReference() {
        return mPhotoReference;
    }

    public void setmPhotoReference(String mPhotoReference) {
        this.mPhotoReference = mPhotoReference;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmFormattedAddress() {
        return mFormattedAddress;
    }

    public void setmFormattedAddress(String mFormattedAddress) {
        this.mFormattedAddress = mFormattedAddress;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    @Override
    public String toString() {
        return "PlaceSearchData{" +
                "mPhotoWidth='" + mPhotoWidth + '\'' +
                ", mPhotoHeight='" + mPhotoHeight + '\'' +
                ", mPhotoReference='" + mPhotoReference + '\'' +
                ", mRating='" + mRating + '\'' +
                ", mFormattedAddress='" + mFormattedAddress + '\'' +
                ", mName='" + mName + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
