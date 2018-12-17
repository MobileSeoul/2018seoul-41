package com.kkh.safetaxi.common;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kkh.safetaxi.R;

import java.util.HashMap;

/**
 * Created by Administrator on 2018-08-14.
 */

public class Constant {

    public static final String KEY_GOOGLE_MAP = "AIzaSyDTiHeT2W1cvcj7XConpvaUkvRdhGQCcV8";
    public static final String KEY_PLACE = "AIzaSyCtLQpAgdncuuOON9XWxFcqbMqfBqnze_8";
    public static final String PREF_LATITUDE = "pref_latitude";
    public static final String PREF_LONGITUDE = "pref_longitude";

    public static final double CENTER_SEOUL_LAT = 37.533005;
    public static final double CENTER_SEOUL_LON = 126.982096;
    public static final int RADIUS_SEOUL = 100000 / 2;
    public static final int LIMIT_TAXI_DISTANCE = 500;

    public static final String INTENT_LOCATION_CHANGE_EVENT = "intent_location_change_event";
    public static final String EXTRA_CURRENT_LOCATION_DATA = "current_location_data";

    public static final String EXTRA_TAXI_INFO = "extra_taxi_info";

    public static final LatLngBounds BOUNDS_SEOUL = new LatLngBounds(
            new LatLng(37.078555, 128.118694), new LatLng(37.781422, 125.978712));

    public static final int LOCATION_TIMER_PERIOD = 5 * 1000;

    public static HashMap<String, Integer> EMBASSY_DRAWABLE_LIST = new HashMap<String, Integer>();
    public static HashMap<String, String> EXCHANGE_LIST = new HashMap<String, String>();

    static {
        EMBASSY_DRAWABLE_LIST.put("usa", R.drawable.country_usa);
        EMBASSY_DRAWABLE_LIST.put("japan", R.drawable.country_japan);
        EMBASSY_DRAWABLE_LIST.put("china", R.drawable.country_china);
        EMBASSY_DRAWABLE_LIST.put("taiwan", R.drawable.country_taiwan);
        EMBASSY_DRAWABLE_LIST.put("russia", R.drawable.country_russia);
        EMBASSY_DRAWABLE_LIST.put("canada", R.drawable.country_canada);
        EMBASSY_DRAWABLE_LIST.put("england", R.drawable.country_england);
        EMBASSY_DRAWABLE_LIST.put("france", R.drawable.country_france);
        EMBASSY_DRAWABLE_LIST.put("germany", R.drawable.country_germany);
        EMBASSY_DRAWABLE_LIST.put("malaysia", R.drawable.country_malaysia);
        EMBASSY_DRAWABLE_LIST.put("thailand", R.drawable.country_thailand);
        EMBASSY_DRAWABLE_LIST.put("singapore", R.drawable.country_singapore);
        EMBASSY_DRAWABLE_LIST.put("australia", R.drawable.country_australia);

        EXCHANGE_LIST.put("AED","Arab Emirates");
        EXCHANGE_LIST.put("AUD","Australia");
        EXCHANGE_LIST.put("BHD","Bahrain");
        EXCHANGE_LIST.put("CAD","Canada");
        EXCHANGE_LIST.put("CHF","Switzerland");
        EXCHANGE_LIST.put("CNH","China");
        EXCHANGE_LIST.put("DKK","Denmark");
        EXCHANGE_LIST.put("EUR","Europe");
        EXCHANGE_LIST.put("GBP","United Kingdom");
        EXCHANGE_LIST.put("HKD","Hongkong");
        EXCHANGE_LIST.put("IDR(100)","Indonesia");
        EXCHANGE_LIST.put("JPY(100)","Japan");
        EXCHANGE_LIST.put("KWD","Kuwait");
        EXCHANGE_LIST.put("MYR","Malaysia");
        EXCHANGE_LIST.put("NOK","Norway");
        EXCHANGE_LIST.put("NZD","New Zealand");
        EXCHANGE_LIST.put("SAR","Southey");
        EXCHANGE_LIST.put("SEK","Sweden");
        EXCHANGE_LIST.put("SGD","Singapore");
        EXCHANGE_LIST.put("THB","Thailand");


    }

    public static final String NOTI_CHANNEL_ID = "com.safe.taxi";
    public static final int LIMIT_DISTANCE = 1000;
    public static final String KEY_EXCHANGE_RATE = "Oz1DAE2NATqBz8Dt0g8hun7DQTyOMrBZ";

}
