package com.kkh.safetaxi.data;

public class ExchangeData {
    private String mCurUnit;
    private String mCurNm;
    private String mTTB;
    private String mTTS;
    private String mBKPR;

    public String getmCurUnit() {
        return mCurUnit;
    }

    public void setmCurUnit(String mCurUnit) {
        this.mCurUnit = mCurUnit;
    }

    public String getmCurNm() {
        return mCurNm;
    }

    public void setmCurNm(String mCurNm) {
        this.mCurNm = mCurNm;
    }

    public String getmTTB() {
        return mTTB;
    }

    public void setmTTB(String mTTB) {
        this.mTTB = mTTB;
    }

    public String getmTTS() {
        return mTTS;
    }

    public void setmTTS(String mTTS) {
        this.mTTS = mTTS;
    }

    public String getmBKPR() {
        return mBKPR;
    }

    public void setmBKPR(String mBKPR) {
        this.mBKPR = mBKPR;
    }

    @Override
    public String toString() {
        return "ExchangeData{" +
                "mCurUnit='" + mCurUnit + '\'' +
                ", mCurNm='" + mCurNm + '\'' +
                ", mTTB='" + mTTB + '\'' +
                ", mTTS='" + mTTS + '\'' +
                ", mBKPR='" + mBKPR + '\'' +
                '}';
    }
}
