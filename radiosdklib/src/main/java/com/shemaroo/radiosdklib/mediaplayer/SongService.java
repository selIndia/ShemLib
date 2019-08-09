package com.shemaroo.radiosdklib.mediaplayer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shemaroo.radiosdklib.R;
import com.shemaroo.radiosdklib.activity.PlayerActivity;
import com.shemaroo.radiosdklib.apimodel.getslugdata.ItemSlugDataResponseParser;
import com.shemaroo.radiosdklib.receivers.NotificationBroadcast;
import com.shemaroo.radiosdklib.utils.UtilityClass;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Timer;
import java.util.TimerTask;


public class SongService extends Service implements /*MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,*/ AudioManager.OnAudioFocusChangeListener {

    String LOG_CLASS = "SongService";
//    private MediaPlayer mp;
    int NOTIFICATION_ID = 1111;
    public static final String NOTIFY_PREVIOUS = "com.shemaroo.shemmusicapp.previous";
    public static final String NOTIFY_DELETE = "com.shemaroo.shemmusicapp.delete";
    public static final String NOTIFY_PAUSE = "com.shemaroo.shemmusicapp.pause";
    public static final String NOTIFY_PLAY = "com.shemaroo.shemmusicapp.play";
    public static final String NOTIFY_NEXT = "com.shemaroo.shemmusicapp.next";
    public static final String NOTIFY_LAUNCHAPP = "com.shemaroo.shemmusicapp.thumbnail";
    public static final String NOTIFY_BACKWARD = "com.shemaroo.shemmusicapp.backward";
    public static final String NOTIFY_FORWARD = "com.shemaroo.shemmusicapp.forward";
    public static final String NOTIFICATION_CHANNEL_ID = "com.shemaroo.shemmusicapp.channelID";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    ItemSlugDataResponseParser data = null;
    String songPath;

    private int seekForwardTime = 10000;
    private int seekBackwardTime = 10000;

    private ComponentName remoteComponentName;
    private RemoteControlClient remoteControlClient;
    AudioManager audioManager;
    Bitmap mDummyAlbumArt;
    private static Timer timer;
    private static boolean currentVersionSupportBigNotification = false;
    private static boolean currentVersionSupportLockScreenControls = false;
    private boolean mServiceInUse = false;
    TrackSelector trackSelector;
    LoadControl loadControl;

    private static final CookieManager DEFAULT_COOKIE_MANAGER;
    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }


    @Override
    public void onCreate() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        loadControl = new DefaultLoadControl();

        PlayerConstants.exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);
        PlayerConstants.exoPlayer.addListener(listener);
        currentVersionSupportBigNotification = UtilityClass.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilityClass.currentVersionSupportLockScreenControls();
        timer = new Timer();
       /* mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Controls.nextControl(getApplicationContext());
            }
        });*/
        super.onCreate();

        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
    }


    private class MainTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (PlayerConstants.exoPlayer != null) {
                int progress = (int) (long) (PlayerConstants.exoPlayer.getCurrentPosition() * 100) / (int) (long) PlayerConstants.exoPlayer.getDuration();
                Integer i[] = new Integer[3];
                i[0] = (int) PlayerConstants.exoPlayer.getCurrentPosition();
                i[1] = (int) PlayerConstants.exoPlayer.getDuration();
                i[2] = progress;
                try {
                    PlayerConstants.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, i));
                } catch (Exception e) {
                }
            }
        }
    };

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0) {
                data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
            }
            if (currentVersionSupportLockScreenControls) {
                RegisterRemoteClient();
            }
            songPath = data.getSongUrl();
            playSong(songPath, data);
            newNotification();

            PlayerConstants.SONG_CHANGE_HANDLER = new Handler(new Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
                    songPath = data.getSongUrl();
                    newNotification();
                    try {
                        setCurrentPlayingValues(data);
                        playSong(songPath, data);

                        if (PlayerActivity.isActivityVisible() == true)
                            PlayerActivity.changeUI();
               /* if (FavoriteActivity.isActivityVisible() == true)
                    FavoriteActivity.changeUI();
                if (AudioPlayerActivity.isActivityVisible() == true)
                    AudioPlayerActivity.changeUI();*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            PlayerConstants.PLAY_PAUSE_HANDLER = new Handler(new Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    String message = (String) msg.obj;
                    if (message.equalsIgnoreCase(getResources().getString(R.string.play))) {
                        PlayerConstants.SONG_PAUSED = false;
                        PlayerConstants.isPlaying = true;
                        if (currentVersionSupportLockScreenControls) {
                            remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
                        }
                        PlayerConstants.exoPlayer.setPlayWhenReady(true);
                        newNotification();
                    } else if (message.equalsIgnoreCase(getResources().getString(R.string.pause))) {
                        PlayerConstants.SONG_PAUSED = true;
                        PlayerConstants.isPlaying = false; // TODO added Fore test to stop animate bars
                        if (currentVersionSupportLockScreenControls) {
                            remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
                        }
                        PlayerConstants.exoPlayer.setPlayWhenReady(false);
                        newNotification();
                    } else if (message.split("#")[0].equalsIgnoreCase(getResources().getString(R.string.seek))) {
                        // forward song
                        PlayerConstants.exoPlayer.seekTo(Integer.parseInt(message.split("#")[1]));

                    }

                    //TODO :  this is commented cause of seekchange should not give notification
//                    newNotification();
                    try {
                        if (PlayerActivity.isActivityVisible() == true)
                            PlayerActivity.changeUI();
                    } catch (Exception e) {
                    }
                    Log.d("TAG", "TAG Pressed: " + message);

                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @SuppressLint("NewApi")
    private void newNotification() {
        NotificationCompat.Builder notificationBuilder = null;
        Notification notification;
        String songName = "", albumName = "";
        if (PlayerConstants.SONGS_LIST.size() > PlayerConstants.SONG_NUMBER) {
            songName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
            albumName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingers();
        }
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = startMyOwnForeground();
        } else {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        }
        notification = notificationBuilder
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(songName).build();
        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if (currentVersionSupportBigNotification) {
            notification.bigContentView = expandedView;
        }

        try {
            NotificationTarget notificationTargetSmallNotification = new NotificationTarget(
                    getApplicationContext(),
                    R.id.imageViewAlbumArt,
                    notification.contentView,
                    notification,
                    NOTIFICATION_ID);
//            Bitmap albumArt = UtilityClass.getBitmapFromURL(getApplicationContext(), PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl()!= null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() :PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ");
//            Bitmap albumArt = getBitmapFromURL(PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl()!= null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() :PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ");
//            if (albumArt != null) {
//                notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ")
                    .placeholder(R.drawable.default_img)
                    .into(notificationTargetSmallNotification);
            if (currentVersionSupportBigNotification) {
                NotificationTarget notificationTargetBigNotification = new NotificationTarget(
                        getApplicationContext(),
                        R.id.imageViewAlbumArt,
                        notification.bigContentView,
                        notification,
                        NOTIFICATION_ID);
//                    notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ")
                        .placeholder(R.drawable.default_img)
                        .into(notificationTargetBigNotification);
            }
//            } else {
//                notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_img);
//                if (currentVersionSupportBigNotification) {
//                    notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_img);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PlayerConstants.SONG_PAUSED) {
            notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

            if (currentVersionSupportBigNotification) {
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.GONE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            }
        } else {
            notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);

            if (currentVersionSupportBigNotification) {
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            }
        }

        notification.contentView.setTextViewText(R.id.textSongName, songName);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumName);
        if (currentVersionSupportBigNotification) {
            notification.bigContentView.setTextViewText(R.id.textSongName, songName);
            notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID, notification);
    }

    @SuppressLint("NewApi")
    private NotificationCompat.Builder startMyOwnForeground() {

        String channelName = "My Background Service";
        @SuppressLint({"NewApi", "LocalSuppress"})
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null)
            manager.createNotificationChannel(chan);

        return new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
    }

    public void setListeners(RemoteViews view) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);
        Intent backward = new Intent(NOTIFY_BACKWARD);
        Intent forward = new Intent(NOTIFY_FORWARD);
        Intent thumbnail = new Intent(NOTIFY_LAUNCHAPP);

        delete.setClass(getApplicationContext(), NotificationBroadcast.class);
        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        pause.setClass(getApplicationContext(), NotificationBroadcast.class);
        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        play.setClass(getApplicationContext(), NotificationBroadcast.class);
        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);
    }

    @Override
    public void onDestroy() {
        if (PlayerConstants.exoPlayer != null) {
            PlayerConstants.exoPlayer.stop();
            PlayerConstants.isPlaying = false;
            PlayerConstants.CURRENT_PLAYING_ID = 0;

            PlayerConstants.exoPlayer = null;
        }

        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void playSong(String songPath, ItemSlugDataResponseParser data) {
        setCurrentPlayingValues(data);
        new LoadSong().execute();

    }

    @SuppressLint("NewApi")
    private void RegisterRemoteClient() {
        remoteComponentName = new ComponentName(getApplicationContext(), new NotificationBroadcast().ComponentName());
        try {
            if (remoteControlClient == null) {
                audioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                audioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        } catch (Exception ex) {
        }
    }

    @SuppressLint("NewApi")
    private void UpdateMetadata(ItemSlugDataResponseParser data) {
        if (remoteControlClient == null)
            return;
        MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, data.getSingers());
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, data.getTitle());
        if (mDummyAlbumArt == null) {
            mDummyAlbumArt = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
        }
        metadataEditor.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, mDummyAlbumArt);
        metadataEditor.apply();
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
    }

    class LoadSong extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setCurrentPlayingValues(data);
        }

        @Override
        protected String doInBackground(String... a) {
            File file1 = new File(getExternalCacheDir().getAbsolutePath() + "/" + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle() + ".mp3");
            String s = null;
            System.out.println("Fie path : " + file1.toString());
            if (!file1.exists()) {
                if (UtilityClass.isConnectedToNetwork(getApplicationContext()) == true) {
                    s = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSongUrl();
                }

            } else {
                file1.setReadable(true, false);
                s = file1.getAbsolutePath();
            }

            try {
                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                        Util.getUserAgent(getApplicationContext(), getResources().getString(R.string.app_name)), bandwidthMeter);
// Produces Extractor instances for parsing the media data.
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
                MediaSource videoSource;
//                if (s.contains(Constants.RADIO_URL_FORMAT)) {
                    videoSource = new HlsMediaSource(Uri.parse(s), dataSourceFactory, 5, null, null);
               /* } else {
                    videoSource = new ExtractorMediaSource(Uri.parse(s),
                            dataSourceFactory, extractorsFactory, null, null);
                }*/

                // Prepare the player with the source.
                System.out.println("MUSIC TO PLAY :" + videoSource);
                PlayerConstants.exoPlayer.prepare(videoSource);

                PlayerConstants.exoPlayer.setPlayWhenReady(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (currentVersionSupportLockScreenControls) {
                // radio should not add in recently played
               /* if (!data.getSongUrl().contains(Constants.RADIO_URL_FORMAT)) {
                    // to move first this song
                    if (!db.isRecentlyPlayedSongAlreadyAdded(data.getSongID())) {
                        db.insertRecentlyPlayedSong(data);
                    }
                }*/
                UpdateMetadata(data);
                remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
            }


            timer.scheduleAtFixedRate(new MainTask(), 0, 100);
            try {
                if (PlayerActivity.isActivityVisible() == true)
                    PlayerActivity.changeUI();
            } catch (Exception e) {
            }

            super.onPostExecute(s);
        }
    }



    SimpleExoPlayer.DefaultEventListener listener = new ExoPlayer.DefaultEventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == SimpleExoPlayer.STATE_ENDED) {
                onCompletion();
            }

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }


        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };

    private void onCompletion() {
//        Controls.nextControl(getApplicationContext());
    }

    public void setCurrentPlayingValues(ItemSlugDataResponseParser data) {
        PlayerConstants.isPlaying = true;
        PlayerConstants.CURRENT_PLAYING_SONGNAME = data.getTitle();
        PlayerConstants.CURRENT_PLAYING_ARTIST = data.getSingers();
//        PlayerConstants.CURRENT_PLAYING_IMAGE_URL = data.getImageUrl() != null ? data.getImageUrl() : "";
        /*if (PlayerConstants.CURRENT_PLAYING_ALBUMDATA != null)
            PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " ";
        else PlayerConstants.CURRENT_PLAYING_IMAGE_URL = "";*/
        PlayerConstants.CURRENT_PLAYING_IMAGE_URL = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() != null ? PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getImageUrl() : PlayerConstants.CURRENT_PLAYING_ALBUMDATA != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() != null ? PlayerConstants.CURRENT_PLAYING_ALBUMDATA.getImageUrl() : " " : " ";
        PlayerConstants.CURRENT_PLAYING_ITEMSONG = data;

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
        Log.e("stopservice", "stopServices");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onTaskRemoved(rootIntent);
    }


}
