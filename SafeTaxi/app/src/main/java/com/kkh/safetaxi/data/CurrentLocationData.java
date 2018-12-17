package com.kkh.safetaxi.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentLocationData implements Parcelable {
    private String mId;
    private String mName;
    private String mLatitude;
    private String mLongitude;
    private String mTime;
    private int mCode;
    private String mStart;
    private String mDestination;
    private float mDistance;
    private String mStartTime;

    public CurrentLocationData() {
    }


    protected CurrentLocationData(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLatitude = in.readString();
        mLongitude = in.readString();
        mTime = in.readString();
        mCode = in.readInt();
        mStart = in.readString();
        mDestination = in.readString();
        mDistance = in.readFloat();
        mStartTime = in.readString();
    }

    public static final Creator<CurrentLocationData> CREATOR = new Creator<CurrentLocationData>() {
        @Override
        public CurrentLocationData createFromParcel(Parcel in) {
            return new CurrentLocationData(in);
        }

        @Override
        public CurrentLocationData[] newArray(int size) {
            return new CurrentLocationData[size];
        }
    };

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
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

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public int getmCode() {
        return mCode;
    }

    public void setmCode(int mCode) {
        this.mCode = mCode;
    }

    public String getmStart() {
        return mStart;
    }

    public void setmStart(String mStart) {
        this.mStart = mStart;
    }

    public String getmDestination() {
        return mDestination;
    }

    public void setmDestination(String mDestination) {
        this.mDestination = mDestination;
    }

    public float getmDistance() {
        return mDistance;
    }

    public void setmDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public static Creator<CurrentLocationData> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "CurrentLocationData{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mCode=" + mCode +
                ", mStart='" + mStart + '\'' +
                ", mDestination='" + mDestination + '\'' +
                ", mDistance=" + mDistance +
                ", mStartTime=" + mStartTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mLatitude);
        parcel.writeString(mLongitude);
        parcel.writeString(mTime);
        parcel.writeInt(mCode);
        parcel.writeString(mStart);
        parcel.writeString(mDestination);
        parcel.writeFloat(mDistance);
        parcel.writeString(mStartTime);
    }
}
