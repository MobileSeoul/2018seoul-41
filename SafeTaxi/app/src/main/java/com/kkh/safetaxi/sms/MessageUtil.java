package com.kkh.safetaxi.sms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.kkh.safetaxi.R;
import com.kkh.safetaxi.common.Util;
import com.kkh.safetaxi.data.TaxiInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.accessibility.AccessibilityEvent.MAX_TEXT_LENGTH;

public class MessageUtil {
    //http://miststory.tistory.com/80
    public static final String DASAN_NUMBER = "02120";
    private static final String KR_COUNTRY_NUMBER = "82";


    public static boolean sendMessage(Context context, String number, String description) {
        if (!Util.isEnabledUsim(context) || Util.isAirPlaneMode(context)) {
            return false;
        }
        SmsManager smsManager = SmsManager.getDefault();

        number = number.replace("-", "");
        if (number.startsWith(KR_COUNTRY_NUMBER)) {
            number = "+" + number;
        }

        if (description.length() > MAX_TEXT_LENGTH) {
            ArrayList<String> messagePart = smsManager.divideMessage(description);
            smsManager.sendMultipartTextMessage(number, null, messagePart, null, null);
        } else {
            smsManager.sendTextMessage(number, null, description, null, null);
        }

        return true;
    }

    public static void sendMessageWithImage(Context context, TaxiInfo data, String contents) {
        Bitmap bm = Util.getBitmapFromFile(context, data.getmImagePath());
        File file = new File(data.getmImagePath());
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Uri fileUri = FileProvider.getUriForFile(context, "com.kkh.safetaxi.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra("address", DASAN_NUMBER);
            intent.putExtra("sms_body", contents);
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.setType("image/png");
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.toast_image_file_err), Toast.LENGTH_SHORT).show();
        }
    }

    public static void startMessageThreadActivity(Context context, String number) {
        Uri uri = Uri.parse("smsto:" + number);
        final Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
