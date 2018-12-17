package com.kkh.safetaxi.common;

import android.util.Log;

import com.kkh.safetaxi.data.ExchangeData;
import com.kkh.safetaxi.data.PlaceSearchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018-08-15.
 */

public class NetworkUtil {
    private static OkHttpClient sOkHttpClient;
    private static final String TAG = "[KKH]NetworkUtil";

    public static NetworkUtil sInstance = new NetworkUtil();

    public static NetworkUtil getsInstance() {
        return sInstance;
    }

    private NetworkUtil() {
        sOkHttpClient = new OkHttpClient();
    }

    public static PlaceSearchData requestPlaceSearch(String query) {
        String text = "";
        try {
            text = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                "&query=" + query +
//                "&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating" +
//                "&locationbias=circle:" + Constant.RADIUS_SEOUL
//                + "@" + Constant.PREF_LATITUDE + "," + Constant.CENTER_SEOUL_LON
                "&key=" + Constant.KEY_PLACE;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = sOkHttpClient.newCall(request).execute();
            String res = response.body().string();
            return parsePlaceSearch(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<ExchangeData> parseExchangeData(String json) {
        ArrayList<ExchangeData> list = new ArrayList<>();

        JSONArray jsonArray;
        try {

            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                ExchangeData data = new ExchangeData();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String cur_unit = jsonObject.getString("cur_unit");
                String cur_nm = jsonObject.getString("cur_nm");
                String ttb = jsonObject.getString("ttb");
                String tts = jsonObject.getString("tts");
                String bkpr = jsonObject.getString("bkpr");
                data.setmCurUnit(cur_unit);
                data.setmCurNm(cur_nm);
                data.setmTTB(ttb);
                data.setmTTS(tts);
                data.setmBKPR(bkpr);
                if("KRW".equalsIgnoreCase(cur_unit)){
                    continue;
                }
                list.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static PlaceSearchData parsePlaceSearch(String json) {
        PlaceSearchData data = new PlaceSearchData();
        JSONObject jobj = null;
        try {

            jobj = new JSONObject(json);

            JSONObject item = (JSONObject) jobj.getJSONArray("results").get(0);
            String formatted_address = item.getString("formatted_address");
            String name = item.getString("name");

            JSONObject photos = (JSONObject) item.getJSONArray("photos").get(0);
            String photo_reference = photos.getString("photo_reference");
            int width = photos.getInt("width");
            int height = photos.getInt("height");

            String rating = item.getString("rating");
            data.setmPhotoWidth(String.valueOf(width));
            data.setmPhotoHeight(String.valueOf(height));
            data.setmPhotoReference(photo_reference);
            data.setmFormattedAddress(formatted_address);
            data.setmRating(rating);
            data.setmName(name);
            String url = "https://maps.googleapis.com/maps/api/place/photo?" +
                    "maxwidth=" + PHOTO_MAX_WIDTH + "&" + PHOTO_MAX_HEIGHT +
                    "&photoreference=" + photo_reference +
                    "&key=" + Constant.KEY_PLACE;
            data.setmImageUrl(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static final int PHOTO_MAX_WIDTH = 200;
    private static final int PHOTO_MAX_HEIGHT = 100;

    public static ArrayList<ExchangeData> requestExchangeData() {
        ArrayList<ExchangeData> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String time = sdf.format(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24)));
        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + Constant.KEY_EXCHANGE_RATE + "&searchdate=" + time + "&data=AP01";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = sOkHttpClient.newCall(request).execute();
            String res = response.body().string();
            list = parseExchangeData(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
