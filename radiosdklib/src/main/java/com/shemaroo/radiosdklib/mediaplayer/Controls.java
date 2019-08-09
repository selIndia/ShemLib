package com.shemaroo.radiosdklib.mediaplayer;

import android.content.Context;

import com.shemaroo.radiosdklib.R;
import com.shemaroo.radiosdklib.utils.UtilityClass;

public class Controls {
    static String LOG_CLASS = "Controls";

    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }
    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));
    }

   /* public static void backwardControl(Context context) {
        sendMessage(context.getResources().getString(R.string.backward));
    }

    public static void forwardControl(Context context) {
        sendMessage(context.getResources().getString(R.string.forward));
    }



    public static void repeatControl(Context context) {
        sendMessage(context.getResources().getString(R.string.repeat));
    }
    public static void shuffleControl(Context context) {
        sendMessage(context.getResources().getString(R.string.shuffle));
    }*/

    public static void seekControl(Context context, int duration) {
        sendMessage(context.getResources().getString(R.string.seek) + "#" + duration);
    }


    /*public static void nextControl(Context context) {
        boolean isServiceRunning = UtilityClass.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LIST.size() - 1)) {
                PlayerConstants.SONG_NUMBER++;

//				PlayerConstants.CURRENT_PLAYING_ID=Integer.parseInt(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getDbUid());
                PlayerConstants.CURRENT_PLAYING_SONGNAME = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
                PlayerConstants.CURRENT_PLAYING_ARTIST = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingers();
//              PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : " ";
//                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ";
                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : PlayerConstants.CURRENT_PLAYING_ALBUMDATA != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ": " ";
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_ID=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryId();
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_NAME=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryName();
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = 0;
//				PlayerConstants.CURRENT_PLAYING_ID=Integer.parseInt(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getDbUid());
                PlayerConstants.CURRENT_PLAYING_SONGNAME = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
                PlayerConstants.CURRENT_PLAYING_ARTIST = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingers();
//              PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : " ";
//                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ";
                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : PlayerConstants.CURRENT_PLAYING_ALBUMDATA != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ": " ";
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_ID=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryId();
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_NAME=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryName();
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
        // TODo To change Equlizer postion after next Play Song
        SongsListAdapter.CURRENT_PLAYING_SONG_POSITION++;
        MainActivity.changeButton();
    }

    public static void previousControl(Context context) {
        boolean isServiceRunning = UtilityClass.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER > 0) {
                PlayerConstants.SONG_NUMBER--;
//				PlayerConstants.CURRENT_PLAYING_ID=Integer.parseInt(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getDbUid());
                PlayerConstants.CURRENT_PLAYING_SONGNAME = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
                PlayerConstants.CURRENT_PLAYING_ARTIST = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingers();
//              PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : " ";
//                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ";
                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : PlayerConstants.CURRENT_PLAYING_ALBUMDATA != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ": " ";
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_ID=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryId();
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_NAME=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryName();
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                ;
            } else {
                PlayerConstants.SONG_NUMBER = PlayerConstants.SONGS_LIST.size() - 1;
//				PlayerConstants.CURRENT_PLAYING_ID=Integer.parseInt(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getDbUid());
                PlayerConstants.CURRENT_PLAYING_SONGNAME = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
                PlayerConstants.CURRENT_PLAYING_ARTIST = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingers();
//              PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : " ";
//                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ";
                PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : PlayerConstants.CURRENT_PLAYING_ALBUMDATA != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ": " ";
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_ID=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryId();
//				PlayerConstants.CURRENT_PLAYING_CATEGORY_NAME=PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getCategoryName();
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
        // TODo To change Equlizer postion after next Play Song
        SongsListAdapter.CURRENT_PLAYING_SONG_POSITION--;
        MainActivity.changeButton();
    }*/

    private static void sendMessage(String message) {
        try {
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
        }
    }
}
