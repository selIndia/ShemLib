package com.shemaroo.radiosdklib.apimodel.getslugdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSlugDataRequsetParser {

    @SerializedName("Slug")
    @Expose
    private String slug;

    @SerializedName("UserID")
    @Expose
    private Integer userID;

    public GetSlugDataRequsetParser(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
