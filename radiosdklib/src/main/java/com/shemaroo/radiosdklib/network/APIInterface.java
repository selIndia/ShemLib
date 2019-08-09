package com.shemaroo.radiosdklib.network;

import com.shemaroo.radiosdklib.apimodel.getslugdata.GetSlugDataRequsetParser;
import com.shemaroo.radiosdklib.apimodel.getslugdata.GetSlugDataResponseParser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("inner/music/getslugdata")
    Call<GetSlugDataResponseParser> getSlugData(@Body GetSlugDataRequsetParser requestParser);
}
