package com.shemaroo.radiosdklib.apimodel.getslugdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSlugDataResponseParser {

    private int viewType = -1;

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Duration")
    @Expose
    private String duration;
    @SerializedName("ReleasedDate")
    @Expose
    private String releasedDate;
    @SerializedName("NoOfTrack")
    @Expose
    private Integer noOfTrack;
    @SerializedName("Items")
    @Expose
    private List<ItemSlugDataResponseParser> items = null;
    @SerializedName("AlbumID")
    @Expose
    private Integer albumID;
    @SerializedName("IsFavourite")
    @Expose
    private Boolean isFavourite;
    @SerializedName("IsUserPlaylist")
    @Expose
    private Boolean isUserPlaylist;
    @SerializedName("UserPlaylistID")
    @Expose
    private Integer userPlaylistID;

    @SerializedName("Message")
    @Expose
    private String message;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public Integer getNoOfTrack() {
        return noOfTrack;
    }

    public void setNoOfTrack(Integer noOfTrack) {
        this.noOfTrack = noOfTrack;
    }

    public List<ItemSlugDataResponseParser> getItems() {
        return items;
    }

    public void setItems(List<ItemSlugDataResponseParser> items) {
        this.items = items;
    }

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    public Boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Boolean getIsUserPlaylist() {
        return isUserPlaylist;
    }

    public void setIsUserPlaylist(Boolean isUserPlaylist) {
        this.isUserPlaylist = isUserPlaylist;
    }

    public Integer getUserPlaylistID() {
        return userPlaylistID;
    }

    public void setUserPlaylistID(Integer userPlaylistID) {
        this.userPlaylistID = userPlaylistID;
    }

    //-------------USE FOR VIEW-----------------------------
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
