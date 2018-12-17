package com.kkh.safetaxi.data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Administrator on 2018-08-15.
 */

public class PlaceData {
    private String mName;
    private String mAddress;
    private LatLng mLatLng;
    private String mImagePath;

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

    public LatLng getmLatLng() {
        return mLatLng;
    }

    public void setmLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }
}
