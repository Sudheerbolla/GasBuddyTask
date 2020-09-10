package com.gasbuddy.utils.wsutils;

import com.gasbuddy.models.PhotoDetails;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WSInterface {

    @GET("photos")
    Call<List<PhotoDetails>> getPhotosList(@Query(value = "page", encoded = false) int page, @Query(value = "per_page", encoded = false) int per_page);

    @GET("photos/{photo_id}")
    Call<PhotoDetails> getPhotoItemDetails(@Path(value = "photo_id", encoded = true) String photoId);

    @GET("volumes")
    Call<JsonObject> getPhotosListGoogle(@Query(value = "q", encoded = false) String query);

    @GET("volumes/{id}")
    Call<PhotoDetails> getPhotosItemDetailsGoogle(@Path(value = "id", encoded = false) String id);
}