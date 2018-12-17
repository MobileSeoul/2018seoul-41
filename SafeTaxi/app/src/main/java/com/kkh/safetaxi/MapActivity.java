package com.kkh.safetaxi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
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
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kkh.safetaxi.common.Constant;
import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.AttractionData;
import com.kkh.safetaxi.data.CurrentLocationData;
import com.kkh.safetaxi.data.DataPref;
import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.FavoriteLocationData;
import com.kkh.safetaxi.data.ImageData;
import com.kkh.safetaxi.data.MuseumData;
import com.kkh.safetaxi.data.PalaceData;
import com.kkh.safetaxi.data.PlaceData;
import com.kkh.safetaxi.data.PlaceSearchData;
import com.kkh.safetaxi.data.TaxiInfo;
import com.kkh.safetaxi.data.TaxiLocationData;
import com.kkh.safetaxi.worker.PlaceWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kkh.safetaxi.ImageListActivity.EXTRA_IMAGE_DATA;
import static com.kkh.safetaxi.MainActivity.EXTRA_CODE;
import static com.kkh.safetaxi.TaxiDriveCompleteDialog.REQ_CODE_GALLERY;
import static com.kkh.safetaxi.TaxiDriveCompleteDialog.TYPE_NEW;
import static com.kkh.safetaxi.common.Constant.BOUNDS_SEOUL;
import static com.kkh.safetaxi.common.Constant.INTENT_LOCATION_CHANGE_EVENT;
import static com.kkh.safetaxi.common.Constant.LIMIT_DISTANCE;
import static com.kkh.safetaxi.common.Constant.NOTI_CHANNEL_ID;
import static com.kkh.safetaxi.common.Constant.PREF_LATITUDE;
import static com.kkh.safetaxi.common.Constant.PREF_LONGITUDE;
import static com.kkh.safetaxi.common.Util.createDrawableFromView;

public class MapActivity extends AppCompatActivity implements
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


    // Directions API
    // http://www.akexorcist.com/2015/12/google-direction-library-for-android-en.html

    private static final String TAG = "[KKH]MapActivity";
    private static final int REQ_CODE_MAP_ACTIVITY = 0;

    private Context mContext;
    private GoogleMap mGoogleMap;
    private UiSettings mUiSettings;
    private GoogleApiClient mGoogleApiClient;
    private double mCurrentLatitude = 0;
    private double mCurrentLongitude = 0;
    private double mDestinationLatitude = 0;
    private double mDestinationLongitude = 0;

    protected GeoDataClient mGeoDataClient;
    private PlaceAutocompleteAdapter mAdapter;
    private PlaceSearchData mPlaceSearchData;

    private AutoCompleteTextView mAutocompleteView;
    private Button mClear;
    private ImageView mImageView;
    private Button mImageButton;
    private ImageView mCarImageView;
    private ToggleButton mTaxiStop;
    private ToggleButton mFavoriteButton;
    private ToggleButton mStart;
    private TextView mStartPlace;
    private TextView mDistance;
    private TextView mTravelTime;
    private TextView mDestinationPlace;
    private View mCustomMarkerViewForFavorite;
    private View mCustomMarkerViewForCurrent;
    private LinearLayout mCustomMarkerLayout;
    private LinearLayout mCustomMarkerLayoutForCurrent;
    private TextView mCustomMarkerName;
    private TextView mCustomMarkerTimeForCurrent;
    private TextView mCustomMarkerTime;
    private ImageView mCustomMarkerImage;
    private TextView mCustomMarkerAddress;
    private TextView mCustomMarkerMemo;
    private boolean mIsStarted = false;
    private static double sDistanceSum = 0;
    private static long sStartTime = 0;
    private static long sTravelTime = 0;
    private String mCurrentImageUrl = "";
    private Drawable mCurrentImageDrawable;

    private LocationWorkService mLocationServices;
    private Intent mLocationWorkServiceIntent;

    private CurrentLocationData mCurrentLocationData = new CurrentLocationData();
    ;

    private ArrayList<TaxiLocationData> mTaxiLocationDataList = new ArrayList<>();


    private MainBroadCastReceiver mMainBroadCastReceiver;
    private boolean mIsFirst = true;

    private TaxiDriveCompleteDialog mTaxiDriveCompleteDialog;
    private DriveStartDialog mDriveStartDialog;

    private ArrayList<FavoriteLocationData> mFavoriteLocationDataList = new ArrayList<>();
    private ArrayList<Marker> mFavoriteLocationDataMarkerList = new ArrayList<>();

    private ArrayList<Marker> mMarkerList = new ArrayList<>();

    private ImageData mImageData;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_palace:
                showListMenuDialog(TYPE_PALACE);
                break;
            case R.id.menu_museum:
                showListMenuDialog(TYPE_MUSEUM);
                break;
            case R.id.menu_image:
                startImageListActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startImageListActivity() {
        Intent intent = new Intent(mContext, ImageListActivity.class);
        startActivityForResult(intent, REQ_CODE_MAP_ACTIVITY);
    }

    private final int TYPE_PALACE = 0;
    private final int TYPE_MUSEUM = 1;
    private final int TYPE_ATTRACTION = 2;

    private void showListMenuDialog(int type) {
        String items[] = new String[0];
        String items_keyword[] = new String[0];
        String title = "";
        final int[] index = {0};
        switch (type) {
            case TYPE_PALACE:
                ArrayList<PalaceData> list_palace = DatabaseManager.getInstance(mContext).getPalaceDataList();
                items = new String[list_palace.size()];
                items_keyword = new String[list_palace.size()];

                for (int i = 0; i < list_palace.size(); i++) {
                    items[i] = list_palace.get(i).getmNameEng() + "(" + list_palace.get(i).getmName() + ")";
                    items_keyword[i] = list_palace.get(i).getmNameEng();
                }
                title = mContext.getString(R.string.dialog_msg_palace);
                break;
            case TYPE_MUSEUM:
                ArrayList<MuseumData> list_museum = DatabaseManager.getInstance(mContext).getMuseumDataList();
                items = new String[list_museum.size()];
                items_keyword = new String[list_museum.size()];
                for (int i = 0; i < list_museum.size(); i++) {
                    items[i] = list_museum.get(i).getmNameEng() + "(" + list_museum.get(i).getmName() + ")";
                    items_keyword[i] = list_museum.get(i).getmNameEng();
                }
                title = mContext.getString(R.string.dialog_msg_museum);
                break;
            case TYPE_ATTRACTION:
                ArrayList<AttractionData> list_attraction = DatabaseManager.getInstance(mContext).getAttractionDataList();
                items = new String[list_attraction.size()];
                items_keyword = new String[list_attraction.size()];
                for (int i = 0; i < list_attraction.size(); i++) {
                    items[i] = list_attraction.get(i).getmNameEng() + "(" + list_attraction.get(i).getmName() + ")";
                    items_keyword[i] = list_attraction.get(i).getmNameEng();
                }
                title = mContext.getString(R.string.dialog_msg_attraction);
                break;
        }


        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle(title);
        final String[] finalItems_keyword = items_keyword;
        ab.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                index[0] = i;
            }
        }).setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAutocompleteView.setText(finalItems_keyword[index[0]]);
            }
        });
        ab.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        Log.d(TAG, "onCreate");
        mContext = this;
        Intent intent = getIntent();
        String code = intent.getStringExtra(EXTRA_CODE);
        mImageData = intent.getParcelableExtra(EXTRA_IMAGE_DATA);
        mTaxiLocationDataList = DatabaseManager.getInstance(mContext).getTaxiLocationDataList(code);

        mCustomMarkerViewForFavorite = LayoutInflater.from(mContext).inflate(R.layout.layout_marker, null);
        mCustomMarkerLayout = mCustomMarkerViewForFavorite.findViewById(R.id.layout);
        mCustomMarkerName = mCustomMarkerViewForFavorite.findViewById(R.id.name);
        mCustomMarkerTime = mCustomMarkerViewForFavorite.findViewById(R.id.time);
        mCustomMarkerImage = mCustomMarkerViewForFavorite.findViewById(R.id.image);
        mCustomMarkerAddress = mCustomMarkerViewForFavorite.findViewById(R.id.address);
        mCustomMarkerMemo = mCustomMarkerViewForFavorite.findViewById(R.id.memo);

        mCustomMarkerViewForCurrent = LayoutInflater.from(mContext).inflate(R.layout.layout_marker, null);
        mCustomMarkerLayoutForCurrent = mCustomMarkerViewForCurrent.findViewById(R.id.layout);
        mCustomMarkerTimeForCurrent = mCustomMarkerViewForCurrent.findViewById(R.id.time);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mCurrentLocationList.clear();
        DataPref.START_PLACE = "";
        DataPref.DESTINATION_PLACE = "";
        sDistanceSum = 0;
        if (mImageData == null) {
            showGuideDialog();
        }
    }

    private void showGuideDialog() {
        MapGuideDialog dialog = new MapGuideDialog(mContext);
        dialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        Log.d(TAG, "onInfoWindowClick");
        if (marker.getTag() == null) {
            Log.d(TAG, "tag is null");
            return;
        }

        if (marker.getTag() instanceof PlaceData) {
            Log.d(TAG, "tag is PlaceData");
        } else if (marker.getTag() instanceof FavoriteLocationData) {
            Log.d(TAG, "tag is FavoriteLocationData");
            return;
        } else {
            Log.e(TAG, marker.getTag().toString());
            return;
        }
        final PlaceData placeData = (PlaceData) marker.getTag();
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle(mContext.getString(R.string.dialog_msg_favorite));
        ab.setPositiveButton(mContext.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        showMemoInputDialog(placeData);
                    }
                }).setNegativeButton(mContext.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        ab.setCancelable(false);
        ab.show();
    }

    private void showMemoInputDialog(PlaceData placeData) {

        FavoriteLocationData favoriteLocationData = new FavoriteLocationData();
        favoriteLocationData.setmName(placeData.getmName());
        LatLng latlng = placeData.getmLatLng();
        favoriteLocationData.setmLatitude(String.valueOf(latlng.latitude));
        favoriteLocationData.setmLongitude(String.valueOf(latlng.longitude));
        favoriteLocationData.setmAddress(placeData.getmAddress());
        favoriteLocationData.setmImagePath(mCurrentImageUrl);

        FavoriteInputDialog dialog = new FavoriteInputDialog(mContext, favoriteLocationData);
        dialog.show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Log.d(TAG, "onInfoWindowLongClick");
        if (marker.getTag() == null) {
            Log.d(TAG, "tag is null");
            return;
        }

        if (marker.getTag() instanceof PlaceData) {
            Log.d(TAG, "tag is PlaceData");
            return;
        } else if (marker.getTag() instanceof FavoriteLocationData) {
            Log.d(TAG, "tag is FavoriteLocationData");
        } else {
            Log.e(TAG, marker.getTag().toString());
            return;
        }
        final FavoriteLocationData data = (FavoriteLocationData) marker.getTag();
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle(mContext.getString(R.string.dialog_msg_favorite_remove));
        ab.setPositiveButton(mContext.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DatabaseManager.getInstance(mContext).deleteFavoriteLocationData(data);
                        showFavoriteLocationMarkers(true);
                    }
                }).setNegativeButton(mContext.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        ab.setCancelable(false);
        ab.show();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick");
        if (!mIsStarted) {
            DataPref.DESTINATION_PLACE = "";
            mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.go_disable));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick");
        if (mIsStarted) {
            return false;
        }

        if (marker.getTag() == null) {
            Log.d(TAG, "tag is null");
            return false;
        }
        String image_url = "";
        if (marker.getTag() instanceof PlaceData) {
            Log.d(TAG, "tag is PlaceData");
            PlaceData placeData = (PlaceData) marker.getTag();
            image_url = placeData.getmImagePath();
            Glide.with(mContext).load(image_url).into(mImageView);
            DataPref.DESTINATION_PLACE = placeData.getmName();
            mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.go_enable));
            mDestinationLatitude = placeData.getmLatLng().latitude;
            mDestinationLongitude = placeData.getmLatLng().longitude;
        } else if (marker.getTag() instanceof FavoriteLocationData) {
            FavoriteLocationData favoriteLocationData = (FavoriteLocationData) marker.getTag();
            Log.d(TAG, "tag is FavoriteLocationData");
            image_url = favoriteLocationData.getmImagePath();
            DataPref.DESTINATION_PLACE = favoriteLocationData.getmName();
            Glide.with(mContext).load(image_url).into(mImageView);
            mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.go_enable));
            mDestinationLatitude = Double.parseDouble(favoriteLocationData.getmLatitude());
            mDestinationLongitude = Double.parseDouble(favoriteLocationData.getmLongitude());
        } else {
            return false;
        }

        //DataPref.DESTINATION_PLACE = Util.getGeocodeName(mContext, placeData.getmLatLng().latitude, placeData.getmLatLng().longitude);

        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d(TAG, "onMyLocationClick");
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
                double newLat = location.getLatitude();
                double newLon = location.getLongitude();
                if (newLat <= 0 || newLon <= 0) {
                    Log.e(TAG, "Location info is invalid");
                    return;
                }
                double distance = Util.distance(mCurrentLatitude, mCurrentLongitude, newLat, newLon, "kilometer");
                String disStr = String.format("%.2f", distance);
                distance = Double.parseDouble(disStr);
                sDistanceSum += distance;

                mCurrentLatitude = newLat;
                mCurrentLongitude = newLon;
                if (mIsFirst) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLatitude, mCurrentLongitude), 15));
                    mIsFirst = false;
                }
                if (mIsStarted) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLatitude, mCurrentLongitude), 15));
                    updateCurrentLocationMarker(mCurrentLatitude, mCurrentLongitude, sDistanceSum);
                }
                /*


                if(mIsStarted){
                    if (mCurrentPolyline != null) {
                        mCurrentPolyline.remove();
                    }
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.flat(false);
                    markerOptions.position(center);
                    markerOptions.title("CurrentLocation");
                    markerOptions.draggable(false);
                    Marker marker = mGoogleMap.addMarker(markerOptions);
                } else {

                }

                */

            }
        });
        mUiSettings = mGoogleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        enableMyLocation();
        initViews();
        initReceiver();

    }

    private void initReceiver() {
        mMainBroadCastReceiver = new MainBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_LOCATION_CHANGE_EVENT);
        registerReceiver(mMainBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        sDistanceSum = 0;
        sStartTime = 0;
        sTravelTime = 0;
        unregisterReceiver(mMainBroadCastReceiver);

    }

    private void setDirection(LatLng dest) {
        Log.d(TAG, "setDirection");
        LatLng current = new LatLng(mCurrentLatitude, mCurrentLongitude);
        GoogleDirection.withServerKey(Constant.KEY_PLACE)
                .from(current)
                .to(dest)
                .transitMode(TransportMode.DRIVING)
                .unit(Unit.METRIC)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Log.d(TAG, "onDirectionSuccess : " + direction.getStatus() + " , body : " + rawBody);
                        List<Route> routeList = direction.getRouteList();
                        if (routeList != null && routeList.size() > 0) {
                            Log.e(TAG, "route list : " + routeList.size());
                            Route route = routeList.get(0);
                            List<Leg> legList = route.getLegList();
                            Log.e(TAG, "leg list : " + legList.size());
                            if (legList != null && legList.size() > 0) {
                                Leg leg = route.getLegList().get(0);
                                List<Step> stepList = leg.getStepList();
                                if (stepList != null && stepList.size() > 0) {
                                    Log.e(TAG, "stepList list : " + stepList.size());
                                    Step step = stepList.get(0);
                                    LatLng start = step.getStartLocation().getCoordination();
                                    LatLng end = step.getEndLocation().getCoordination();
                                    ArrayList<LatLng> pointList = leg.getDirectionPoint();
                                    String travelMode = step.getTravelMode();
                                    ArrayList<LatLng> sectionList = leg.getSectionPoint();

                                    Info distanceInfo = step.getDistance();
                                    Info durationInfo = step.getDuration();
                                    String distance = distanceInfo.getText();
                                    String duration = durationInfo.getText();

                                    String maneuver = step.getManeuver();
                                    String instruction = step.getHtmlInstruction();

                                    Log.e(TAG, "travelMode:" + travelMode + ",distance:" + distance + ",duration:" + duration + ",maneuver:" + maneuver + ",instruction:" + instruction);

                                    ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline(mContext, directionPositionList, 5, Color.RED);
                                    mGoogleMap.addPolyline(polylineOptions);

                                }
                            }

                        }


                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }

    private void initViews() {
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        mStartPlace = findViewById(R.id.start_place);
        mDestinationPlace = findViewById(R.id.destination_place);
        mDistance = findViewById(R.id.distance);
        mTravelTime = findViewById(R.id.travel_time);
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_SEOUL, null);
        mAutocompleteView.setAdapter(mAdapter);
        mClear = findViewById(R.id.clear);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAutocompleteView.setText("");
            }
        });
        mImageView = findViewById(R.id.image);
        mImageButton = findViewById(R.id.image_btn);
        mImageView.setVisibility(View.GONE);
        mImageButton.setVisibility(View.GONE);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setVisibility(View.VISIBLE);
                mImageButton.setVisibility(View.GONE);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setVisibility(View.GONE);
                mImageButton.setVisibility(View.VISIBLE);
            }
        });
        mCarImageView = findViewById(R.id.car_image);

        mFavoriteButton = findViewById(R.id.favorite);
        mFavoriteButton.setChecked(false);
        mFavoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFavoriteLocationDataList = DatabaseManager.getInstance(mContext).getFavoriteLocationDataList();

                    if (mFavoriteLocationDataList.size() <= 0) {
                        Toast.makeText(mContext, mContext.getString(R.string.label_no_favorite), Toast.LENGTH_SHORT).show();
                    } else {
                        FavoriteLocationData fd = mFavoriteLocationDataList.get(0);
                        showFavoriteLocationMarkers(true);
                        LatLng center = new LatLng(Double.parseDouble(fd.getmLatitude()), Double.parseDouble(fd.getmLongitude()));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
                    }
                } else {
                    showFavoriteLocationMarkers(false);
                }
            }
        });
        mTaxiStop = findViewById(R.id.taxi_stop);
        mTaxiStop.setChecked(false);
        mTaxiStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    addTaxiStopMarker();
                    LatLng center = new LatLng(mCurrentLatitude, mCurrentLongitude);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
                } else {
                    removeTaxiStopMarker();
                }
            }
        });
        mStart = findViewById(R.id.start);
        mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.go_disable));
        mLocationWorkServiceIntent = new Intent(mContext, LocationWorkService.class);
        mStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {


                if ("".equalsIgnoreCase(DataPref.DESTINATION_PLACE)) {
                    Toast.makeText(mContext, mContext.getString(R.string.msg_destination_err), Toast.LENGTH_SHORT).show();
                    mStart.setChecked(false);
                } else {

                    if (checked) {
                        sStartTime = System.currentTimeMillis();
                        DataPref.START_PLACE = Util.getGeocodeName(mContext, mCurrentLatitude, mCurrentLongitude);
                        mDriveStartDialog = new DriveStartDialog(mContext, DataPref.START_PLACE, DataPref.DESTINATION_PLACE, sStartTime, IIDriveEvent);
                        mDriveStartDialog.show();
                        mCurrentLocationData.setmStartTime(String.valueOf(sStartTime));
                        mStartPlace.setText(String.format(mContext.getString(R.string.start_place), DataPref.START_PLACE));
                        mDestinationPlace.setText(String.format(mContext.getString(R.string.destination_place), DataPref.DESTINATION_PLACE));
                        mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.stop));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLatitude, mCurrentLongitude), 15));
                    } else {
                        mIsStarted = false;
                        sTravelTime = System.currentTimeMillis() - sStartTime;
                        //stopService(mLocationWorkServiceIntent);
                        mTravelTime.setText(String.format(mContext.getString(R.string.travel_time), Util.getTimeToMinute(sTravelTime)));
                        TaxiInfo taxiInfo = new TaxiInfo();
                        taxiInfo.setmStartPlace(DataPref.START_PLACE);
                        taxiInfo.setmDestinationPlace(DataPref.DESTINATION_PLACE);
                        taxiInfo.setmStartTime(String.valueOf(sStartTime));
                        taxiInfo.setmEndTime(String.valueOf(System.currentTimeMillis()));
                        taxiInfo.setmDistance(sDistanceSum);
                        taxiInfo.setmTravelTime((int) sTravelTime);
                        mTaxiDriveCompleteDialog = new TaxiDriveCompleteDialog(mContext, taxiInfo, TYPE_NEW);
                        mTaxiDriveCompleteDialog.show();
                        DataPref.DESTINATION_PLACE = "";
                        sStartTime = 0;
                        mCarImageView.clearAnimation();
                        mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.go_disable));
                        removeNotification();
                    }
                }

            }
        });

        if (mImageData != null) {
            mAutocompleteView.setText(mImageData.getmNameEng());
        }

    }

    private void removeNotification() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.deleteNotificationChannel(NOTI_CHANNEL_ID);
        }

    }

    private int mTimerCount = 0;
    private static final int MSG_TIMER_START = 0;
    private static final int MSG_TIMER_STOP = 1;
    private Handler mTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_TIMER_START:
                    mTimerCount = mTimerCount + 1;
                    mTravelTime.setText(String.format(mContext.getString(R.string.time), Util.getTimeFormatText3(mTimerCount)));
                    mDistance.setText(String.format(mContext.getString(R.string.distance), String.format("%.2f", sDistanceSum)));
                    if (mIsStarted) {
                        sendEmptyMessageDelayed(MSG_TIMER_START, 1000);
                    }
                    break;
                case MSG_TIMER_STOP:
                    //removeMessages(MSG_TIMER_START);
                    break;
            }

        }
    };

    IDriveEvent IIDriveEvent = new IDriveEvent() {
        @Override
        public void onClickStartEvent(boolean start) {
            if (start) {
                mCurrentLocationLatLngList.clear();
                mCarImageView.setVisibility(View.VISIBLE);
                mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.stop));
                mIsStarted = true;
                //startService(mLocationWorkServiceIntent);
                sDistanceSum = 0;
                mTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_START, 1000);
                startCarAnim();
            } else {
                mCarImageView.setVisibility(View.GONE);
                mStart.setBackgroundDrawable(mContext.getDrawable(R.drawable.go_disable));
                sStartTime = 0;
                DataPref.DESTINATION_PLACE = "";
            }
        }
    };

    private void startCarAnim() {
        int[] size = Util.getDisplaySize(mContext);
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.car_anim);
        mCarImageView.startAnimation(anim);
    }

    interface IDriveEvent {
        void onClickStartEvent(boolean start);
    }


    private void showFavoriteLocationMarkers(boolean b) {
        Log.d(TAG, "showFavoriteLocationMarkers : " + b + ",mFavoriteLocationDataList : " + mFavoriteLocationDataList.size());
        if (b) {
            // show
            for (int i = 0; i < mFavoriteLocationDataList.size(); i++) {
                final FavoriteLocationData data = mFavoriteLocationDataList.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.flat(false);
                double lat = Double.parseDouble(data.getmLatitude());
                double lon = Double.parseDouble(data.getmLongitude());
                markerOptions.position(new LatLng(lat, lon));
                markerOptions.title(data.getmName());
                markerOptions.draggable(false);

                mCustomMarkerLayout.setBackground(mContext.getDrawable(R.drawable.ic_marker_phone));
                mCustomMarkerTime.setVisibility(View.GONE);
                mCustomMarkerName.setVisibility(View.VISIBLE);
                mCustomMarkerAddress.setVisibility(View.VISIBLE);
                mCustomMarkerMemo.setVisibility(View.VISIBLE);
                mCustomMarkerName.setText(data.getmName());
                mCustomMarkerAddress.setText(data.getmAddress());
                mCustomMarkerMemo.setText(data.getmMemo());

                //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, mCustomMarkerViewForFavorite)));
                Marker marker = mGoogleMap.addMarker(markerOptions);
                marker.setTag(data);
                mFavoriteLocationDataMarkerList.add(marker);
            }
        } else {
            // hide
            for (int i = 0; i < mFavoriteLocationDataMarkerList.size(); i++) {
                Marker marker = mFavoriteLocationDataMarkerList.get(i);
                marker.remove();
            }
        }
    }

    private void removeTaxiStopMarker() {
        if (mTaxiMarkerList != null) {
            for (int i = 0; i < mTaxiMarkerList.size(); i++) {
                mTaxiMarkerList.get(i).remove();
            }
        }

    }

    private ArrayList<Marker> mTaxiMarkerList = new ArrayList<>();

    private void addTaxiStopMarker() {
        mTaxiMarkerList.clear();
        for (int i = 0; i < mTaxiLocationDataList.size(); i++) {
            TaxiLocationData data = mTaxiLocationDataList.get(i);
            double lat = Double.parseDouble(data.getmLatitude());
            double lnt = Double.parseDouble(data.getmLongitude());
            double distance = Util.distance(mCurrentLatitude, mCurrentLongitude, lat, lnt, "meter");
            //if (distance <= LIMIT_TAXI_DISTANCE) {
            addTaxiMarker(data);
            //}
        }
    }


    private void addTaxiMarker(TaxiLocationData data) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.flat(false);
        double lat = Double.parseDouble(data.getmLatitude());
        double lnt = Double.parseDouble(data.getmLongitude());
        markerOptions.position(new LatLng(lat, lnt));
        markerOptions.title(mContext.getString(R.string.taxi_stop));
        markerOptions.draggable(false);
        Marker marker = mGoogleMap.addMarker(markerOptions);
        mTaxiMarkerList.add(marker);
    }

    private void addMarker(PlaceData place) {
        //mGoogleMap.clear();
        for (int i = 0; i < mMarkerList.size(); i++) {
            Marker marker = mMarkerList.get(i);
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.flat(false);
        markerOptions.title(place.getmName());
        markerOptions.position(place.getmLatLng());
        markerOptions.draggable(false);

        Marker marker = mGoogleMap.addMarker(markerOptions);
        marker.setTag(place);

        mMarkerList.add(marker);

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                final CharSequence thirdPartyAttribution = places.getAttributions();
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                if (place != null) {

                    PlaceData placeData = new PlaceData();
                    placeData.setmName(place.getName().toString());
                    placeData.setmLatLng(place.getLatLng());
                    placeData.setmAddress(place.getAddress().toString());
                    //addMarker(placeData);
                    PlaceWorker placeWorker = new PlaceWorker(mContext, placeData);
                    placeWorker.setCompleteListener(placeSearchCompleteListener);
                    placeWorker.execute();
                    //setDirection(place.getLatLng());
                }

                Log.i(TAG, "Place details received: " + place.getName() + "," + place.getLatLng() + "," + place.getAddress() + "," + place.getWebsiteUri() + "," + place.getRating() + "," + place.getPriceLevel());
                if (thirdPartyAttribution != null) {
                    Log.i(TAG, "Place third attr: " + Html.fromHtml(thirdPartyAttribution.toString()));
                }
                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    public interface IPlaceSearchCompleteListener {
        public void onCompleted(PlaceSearchData data, PlaceData placeData);
    }

    IPlaceSearchCompleteListener placeSearchCompleteListener = new IPlaceSearchCompleteListener() {
        @Override
        public void onCompleted(PlaceSearchData data, PlaceData placeData) {
            if (data == null) {
                return;
            }
            addMarker(placeData);
            mPlaceSearchData = data;
            mAutocompleteView.clearFocus();
            mImageButton.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mPlaceSearchData.getmImageUrl()).into(mImageView);
            placeData.setmImagePath(mPlaceSearchData.getmImageUrl());
            mCurrentImageUrl = mPlaceSearchData.getmImageUrl();
            mCurrentImageDrawable = mImageView.getDrawable();
            mImageView.setFocusable(true);
            mImageView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mAutocompleteView.getWindowToken(), 0);
        }
    };


    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Util.checkLocationSetting(mContext);
        Util.checkNetworkStatus(mContext);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
    }

    class MainBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive : " + action);
            if (INTENT_LOCATION_CHANGE_EVENT.equalsIgnoreCase(action)) {
                double newLat = intent.getDoubleExtra(PREF_LATITUDE, -1);
                double newLon = intent.getDoubleExtra(PREF_LONGITUDE, -1);
                //updateCurrentLocationMarker(newLat, newLon);
            }
        }
    }

    private ArrayList<CurrentLocationData> mCurrentLocationList = new ArrayList<>();
    private ArrayList<LatLng> mCurrentLocationLatLngList = new ArrayList<>();

    private void updateCurrentLocationMarker(double newLat, double newLon, double distance) {

        mCurrentLocationData.setmLatitude(String.valueOf(newLat));
        mCurrentLocationData.setmLongitude(String.valueOf(newLon));
        mCurrentLocationData.setmName(Util.getGeocodeName(mContext, newLat, newLon));
        mCurrentLocationData.setmTime(Util.getTimeFormatText2(System.currentTimeMillis()));
        mCurrentLocationData.setmStart(DataPref.START_PLACE);
        mCurrentLocationData.setmDestination(DataPref.DESTINATION_PLACE);
        mCurrentLocationData.setmDistance((float) distance);
        DatabaseManager.getInstance(mContext).insertCurrentLocation(mCurrentLocationData);

        mCurrentLocationList.add(mCurrentLocationData);
        LatLng center = new LatLng(mCurrentLatitude, mCurrentLongitude);
        mCurrentLocationLatLngList.add(center);

        if (mIsStarted) {
            if (mCurrentLocationLatLngList.size() == 1) {
                mCustomMarkerLayoutForCurrent.setBackground(mContext.getDrawable(R.drawable.ic_marker_phone));
                mCustomMarkerTimeForCurrent.setVisibility(View.VISIBLE);
                mCustomMarkerTimeForCurrent.setText(String.format(mContext.getString(R.string.start_time), mCurrentLocationData.getmTime()));
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.flat(false);
            markerOptions.position(center);
            markerOptions.title(mCurrentLocationData.getmName() + "\n" + mCurrentLocationData.getmTime());
            markerOptions.draggable(false);
            if (mCurrentLocationLatLngList.size() == 1) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, mCustomMarkerViewForCurrent)));
            } else {
                BitmapDrawable bd = (BitmapDrawable) mContext.getDrawable(R.drawable.icon_circle);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap((bd.getBitmap())));
            }
            Marker marker = mGoogleMap.addMarker(markerOptions);
        }

        /*
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
        polylineOptions.addAll(mCurrentLocationLatLngList);
        mGoogleMap.addPolyline(polylineOptions);
        */
        if (mIsStarted) {
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15));
        }

        checkNotification();
    }

    private void checkNotification() {

        int distance = (int) Util.distance(mCurrentLatitude, mCurrentLongitude, mDestinationLatitude, mDestinationLongitude, "meter");
        Log.d(TAG, "checkNotification : " + distance);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (distance < LIMIT_DISTANCE) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int notifyID = 1;
                //send
                Notification notification = new Notification.Builder(mContext)
                        .setContentTitle("Safe Taxi")
                        .setContentText(String.format(mContext.getString(R.string.noti_destination), String.valueOf(distance)))
                        .setSmallIcon(R.drawable.safe_taxi)
                        .setChannelId(NOTI_CHANNEL_ID)
                        .build();
                mNotificationManager.notify(notifyID, notification);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        if (requestCode == REQ_CODE_GALLERY) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            if (uri != null) {
                String path = Util.getPath(mContext, uri);
                mTaxiDriveCompleteDialog.setImagePath(path);
                Log.e(TAG, "selected image path : " + path);
            }
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mTaxiDriveCompleteDialog != null && bitmap != null) {
                mTaxiDriveCompleteDialog.setImage(bitmap);
            }
        }

    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed");
        showQuitDialog();

    }

    private void showQuitDialog() {
        if (mIsStarted) {
            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
            ab.setTitle(mContext.getString(R.string.dialog_msg_quit));
            ab.setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            ab.setNegativeButton(mContext.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ab.show();
        } else {
            finish();
        }
    }
}
