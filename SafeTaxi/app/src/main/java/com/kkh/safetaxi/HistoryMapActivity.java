package com.kkh.safetaxi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.CurrentLocationData;
import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.TaxiInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.kkh.safetaxi.common.Constant.EXTRA_TAXI_INFO;
import static com.kkh.safetaxi.common.Util.FILE_PATH;
import static com.kkh.safetaxi.common.Util.createDrawableFromView;

public class HistoryMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "[KKH]HistoryMapActivity";

    private Context mContext;
    private TaxiInfo mTaxiInfo;
    private ArrayList<CurrentLocationData> mCurrentLocationDataList = new ArrayList<>();
    private GoogleMap mGoogleMap;
    private UiSettings mUiSettings;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<LatLng> mLatLngList = new ArrayList<>();

    private LinearLayout mLayout;
    private ImageView mImageView;
    private TextView mStartPlace;
    private TextView mStartTime;
    private TextView mDestinationPlace;
    private TextView mDestinationTime;
    private TextView mDistance;
    private TextView mTravelTime;
    private TextView mFee;
    private TextView mReviewScore;
    private TextView mReview;
    private View mCustomMarkerLayout;
    private TextView mCustomMarkerName;
    private TextView mCustomMarkerTime;
    private Button mScreenShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_map);

        mContext = this;
        Intent intent = getIntent();
        mTaxiInfo = intent.getParcelableExtra(EXTRA_TAXI_INFO);
        if (mTaxiInfo == null) {
            finish();
        }
        mCurrentLocationDataList = DatabaseManager.getInstance(mContext).getCurrentLocationDataListByTaxiInfo(mTaxiInfo);
        initViews();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnInfoWindowLongClickListener(this);
        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnMyLocationClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setOnInfoWindowCloseListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.d(TAG, "onMyLocationChange");

            }
        });
        mUiSettings = mGoogleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        enableMyLocation();
        addCurrentLocationMarkers();

        //addPolyLine();
    }

    private void addPolyLine() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
        polylineOptions.addAll(mLatLngList);
        mGoogleMap.addPolyline(polylineOptions);
    }

    private void addCurrentLocationMarkers() {
        for (int i = 0; i < mCurrentLocationDataList.size(); i++) {
            CurrentLocationData data = mCurrentLocationDataList.get(i);
            addCurrentLocationMarker(data, i);
            if (i == mCurrentLocationDataList.size() - 1) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(data.getmLatitude()), Double.parseDouble(data.getmLongitude())), 15));
            }
        }
    }

    private void takeScreenShot2() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                try {
                    String filename = Util.getTimeFormatText4(System.currentTimeMillis()) + ".jpg";
                    String path = FILE_PATH + filename;
                    File imageFile = new File(path);
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(mContext, String.format(mContext.getString(R.string.shot_message), filename), Toast.LENGTH_SHORT).show();
                } catch (Throwable e) {
                    // Several error may come out with file handling or OOM
                    e.printStackTrace();
                }

            }
        };
        if (mGoogleMap != null) {
            mGoogleMap.snapshot(callback);
        }
    }

    private void addCurrentLocationMarker(CurrentLocationData data, int index) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.flat(false);
        double lat = Double.parseDouble(data.getmLatitude());
        double lnt = Double.parseDouble(data.getmLongitude());
        markerOptions.position(new LatLng(lat, lnt));
        String time = String.format(mContext.getString(R.string.start_time), data.getmTime());
        markerOptions.draggable(false);
        mCustomMarkerLayout.setBackground(mContext.getDrawable(R.drawable.ic_marker_phone));
        mCustomMarkerTime.setText(time);
        mCustomMarkerName.setText(data.getmName());
        markerOptions.title(time);
        BitmapDrawable bd = (BitmapDrawable) mContext.getDrawable(R.drawable.icon_circle);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap((bd.getBitmap())));

        Marker marker = mGoogleMap.addMarker(markerOptions);
        mLatLngList.add(new LatLng(lat, lnt));
    }

    private void initViews() {
        mLayout = findViewById(R.id.frame);
        mImageView = findViewById(R.id.image);
        mStartPlace = findViewById(R.id.start_place);
        mStartTime = findViewById(R.id.start_time);
        mDestinationPlace = findViewById(R.id.destination_place);
        mDestinationTime = findViewById(R.id.destination_time);
        mDistance = findViewById(R.id.distance);
        mTravelTime = findViewById(R.id.travel_time);
        mFee = findViewById(R.id.fee);
        mScreenShot = findViewById(R.id.shot);

        mStartPlace.setText(String.format(mContext.getString(R.string.label_from), mTaxiInfo.getmStartPlace()));
        mStartTime.setText(String.format(mContext.getString(R.string.start_time), Util.getTimeFormatText2(Long.parseLong(mTaxiInfo.getmStartTime()))));
        mDestinationPlace.setText(String.format(mContext.getString(R.string.label_to), mTaxiInfo.getmDestinationPlace()));
        mDestinationTime.setText(String.format(mContext.getString(R.string.end_time), Util.getTimeFormatText2(Long.parseLong(mTaxiInfo.getmEndTime()))));
        mDistance.setText(String.format(mContext.getString(R.string.distance), String.format("%.2f", mTaxiInfo.getmDistance())));
        mTravelTime.setText(String.format(mContext.getString(R.string.travel_time), String.valueOf(Util.mliToMinute(mTaxiInfo.getmTravelTime()))));
        mFee.setText(String.format(mContext.getString(R.string.fee), String.valueOf(mTaxiInfo.getmFee())));

        mCustomMarkerLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_marker, null);
        mCustomMarkerName = mCustomMarkerLayout.findViewById(R.id.name);
        mCustomMarkerTime = mCustomMarkerLayout.findViewById(R.id.time);

        mScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takeScreenShot();
                takeScreenShot2();


            }
        });

    }

    @SuppressLint("StringFormatInvalid")
    private void takeScreenShot() {
        try {
            // image naming and path  to include sd card  appending name you choose for file
            // 저장할 주소 + 이름
            String filename = Util.getTimeFormatText4(System.currentTimeMillis()) + ".jpg";
            String path = FILE_PATH + filename;

            // create bitmap screen capture
            // 화면 이미지 만들기
            mLayout.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(mLayout.getDrawingCache());
            mLayout.setDrawingCacheEnabled(false);
//            View v1 = getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);

            // 이미지 파일 생성
            File imageFile = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(mContext, String.format(mContext.getString(R.string.shot_message), filename), Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }

    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
    }
}
