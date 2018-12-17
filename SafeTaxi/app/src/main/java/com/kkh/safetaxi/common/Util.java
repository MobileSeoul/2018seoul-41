package com.kkh.safetaxi.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kkh.safetaxi.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.kkh.safetaxi.common.Constant.EMBASSY_DRAWABLE_LIST;

/**
 * Created by Administrator on 2018-08-14.
 */

public class Util {
    private static final String TAG = "[KKH]Util";

    public static boolean initDatabase(Context context, String name) {
        Log.d(TAG, "initDatabase name : " + name);
        final File dbfile = context.getDatabasePath(name);
        copyFile(dbfile);
        if (dbfile.exists()) {
            //dbfile.delete();
            return false;
        }
        AssetManager am = context.getAssets();
        InputStream is = null;

        //String sdcard_path = Environment.getExternalStorageDirectory() + "/"+PROJECT_NAME+"/" + DB_NAME_SUBWAY;
        FileOutputStream out = null;
        FileInputStream in = null;
        boolean isComplete = false;
        try {
            out = new FileOutputStream(dbfile);
            is = am.open(name);
            //in = new FileInputStream(sdcard_path);

            byte[] buffer = new byte[64 * 1024];

            int count;
            while ((count = is.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            isComplete = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    isComplete = false;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "db file exists : " + dbfile.exists());
        return isComplete;
    }

    public static final String FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SafeTaxi/";
    private static void copyFile(File file) {
        Log.e(TAG, "copyFile");
        File folder = new File(FILE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String file_path = FILE_PATH + "seoul_taxi.db";
        File newFile = new File(file_path);

        if (file != null && file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(file_path);
                int readcount = 0;
                byte[] buffer = new byte[1024];
                while ((readcount = fis.read(buffer, 0, 1024)) != -1) {
                    newfos.write(buffer, 0, readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getString(R.string.working));
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        if (dist < 0) {
            dist = dist * -1;
        }

        return dist;
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String getGeocodeName(Context context, double lat, double lon) {
        String name = "";
        if (lat <= 0 && lon <= 0) {
            return name;
        }
        Geocoder geocoder = new Geocoder(context);
        try

        {
            List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                name = address.getAddressLine(0);
            }
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        return name;
    }

    public static String getTimeFormatText(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-E-a HH:mm:ss ");
        return sdf.format(new Date(time));
    }

    public static String getTimeFormatText2(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-E-a-HH:mm");
        return sdf.format(new Date(time));
    }

    public static String getTimeFormatText3(long time) {
        // 4000초
        long hour = time / 60 / 60;
        long min = (time - hour * 3600) / 60;
        long sec = time - hour * 3600 - min * 60;

        String ret = "";
        if (hour > 0) {
            ret = hour + " hour " + min + " min " + sec + " sec";
        }

        if (hour <= 0 && min > 0) {
            ret = min + " min " + sec + " sec";
        }

        if (hour <= 0 && min <= 0) {
            ret = sec + " sec";
        }

        return ret;
    }

    public static String getTimeFormatText4(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
        return sdf.format(new Date(time));
    }

    public static String getTimeToMinute(long time) {
        int ret = (int) (time / 1000 / 60);

        return String.valueOf(ret);
    }

    public static String getPath(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static int mliToMinute(int mil) {
        return mil / 1000 / 60;
    }

    public static Bitmap getBitmapFromFile(Context context, String path) {
        BitmapDrawable drawable = (BitmapDrawable) context.getDrawable(R.drawable.safe_taxi_icon);
        Bitmap bitmap = drawable.getBitmap();
        try {
            File file = new File(path);

            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (NullPointerException e) {
            return drawable.getBitmap();
        }
        return bitmap;
    }

    public static Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public static int[] getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new int[]{width, height};
    }

    public static boolean isEnabledUsim(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        String number = tMgr.getLine1Number();
        if (number == null) {
            return false;
        }
        return true;
    }

    public static boolean isAirPlaneMode(Context context) {
        int res = Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
        return res == 1;
    }

    public static String getAppVersion(Context context) {
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pinfo.versionName;
        return versionName;
    }


    public static Drawable getEmbassyImage(Context context, String name) {
        int id = EMBASSY_DRAWABLE_LIST.get(name);
        return context.getDrawable(id);
    }

    public static boolean isLocationServiceOn(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNetworkOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                return ni.isConnected();
            }
        }

        return false;
    }

    public static void checkNetworkStatus(final Context context) {
        if (!Util.isNetworkOn(context)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle(context.getString(R.string.dialog_msg_network));
            ab.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //((Activity) context).finish();
                    Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    context.startActivity(intent);
                }
            });
            ab.show();
        }
    }

    public static void checkLocationSetting(final Context context) {
        if (!Util.isLocationServiceOn(context)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle(context.getString(R.string.noti_location_error));
            ab.setPositiveButton(context.getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    }
            );
            ab.setCancelable(false);
            ab.show();
        }
    }
}
