package com.shemaroo.radiosdklib.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shemaroo.radiosdklib.R;
import com.shemaroo.radiosdklib.apimodel.getslugdata.GetSlugDataRequsetParser;
import com.shemaroo.radiosdklib.apimodel.getslugdata.GetSlugDataResponseParser;
import com.shemaroo.radiosdklib.apimodel.getslugdata.ItemSlugDataResponseParser;
import com.shemaroo.radiosdklib.listner.ISongsGettingDownloadedByAsync;
import com.shemaroo.radiosdklib.mediaplayer.AsyncTaskToLoadSong;
import com.shemaroo.radiosdklib.mediaplayer.Controls;
import com.shemaroo.radiosdklib.mediaplayer.PlayerConstants;
import com.shemaroo.radiosdklib.mediaplayer.SongService;
import com.shemaroo.radiosdklib.network.APIClient;
import com.shemaroo.radiosdklib.network.APIInterface;
import com.shemaroo.radiosdklib.utils.Constants;
import com.shemaroo.radiosdklib.utils.UtilityClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerActivity extends BaseActivity implements ISongsGettingDownloadedByAsync, View.OnClickListener {
//    private Unbinder unbinder;
    APIInterface apiInterfaceLive;
    private static boolean activityVisible;
    static Context mContext;

    public static TextView lblTitleTrack, lblArtistName, lblSongTitle;
    public static ImageView imgAlbumMain, btnPlay, btnPause, imgBack;
    public static SeekBar progressPlayedDuration;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_player);
    }

    @Override
    protected void initializeComponents() {
//        unbinder = ButterKnife.bind(this);
        apiInterfaceLive = APIClient.getApiShemarooMusicLiveClient();
        mContext = UtilityClass.getmContext();
        lblTitleTrack = findViewById(R.id.lblTitleTrack);
        lblSongTitle = findViewById(R.id.lblSongTitle);
        lblArtistName = findViewById(R.id.lblArtistName);

        imgAlbumMain = findViewById(R.id.imgAlbumMain);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        imgBack = findViewById(R.id.imgBack);
        progressPlayedDuration = findViewById(R.id.progressPlayedDuration);
        callGetAlbumBySlugNewAPI();
    }

    private void callGetAlbumBySlugNewAPI() {
//        ((MainActivity) getActivity()).showProgress();
        GetSlugDataRequsetParser reqObject = new GetSlugDataRequsetParser("radio");
        reqObject.setUserID(0);
        if (UtilityClass.isConnectedToNetwork(mContext)) {
            Call<GetSlugDataResponseParser> call = apiInterfaceLive.getSlugData(reqObject);
            call.enqueue(new Callback<GetSlugDataResponseParser>() {
                @Override
                public void onResponse(Call<GetSlugDataResponseParser> call, Response<GetSlugDataResponseParser> response) {
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getItems() != null && response.body().getItems().size() > 0) {
                                if (!isFinishing()) {
                                    ItemSlugDataResponseParser tempModel = response.body().getItems().get(0);
                                    loadSong(tempModel);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetSlugDataResponseParser> call, Throwable t) {
                    UtilityClass.showShortToast(mContext, Constants.API_NETWORK_ISSUE_ERROR_MESSAGE);
                    String errorMessage = ((Exception) t).getMessage();
                }
            });
        } else {
            UtilityClass.showShortToast(mContext, Constants.INTERNET_NOT_WORKING_ERROR_MESSAGE);
        }
    }

    private void loadSong(ItemSlugDataResponseParser tempModel) {
        PlayerConstants.SONGS_LIST.clear();
        PlayerConstants.SONGS_LIST.add(tempModel);
        PlayerConstants.CURRENT_PLAYING_IMAGE_URL = tempModel.getImageUrl() != null ? tempModel.getImageUrl() : "";
        PlayerConstants.CURRENT_PLAYING_ITEMSONG = tempModel;

        int CURRENT_PLAYING_SONG_POSITION = 0;
        new AsyncTaskToLoadSong(mContext, this).execute(CURRENT_PLAYING_SONG_POSITION);
    }

    @Override
    protected void setListeners() {
        imgBack.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayerActivity.activityResumed();
        boolean isServiceRunning = UtilityClass.isServiceRunning(SongService.class.getName(), mContext);
        if (isServiceRunning) {
            updateUI();
        }
        changeButton();

        PlayerConstants.PROGRESSBAR_HANDLER = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Integer i[] = (Integer[]) msg.obj;
                   /* textBufferDuration.setText(JsonUtils.getDuration(i[0]));
                    textDuration.setText(JsonUtils.getDuration(i[1]));
                    //  progressBar.setProgress(i[2]);
                    // seekBar.setMax(UtilFunctions.getDuration(i[1]));
                    totalDuration=i[1];*/
                progressPlayedDuration.setProgress(progressPlayedDuration.getMax());
//                progressPlayedDuration.setEnabled(false);
                }
        };
    }

    public static void changeUI() {
        updateUI();
        changeButton();
    }

    private static void updateUI() {
        try {
            lblTitleTrack.setText(PlayerConstants.CURRENT_PLAYING_SONGNAME);
            lblSongTitle.setText(PlayerConstants.CURRENT_PLAYING_SONGNAME);
            lblSongTitle.setSelected(true);
            String composer = null;
            lblArtistName.setText(PlayerConstants.CURRENT_PLAYING_CATEGORY_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set Image
        System.out.println("IMAGE VALUE is -AUDIO PLAYER:" + PlayerConstants.CURRENT_PLAYING_IMAGE_URL);
        Glide.with(mContext)
                .load(PlayerConstants.CURRENT_PLAYING_IMAGE_URL).fitCenter().placeholder(R.drawable.default_img)
                .into(imgAlbumMain);
    }

    public static void changeButton() {
        if (PlayerConstants.SONG_PAUSED) {
            btnPause.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
        } else {
            btnPause.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        }
    }

    protected void onPause() {
        super.onPause();
        PlayerActivity.activityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
    }

    @Override
    public void songDownloadCompletedByAsync() {
        changeUI();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnPause) {
            Controls.pauseControl(mContext);
            btnPause.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
        } else if (i == R.id.btnPlay) {
            Controls.playControl(mContext);
            btnPause.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        } else if (i == R.id.imgBack) {
            this.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
