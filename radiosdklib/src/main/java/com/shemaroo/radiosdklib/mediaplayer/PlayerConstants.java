package com.shemaroo.radiosdklib.mediaplayer;

import android.os.Handler;

import com.shemaroo.radiosdklib.apimodel.getslugdata.GetSlugDataResponseParser;
import com.shemaroo.radiosdklib.apimodel.getslugdata.ItemSlugDataResponseParser;
import com.google.android.exoplayer2.SimpleExoPlayer;
import java.util.ArrayList;

public class PlayerConstants {

	public static Boolean isPlaying = false, isFav = false;
	public  static int CURRENT_PLAYING_ID=0;
	public  static String CURRENT_PLAYING_SONGNAME="";
	public  static String CURRENT_PLAYING_ARTIST="";
	public  static String CURRENT_PLAYING_IMAGE_URL="";
	public  static String CURRENT_PLAYING_CATEGORY_ID="";
	public  static String CURRENT_PLAYING_CATEGORY_NAME="";
	public  static String CURRENT_ACTIVITY="mainactivity";
	public  static ItemSlugDataResponseParser CURRENT_PLAYING_ITEMSONG;
	public  static GetSlugDataResponseParser CURRENT_PLAYING_ALBUMDATA;
	//List of Songs
	public static ArrayList<ItemSlugDataResponseParser> SONGS_LIST = new ArrayList<ItemSlugDataResponseParser>();
	//song number which is playing right now from SONGS_LIST
	public static int SONG_NUMBER = 0;
	//song is playing or paused
	public static boolean SONG_PAUSED = true;
	//song changed (next, previous)
	public static boolean SONG_CHANGED = false;
	//handler for song changed(next, previous) defined in service(SongService)
	public static Handler SONG_CHANGE_HANDLER;
	//handler for song play/pause defined in service(SongService)
	public static Handler PLAY_PAUSE_HANDLER;
	//handler for showing song progress defined in Activities(MainActivity, AudioPlayerActivity)
	public static Handler PROGRESSBAR_HANDLER;

	public static String SONG_TITLE;
	public static String SONG_IMAGE;
	public static String SONG_IMAGEHQ;
	public static String SONG_ALBUM;

	public static SimpleExoPlayer exoPlayer;
}
