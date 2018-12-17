package com.kkh.safetaxi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-08-14.
 */

public class DatabaseManager {

    private static final String TAG = "[KKH]DatabaseManager";
    public static final String DB_NAME = "seoul_taxi.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_TAXI_LOCATION = "taxi_location";
    public static final String TABLE_CURRENT_LOCATION = "current_location";
    public static final String TABLE_LOCATION_KOR = "location_kor";
    public static final String TABLE_TAXI_INFO = "taxi_info";
    public static final String TABLE_FAVORITE_LOCATION = "favorite_location";
    public static final String TABLE_EMBASSY = "embassy";
    public static final String TABLE_MUSEUM = "location_museum";
    public static final String TABLE_PALACE = "location_palace";
    public static final String TABLE_ATTRACTION = "location_attraction";
    public static final String TABLE_EXCHANGE = "exchange";
    public static final String TABLE_IMAGE_INFO = "image_info";

    public static final String COL_ID = "_id";
    public static final String COL_ADDRESS = "address";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_CODE = "code";
    public static final String COL_NAME = "name";
    public static final String COL_TIME = "time";
    public static final String COL_DISTANCE = "distance";

    public static final String COL_START_PLACE = "start_place";
    public static final String COL_DESTINATION_PLACE = "destination_place";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_DESTINATION_TIME = "destination_time";
    public static final String COL_TRAVEL_TIME = "travel_time";
    public static final String COL_FEE = "fee";
    public static final String COL_REVIEW = "review";
    public static final String COL_REVIEW_SCORE = "review_score";
    public static final String COL_IMAGE_PATH = "image_path";

    public static final String COL_MEMO = "memo";
    public static final String COL_PHONE = "phone";
    public static final String COL_AREA = "area";
    public static final String COL_NAME_ENG = "name_eng";

    public static final String COL_UNIT = "unit";
    public static final String COL_BKPR = "bkpr";

    public static final String COL_CATEGORY = "category";
    public static final String COL_NAME_IMAGE = "name_image";
    public static final String COL_URL_IMAGE = "url_image";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Context mContext;
    public static DatabaseManager sDbManager;
    private ArrayList<ImageData> imageDataListByName;

    public DatabaseManager(Context context) {
        mContext = context;
        mDbHelper = new DatabaseHelper(context, DB_NAME);
    }

    public static DatabaseManager getInstance(Context context) {

        if (sDbManager == null) {
            sDbManager = new DatabaseManager(context);
        }
        return sDbManager;
    }

    public ArrayList<CurrentLocationData> getCurrentLocationDataList() {
        ArrayList<CurrentLocationData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CURRENT_LOCATION;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(getCurrentLocationData(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ArrayList<FavoriteLocationData> getFavoriteLocationDataList() {
        ArrayList<FavoriteLocationData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_FAVORITE_LOCATION;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(getFavoriteLocationData(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ArrayList<CurrentLocationData> getCurrentLocationDataListByTaxiInfo(TaxiInfo taxiInfo) {
        ArrayList<CurrentLocationData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CURRENT_LOCATION
                + " WHERE " + COL_START_PLACE + "=" + "'" + taxiInfo.getmStartPlace() + "'"
                + " AND " + COL_START_TIME + "=" + "'" + taxiInfo.getmStartTime() + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(getCurrentLocationData(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ArrayList<MuseumData> getMuseumDataList() {
        ArrayList<MuseumData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_MUSEUM;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(COL_ID));
                String name = c.getString(c.getColumnIndex(COL_NAME));
                String name_eng = c.getString(c.getColumnIndex(COL_NAME_ENG));
                MuseumData data = new MuseumData();
                data.setmId(id);
                data.setmName(name);
                data.setmNameEng(name_eng);
                list.add(data);
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ArrayList<PalaceData> getPalaceDataList() {
        ArrayList<PalaceData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_PALACE;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(COL_ID));
                String name = c.getString(c.getColumnIndex(COL_NAME));
                String name_eng = c.getString(c.getColumnIndex(COL_NAME_ENG));
                PalaceData data = new PalaceData();
                data.setmId(id);
                data.setmName(name);
                data.setmNameEng(name_eng);
                list.add(data);
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ExchangeData getExchangeData() {
        Log.d(TAG, "getExchangeData");
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_EXCHANGE;
        Cursor c = mDb.rawQuery(sql, null);
        ExchangeData data = null;
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                data = new ExchangeData();
                String id = c.getString(c.getColumnIndex(COL_ID));
                String name = c.getString(c.getColumnIndex(COL_NAME));
                String unit = c.getString(c.getColumnIndex(COL_UNIT));
                String bkpr = c.getString(c.getColumnIndex(COL_BKPR));
                data.setmCurNm(name);
                data.setmCurUnit(unit);
                data.setmBKPR(bkpr);
                c.moveToNext();
            }
        }
        c.close();
        return data;
    }

    public ArrayList<AttractionData> getAttractionDataList() {
        ArrayList<AttractionData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_ATTRACTION;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(COL_ID));
                String name = c.getString(c.getColumnIndex(COL_NAME));
                String name_eng = c.getString(c.getColumnIndex(COL_NAME_ENG));
                AttractionData data = new AttractionData();
                data.setmId(id);
                data.setmName(name);
                data.setmNameEng(name_eng);
                list.add(data);
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ArrayList<ImageData> getImageDataList(ImageData imageData) {
        ArrayList<ImageData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_IMAGE_INFO + " WHERE " + COL_NAME + "=" + "'" + imageData.getmName() + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(COL_ID));
                String category = c.getString(c.getColumnIndex(COL_CATEGORY));
                String name = c.getString(c.getColumnIndex(COL_NAME));
                String name_eng = c.getString(c.getColumnIndex(COL_NAME_ENG));
                String name_image = c.getString(c.getColumnIndex(COL_NAME_IMAGE));
                String url_image = c.getString(c.getColumnIndex(COL_URL_IMAGE));
                ImageData data = new ImageData();
                data.setmId(id);
                data.setmCategory(category);
                data.setmName(name);
                data.setmNameEng(name_eng);
                data.setmNameImage(name_image);
                data.setmUrlImage(url_image);
                list.add(data);
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    private CurrentLocationData getCurrentLocationData(Cursor c) {
        String id = c.getString(c.getColumnIndex(COL_ID));
        String name = c.getString(c.getColumnIndex(COL_NAME));
        String time = c.getString(c.getColumnIndex(COL_TIME));
        String latitude = c.getString(c.getColumnIndex(COL_LATITUDE));
        String longitude = c.getString(c.getColumnIndex(COL_LONGITUDE));
        String start = c.getString(c.getColumnIndex(COL_START_PLACE));
        String destination = c.getString(c.getColumnIndex(COL_DESTINATION_PLACE));
        float distance = c.getFloat(c.getColumnIndex(COL_DISTANCE));
        int code = c.getInt(c.getColumnIndex(COL_CODE));
        String start_time = c.getString(c.getColumnIndex(COL_START_TIME));
        CurrentLocationData data = new CurrentLocationData();
        data.setmId(id);
        data.setmLatitude(latitude);
        data.setmLongitude(longitude);
        data.setmName(name);
        data.setmTime(time);
        data.setmCode(code);
        data.setmStart(start);
        data.setmDestination(destination);
        data.setmDistance(distance);
        data.setmStartTime(start_time);
        return data;
    }

    private FavoriteLocationData getFavoriteLocationData(Cursor c) {
        String name = c.getString(c.getColumnIndex(COL_NAME));
        String latitude = c.getString(c.getColumnIndex(COL_LATITUDE));
        String longitude = c.getString(c.getColumnIndex(COL_LONGITUDE));
        String memo = c.getString(c.getColumnIndex(COL_MEMO));
        String address = c.getString(c.getColumnIndex(COL_ADDRESS));
        String image_path = c.getString(c.getColumnIndex(COL_IMAGE_PATH));

        FavoriteLocationData data = new FavoriteLocationData();
        data.setmLatitude(latitude);
        data.setmLongitude(longitude);
        data.setmName(name);
        data.setmAddress(address);
        data.setmMemo(memo);
        data.setmImagePath(image_path);
        return data;
    }

    public void insertCurrentLocation(CurrentLocationData data) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, data.getmName());
        cv.put(COL_LATITUDE, data.getmLatitude());
        cv.put(COL_LONGITUDE, data.getmLongitude());
        cv.put(COL_TIME, data.getmTime());
        cv.put(COL_CODE, data.getmCode());
        cv.put(COL_START_PLACE, data.getmStart());
        cv.put(COL_DESTINATION_PLACE, data.getmDestination());
        cv.put(COL_DISTANCE, data.getmDistance());
        cv.put(COL_START_TIME, data.getmStartTime());
        mDb.insert(TABLE_CURRENT_LOCATION, null, cv);
    }

    public void insertFavoriteLocation(FavoriteLocationData data) {
        Log.d(TAG, "insertFavoriteLocation data:" + data.toString());
        mDb = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, data.getmName());
        cv.put(COL_LATITUDE, data.getmLatitude());
        cv.put(COL_LONGITUDE, data.getmLongitude());
        cv.put(COL_ADDRESS, data.getmAddress());
        cv.put(COL_MEMO, data.getmMemo());
        cv.put(COL_IMAGE_PATH, data.getmImagePath());
        mDb.insert(TABLE_FAVORITE_LOCATION, null, cv);
    }


    public ArrayList<TaxiLocationData> getTaxiLocationDataList(String code) {
        ArrayList<TaxiLocationData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        //String sql = "SELECT * FROM " + TABLE_TAXI_LOCATION + " WHERE " + COL_CODE + "=" + "'" + code + "'";
        String sql = "SELECT * FROM " + TABLE_TAXI_LOCATION;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(getTaxiLocationData(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ArrayList<TaxiInfo> getTaxiDataList() {
        ArrayList<TaxiInfo> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_TAXI_INFO;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(getTaxiInfo(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public ArrayList<EmbassyData> getEmbassyDataList(String area) {
        ArrayList<EmbassyData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "";
        if ("".equalsIgnoreCase(area)) {
            sql = "SELECT * FROM " + TABLE_EMBASSY;
        } else {
            sql = "SELECT * FROM " + TABLE_EMBASSY + " WHERE " + COL_AREA + "=" + "'" + area + "'";
        }

        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(getEmbassyData(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    private EmbassyData getEmbassyData(Cursor c) {

        String id = c.getString(c.getColumnIndex(COL_ID));
        String name = c.getString(c.getColumnIndex(COL_NAME));
        String address = c.getString(c.getColumnIndex(COL_ADDRESS));
        String phone = c.getString(c.getColumnIndex(COL_PHONE));
        String latitude = c.getString(c.getColumnIndex(COL_LATITUDE));
        String longitude = c.getString(c.getColumnIndex(COL_LONGITUDE));
        String area = c.getString(c.getColumnIndex(COL_AREA));
        EmbassyData data = new EmbassyData();
        data.setmId(id);
        data.setmAddress(address);
        data.setmName(name);
        data.setmPhone(phone);
        data.setmLatitude(latitude);
        data.setmLongitude(longitude);
        data.setmArea(area);
        return data;
    }

    private TaxiLocationData getTaxiLocationData(Cursor c) {

        String id = c.getString(c.getColumnIndex(COL_ID));
        String address = c.getString(c.getColumnIndex(COL_ADDRESS));
        String code = c.getString(c.getColumnIndex(COL_CODE));
        String latitude = c.getString(c.getColumnIndex(COL_LATITUDE));
        String longitude = c.getString(c.getColumnIndex(COL_LONGITUDE));
        TaxiLocationData data = new TaxiLocationData();
        data.setmId(id);
        data.setmAddress(address);
        data.setmCode(code);
        data.setmLatitude(latitude);
        data.setmLongitude(longitude);
        return data;
    }


    private TaxiInfo getTaxiInfo(Cursor c) {

        int id = c.getInt(c.getColumnIndex(COL_ID));
        String start_place = c.getString(c.getColumnIndex(COL_START_PLACE));
        String destination_pace = c.getString(c.getColumnIndex(COL_DESTINATION_PLACE));
        String start_time = c.getString(c.getColumnIndex(COL_START_TIME));
        String end_time = c.getString(c.getColumnIndex(COL_DESTINATION_TIME));
        float distance = c.getFloat(c.getColumnIndex(COL_DISTANCE));
        int travel_time = c.getInt(c.getColumnIndex(COL_TRAVEL_TIME));
        int fee = c.getInt(c.getColumnIndex(COL_FEE));
        String review = c.getString(c.getColumnIndex(COL_REVIEW));
        int review_score = c.getInt(c.getColumnIndex(COL_REVIEW_SCORE));
        String image_path = c.getString(c.getColumnIndex(COL_IMAGE_PATH));
        TaxiInfo data = new TaxiInfo();
        data.setmId(id);
        data.setmStartPlace(start_place);
        data.setmDestinationPlace(destination_pace);
        data.setmStartTime(start_time);
        data.setmEndTime(end_time);
        data.setmDistance(distance);
        data.setmTravelTime(travel_time);
        data.setmFee(fee);
        data.setmReview(review);
        data.setmReviewScore(review_score);
        data.setmImagePath(image_path);
        return data;
    }


    public ArrayList<LocationKorData> getLocationKorDataList() {
        ArrayList<LocationKorData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_LOCATION_KOR;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(getLocationKorData(c));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    private LocationKorData getLocationKorData(Cursor c) {
        String id = c.getString(c.getColumnIndex(COL_ID));
        String name = c.getString(c.getColumnIndex(COL_NAME));
        String code = c.getString(c.getColumnIndex(COL_CODE));
        LocationKorData data = new LocationKorData();
        data.setmId(id);
        data.setmCode(code);
        data.setmName(name);
        return data;
    }

    public String getLocationKorDataByCode(String name) {
        String code = "";
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_LOCATION_KOR + " WHERE " + COL_NAME + "='" + name + "'";
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                code = c.getString(c.getColumnIndex(COL_CODE));
                c.moveToNext();
            }
        }
        c.close();
        return code;
    }

    public void insertTaxiData(TaxiInfo data) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_START_PLACE, data.getmStartPlace());
        cv.put(COL_DESTINATION_PLACE, data.getmDestinationPlace());
        cv.put(COL_START_TIME, data.getmStartTime());
        cv.put(COL_DESTINATION_PLACE, data.getmDestinationPlace());
        cv.put(COL_DESTINATION_TIME, data.getmEndTime());
        cv.put(COL_DISTANCE, data.getmDistance());
        cv.put(COL_TRAVEL_TIME, data.getmTravelTime());
        cv.put(COL_FEE, data.getmFee());
        cv.put(COL_REVIEW, data.getmReview());
        cv.put(COL_REVIEW_SCORE, data.getmReviewScore());
        cv.put(COL_IMAGE_PATH, data.getmImagePath());
        mDb.insert(TABLE_TAXI_INFO, null, cv);
    }

    public void deleteFavoriteLocationData(FavoriteLocationData data) {
        mDb = mDbHelper.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_FAVORITE_LOCATION + " WHERE " + COL_NAME + "=" + "'" + data.getmName() + "'";
        mDb.execSQL(sql);
    }

    public void deleteTaxiInfo(TaxiInfo data) {
        mDb = mDbHelper.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_TAXI_INFO + " WHERE " + COL_START_TIME + "=" + "'" + data.getmStartTime() + "'";
        mDb.execSQL(sql);
        sql = "DELETE FROM " + TABLE_CURRENT_LOCATION + " WHERE " + COL_START_TIME + "=" + "'" + data.getmStartTime() + "'";
        mDb.execSQL(sql);
    }

    public void updateTaxiData(TaxiInfo taxiInfo) {
        mDb = mDbHelper.getWritableDatabase();
        String sql = "UPDATE " + TABLE_TAXI_INFO +
                " SET " +
                COL_FEE + "=" + taxiInfo.getmFee() + "," +
                COL_REVIEW_SCORE + "=" + taxiInfo.getmReviewScore() + "," +
                COL_REVIEW + "=" + "'" + taxiInfo.getmReview() + "'" + "," +
                COL_IMAGE_PATH + "=" + "'" + taxiInfo.getmImagePath() + "'" +
                "WHERE " + COL_ID + "=" + taxiInfo.getmId();
        mDb.execSQL(sql);
    }

    public ArrayList<ImageData> getImageDataListByName() {
        ArrayList<ImageData> list = new ArrayList<>();
        mDb = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_IMAGE_INFO + " GROUP BY " + COL_NAME;
        Cursor c = mDb.rawQuery(sql, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(COL_ID));
                String category = c.getString(c.getColumnIndex(COL_CATEGORY));
                String name = c.getString(c.getColumnIndex(COL_NAME));
                String name_eng = c.getString(c.getColumnIndex(COL_NAME_ENG));
                String name_image = c.getString(c.getColumnIndex(COL_NAME_IMAGE));
                String url_image = c.getString(c.getColumnIndex(COL_URL_IMAGE));
                ImageData data = new ImageData();
                data.setmId(id);
                data.setmCategory(category);
                data.setmName(name);
                data.setmNameEng(name_eng);
                data.setmNameImage(name_image);
                data.setmUrlImage(url_image);
                c.moveToNext();
                list.add(data);
            }
        }
        c.close();
        return list;
    }

    public void setImageDataListByName(ArrayList<ImageData> imageDataListByName) {
        this.imageDataListByName = imageDataListByName;
    }

    public void insertExchangeData(ExchangeData data) {
        Log.d(TAG, "insertExchangeData data : " + data.toString());
        String sql = "";
        mDb = mDbHelper.getWritableDatabase();
        sql = "DELETE FROM " + TABLE_EXCHANGE;
        Cursor c = mDb.rawQuery(sql, null);
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, data.getmCurNm());
        cv.put(COL_UNIT, data.getmCurUnit());
        cv.put(COL_BKPR, data.getmBKPR());
        mDb.insert(TABLE_EXCHANGE, null, cv);


    }

    class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name) {
            super(context, name, null, DB_VERSION);
        }

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
