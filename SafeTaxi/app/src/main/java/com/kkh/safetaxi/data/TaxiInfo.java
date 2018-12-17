package com.kkh.safetaxi.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class TaxiInfo implements Parcelable {
    private int mId;
    private String mStartPlace;
    private String mDestinationPlace;
    private String mStartTime;
    private String mEndTime;
    private double mDistance;
    private int mTravelTime;
    private int mFee;
    private String mReview;
    private String mNumber;
    private Bitmap mCarBitmap;
    private int mReviewScore;
    private String mImagePath = "";

    public TaxiInfo() {

    }

    protected TaxiInfo(Parcel in) {
        mId = in.readInt();
        mStartPlace = in.readString();
        mDestinationPlace = in.readString();
        mStartTime = in.readString();
        mEndTime = in.readString();
        mDistance = in.readDouble();
        mTravelTime = in.readInt();
        mFee = in.readInt();
        mReview = in.readString();
        mNumber = in.readString();
        mCarBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        mReviewScore = in.readInt();
        mImagePath = in.readString();
    }

    public static final Creator<TaxiInfo> CREATOR = new Creator<TaxiInfo>() {
        @Override
        public TaxiInfo createFromParcel(Parcel in) {
            return new TaxiInfo(in);
        }

        @Override
        public TaxiInfo[] newArray(int size) {
            return new TaxiInfo[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmStartPlace() {
        return mStartPlace;
    }

    public void setmStartPlace(String mStartPlace) {
        this.mStartPlace = mStartPlace;
    }

    public String getmDestinationPlace() {
        return mDestinationPlace;
    }

    public void setmDestinationPlace(String mDestinationPlace) {
        this.mDestinationPlace = mDestinationPlace;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public int getmTravelTime() {
        return mTravelTime;
    }

    public void setmTravelTime(int mTravelTime) {
        this.mTravelTime = mTravelTime;
    }

    public int getmFee() {
        return mFee;
    }

    public void setmFee(int mFee) {
        this.mFee = mFee;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public Bitmap getmCarBitmap() {
        return mCarBitmap;
    }

    public void setmCarBitmap(Bitmap mCarBitmap) {
        this.mCarBitmap = mCarBitmap;
    }

    public int getmReviewScore() {
        return mReviewScore;
    }

    public void setmReviewScore(int mReviewScore) {
        this.mReviewScore = mReviewScore;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    @Override
    public String toString() {
        return "TaxiInfo{" +
                "mId=" + mId +
                ", mStartPlace='" + mStartPlace + '\'' +
                ", mDestinationPlace='" + mDestinationPlace + '\'' +
                ", mStartTime='" + mStartTime + '\'' +
                ", mEndTime='" + mEndTime + '\'' +
                ", mDistance=" + mDistance +
                ", mTravelTime=" + mTravelTime +
                ", mFee=" + mFee +
                ", mReview='" + mReview + '\'' +
                ", mNumber='" + mNumber + '\'' +
                ", mCarBitmap=" + mCarBitmap +
                ", mReviewScore=" + mReviewScore +
                ", mImagePath='" + mImagePath + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mStartPlace);
        parcel.writeString(mDestinationPlace);
        parcel.writeString(mStartTime);
        parcel.writeString(mEndTime);
        parcel.writeDouble(mDistance);
        parcel.writeInt(mTravelTime);
        parcel.writeInt(mFee);
        parcel.writeString(mReview);
        parcel.writeString(mNumber);
        parcel.writeParcelable(mCarBitmap, i);
        parcel.writeInt(mReviewScore);
        parcel.writeString(mImagePath);
    }
}
