package com.shemaroo.radiosdklib.apimodel.getslugdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSlugDataResponseParser {

    private int db_uid;// param add for db

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Singers")
    @Expose
    private String singers;
    @SerializedName("SongUrl")
    @Expose
    private String songUrl;
    @SerializedName("SongDuration")
    @Expose
    private String songDuration;
    @SerializedName("RedirectTo")
    @Expose
    private String redirectTo;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("SongID")
    @Expose
    private int songID;
    @SerializedName("IsFavourite")
    @Expose
    private Boolean isFavourite;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingers() {
        return singers;
    }

    public void setSingers(String singers) {
        this.singers = singers;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public Boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    //-------------USE FOR DB-----------------------------
    public int getDbUid() {
        return db_uid;
    }

    public void setDbUid(int db_uid) {
        this.db_uid = db_uid;
    }
}
