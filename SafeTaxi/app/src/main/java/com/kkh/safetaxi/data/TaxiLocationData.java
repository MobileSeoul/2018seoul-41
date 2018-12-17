package com.kkh.safetaxi.data;

/**
 * Created by Administrator on 2018-08-14.
 */

public class TaxiLocationData {
    private String mId;
    private String mAddress;
    private String mLatitude;
    private String mLongitude;
    private String mCode;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
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

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    @Override
    public String toString() {
        return "TaxiLocationData{" +
                "mId='" + mId + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mCode='" + mCode + '\'' +
                '}';
    }
}
