package com.gasbuddy.utils.wsutils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;

import com.gasbuddy.BaseApplication;
import com.gasbuddy.imageloading.HttpGetRequest;
import com.gasbuddy.imageloading.IParserListener;
import com.gasbuddy.imageloading.WSUtils;
import com.gasbuddy.models.PhotoDetails;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchImageDetails extends BaseObservable {

    private MutableLiveData<PhotoDetails> photoDetailsMutable = new MutableLiveData<>();

    public MutableLiveData<PhotoDetails> getPhotoDetails() {
        return photoDetailsMutable;
    }

    public void fetchPhotoDetails(String id, String networkMethod, String source) {
        if (networkMethod.equalsIgnoreCase("Retrofit")) {
            Callback<PhotoDetails> callback = new Callback<PhotoDetails>() {
                @Override
                public void onResponse(@NonNull Call<PhotoDetails> call, Response<PhotoDetails> response) {
                    photoDetailsMutable.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<PhotoDetails> call, @NonNull Throwable t) {
                    Log.e("api error", t.getMessage(), t);
                }

            };
            if (source.equalsIgnoreCase("unsplash")) {
                BaseApplication.getInstance().getWsClientListenerUnsplash().getPhotoItemDetails(id).enqueue(callback);
            } else {
                BaseApplication.getInstance().getWsClientListenerGoogle().getPhotosItemDetailsGoogle(id).enqueue(callback);
            }
        } else
            new HttpGetRequest(source.equalsIgnoreCase("unsplash") ? WSUtils.REQ_FOR_GET_PHOTO_ITEM_DETAILS : WSUtils.REQ_FOR_GET_PHOTO_ITEM_DETAILS_GOOGLE, new IParserListener() {
                @Override
                public void successResponse(int requestCode, Object response) {
                    photoDetailsMutable.setValue(new PhotoDetails((JSONObject) response));
                }

                @Override
                public void errorResponse(int requestCode, String error) {
                    Log.e("api error", error);
                }

                @Override
                public void noInternetConnection(int requestCode) {

                }
            }).execute(source.equalsIgnoreCase("unsplash") ? WSUtils.PHOTO_ITEM_URL + id : WSUtils.PHOTO_ITEM_DETAILS_GOOGLE_URL + id);
    }
}