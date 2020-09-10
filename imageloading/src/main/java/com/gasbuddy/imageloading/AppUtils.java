package com.gasbuddy.imageloading;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    public static final String UNSPLASH_ACCESS_KEY = "1p9ERLclx-R8eXzCaJmjHrm7nVr0ITqHqa-wrpLoox8";
    public static final String UNSPLASH_SECRET_KEY = "hV4KkcMmitu84ZzCdTthUBx9bvsorusjtwAWVE-FW-Y";

    public static int SCREEN_HEIGHT, SCREEN_WIDTH;

//    https://api.unsplash.com/photos?page=1&per_page=30
//    Authorization Client-ID 1p9ERLclx-R8eXzCaJmjHrm7nVr0ITqHqa-wrpLoox8

//    https://api.unsplash.com/photos/?client_id=1p9ERLclx-R8eXzCaJmjHrm7nVr0ITqHqa-wrpLoox8

    //    https://api.unsplash.com/photos/YNliXm_hMn8
//    https://www.googleapis.com/books/v1/volumes?q=cars

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo _activeNetwork = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return _activeNetwork != null && _activeNetwork.isConnectedOrConnecting();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void getHeightAndWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        SCREEN_HEIGHT = displaymetrics.heightPixels;
        SCREEN_WIDTH = displaymetrics.widthPixels;
    }

    public static void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formattedDateFromString(String inputDate) {
        String inputFormat, outputFormat;
//        inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        inputFormat = "yyyy-MM-dd'T'HH:mm:ss";
        outputFormat = "EEE, MMM d, yyyy";
        Date parsed;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
            outputDate = inputDate;
        }

        return outputDate;

    }

    public static void hideSoftKeyboard(Activity act) {
        try {
            if (act.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }
    }


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

}
