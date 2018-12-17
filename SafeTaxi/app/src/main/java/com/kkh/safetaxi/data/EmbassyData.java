package com.kkh.safetaxi.data;

import android.os.Parcel;
import android.os.Parcelable;

public class EmbassyData implements Parcelable{
    private String mId;
    private String mName;
    private String mAddress;
    private String mPhone;
    private String mLatitude;
    private String mLongitude;
    private String mArea;

    public EmbassyData(){

    }

    protected EmbassyData(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mAddress = in.readString();
        mPhone = in.readString();
        mLatitude = in.readString();
        mLongitude = in.readString();
        mArea = in.readString();
    }

    public static final Creator<EmbassyData> CREATOR = new Creator<EmbassyData>() {
        @Override
        public EmbassyData createFromParcel(Parcel in) {
            return new EmbassyData(in);
        }

        @Override
        public EmbassyData[] newArray(int size) {
            return new EmbassyData[size];
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

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
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

    public String getmArea() {
        return mArea;
    }

    public void setmArea(String mArea) {
        this.mArea = mArea;
    }

    @Override
    public String toString() {
        return "EmbassyData{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mArea='" + mArea + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mAddress);
        dest.writeString(mPhone);
        dest.writeString(mLatitude);
        dest.writeString(mLongitude);
        dest.writeString(mArea);
    }
}
