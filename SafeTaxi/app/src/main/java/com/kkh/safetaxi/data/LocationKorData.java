package com.kkh.safetaxi.data;

/**
 * Created by Administrator on 2018-08-14.
 */

public class LocationKorData {
    private String mId;
    private String mName;
    private String mCode;

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

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    @Override
    public String toString() {
        return "LocationKorData{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mCode='" + mCode + '\'' +
                '}';
    }
}
