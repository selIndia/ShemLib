package com.shemaroo.radiosdklib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.shemaroo.radiosdklib.activity.PlayerActivity;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UtilityClass {
    public static long currentProgress = 0;
    public static Context mContext;

    public static String getShamarooMusicBaseUrlLive() {
        /*if(BuildConfig.DEBUG){
            return Constants.SAVEBARCODE_BASEURL_UAT;
        }else{
            return Constants.SAVEBARCODE_BASEURL_LIVE;
        }*/
        return Constants.SHEMAROO_MUSIC_BASE_URL_LIVE;
    }

    public static boolean isConnectedToNetwork(Context mCtx) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)) {
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }

    public static void showShortToast(Context ctx, String message) {
        Toast.makeText(ctx, "" + message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isServiceRunning(String serviceName, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }

    public static boolean currentVersionSupportLockScreenControls() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return true;
        }
        return false;
    }

    public static void startPlayer(Activity currentActivity){
        Intent intent = new Intent(currentActivity, PlayerActivity.class);
        currentActivity.startActivity(intent);
    }
    public static Context getmContext() {
        return mContext;
    }

    public static void setAppLicationContext(Context mContext) {
        UtilityClass.mContext = mContext;
    }

    @SuppressLint("NewApi")
    public static String getBase64FromString(String pass){
        return Base64.getEncoder().encodeToString(pass.getBytes());
    }

    @SuppressLint("NewApi")
    public static String encryptHeader(String plainText) {
        String encryptedText = "";
        final String encryptionKey = "bQeThWmZq4t6w9zS";
        final String characterEncoding = "UTF-8";
        final String cipherTransformation = "AES/CBC/PKCS5PADDING";
        final String aesEncryptionAlgorithem = "AES";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(characterEncoding));
            @SuppressLint({"NewApi", "LocalSuppress"}) Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception E) {
            System.err.println("Encrypt Exception : " + E.getMessage());
        }
        return encryptedText;
    }

}
