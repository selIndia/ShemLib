package com.shemaroo.radiosdklib.network;

import android.util.Log;

import com.shemaroo.radiosdklib.BuildConfig;
import com.shemaroo.radiosdklib.utils.Constants;
import com.shemaroo.radiosdklib.utils.UtilityClass;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    public static APIInterface getApiShemarooMusicLiveClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new DynamicHeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(UtilityClass.getShamarooMusicBaseUrlLive())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(APIInterface.class);
    }



    public static class DynamicHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            /*if (!"/posts".contains(originalRequest.url().toString())) {
                return chain.proceed(originalRequest);
            }*/
            String base64Test = UtilityClass.getBase64FromString("@ndro!d_@p!k@y_W_!fnybxS_b_5Y@4_9G!");
            String keyReq = "Android_|_|_|_android.apikey_|_|_|_"+base64Test;
            String token = UtilityClass.encryptHeader(keyReq);
            Request newRequest = originalRequest.newBuilder()
                    .header("ApiAuthorization", token)
                    .header("dFrom", "Android")
                    .build();

            Log.d("Headers",newRequest.headers().toString());

            return chain.proceed(newRequest);
        }

    }
}
