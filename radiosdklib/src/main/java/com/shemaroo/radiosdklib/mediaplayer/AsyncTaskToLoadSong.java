package com.shemaroo.radiosdklib.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.shemaroo.radiosdklib.listner.ISongsGettingDownloadedByAsync;
import com.shemaroo.radiosdklib.utils.UtilityClass;


public class AsyncTaskToLoadSong extends AsyncTask<Integer, String, String> {
    private Context context;
    private ISongsGettingDownloadedByAsync listner;

    public AsyncTaskToLoadSong(Context context) {
        this.context = context;
    }

    public AsyncTaskToLoadSong(Context context, ISongsGettingDownloadedByAsync listner) {
        this.context = context;
        this.listner = listner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        ((MainActivity) context).showProgress();
    }


    @Override
    protected String doInBackground(Integer... xyz) {
        Log.d("TAG", "TAG Tapped INOUT(IN)");
        PlayerConstants.SONG_PAUSED = false;
        PlayerConstants.SONG_NUMBER = xyz[0];

        boolean isServiceRunning = UtilityClass.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning) {
            Intent i = new Intent(context, SongService.class);
            context.startService(i);
        } else {
            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(listner != null) listner.songDownloadCompletedByAsync();
    }
}
