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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchImagesList extends BaseObservable {

    private ArrayList<PhotoDetails> photoDetailsArrayList = new ArrayList<>();
    private MutableLiveData<List<PhotoDetails>> photoDetails = new MutableLiveData<>();

    public MutableLiveData<List<PhotoDetails>> getPhotosList() {
        return photoDetails;
    }

    public void fetchList(String networkMethod, String source) {
        if (networkMethod.equalsIgnoreCase("Retrofit")) {
            Callback<List<PhotoDetails>> callback = new Callback<List<PhotoDetails>>() {
                @Override
                public void onResponse(@NonNull Call<List<PhotoDetails>> call, Response<List<PhotoDetails>> response) {
                    photoDetailsArrayList = new ArrayList<>();
                    photoDetailsArrayList = (ArrayList<PhotoDetails>) response.body();
                    photoDetails.setValue(photoDetailsArrayList);
                }

                @Override
                public void onFailure(@NonNull Call<List<PhotoDetails>> call, @NonNull Throwable t) {
                    Log.e("api error", t.getMessage(), t);
                }
            };
            Callback<JsonObject> callbackGoogle = new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, Response<JsonObject> response) {
                    photoDetailsArrayList = new ArrayList<>();
                    JsonObject jsonObject = response.body();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("items");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<PhotoDetails>>() {
                    }.getType();
                    photoDetailsArrayList = gson.fromJson(String.valueOf(jsonArray), listType);
                    if (photoDetailsArrayList != null)
                        photoDetails.setValue(photoDetailsArrayList);
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    Log.e("api error", t.getMessage(), t);
                }
            };
            if (source.equalsIgnoreCase("unsplash")) {
                BaseApplication.getInstance().getWsClientListenerUnsplash().getPhotosList(1, 30).enqueue(callback);
            } else {
                BaseApplication.getInstance().getWsClientListenerGoogle().getPhotosListGoogle("cars").enqueue(callbackGoogle);
            }
        } else
            new HttpGetRequest(source.equalsIgnoreCase("unsplash") ? WSUtils.REQ_FOR_GET_PHOTO_LIST : WSUtils.REQ_FOR_GET_PHOTO_LIST_GOOGLE,
                    new IParserListener() {
                        @Override
                        public void successResponse(int requestCode, Object response) {
                            if (requestCode == WSUtils.REQ_FOR_GET_PHOTO_LIST) {
                                photoDetailsArrayList = new ArrayList<>();
                                JSONArray jsonArray = (JSONArray) response;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    photoDetailsArrayList.add(new PhotoDetails(jsonArray.optJSONObject(i)));
                                }
                                photoDetails.setValue(photoDetailsArrayList);
                            } else {
                                photoDetailsArrayList = new ArrayList<>();
                                JSONObject jsonObject = (JSONObject) response;
                                JSONArray jsonArray = jsonObject.optJSONArray("items");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    photoDetailsArrayList.add(new PhotoDetails(jsonArray.optJSONObject(i)));
                                }
                                photoDetails.setValue(photoDetailsArrayList);
                            }
                        }

                        @Override
                        public void errorResponse(int requestCode, String error) {
                            Log.e("api error", error);
                        }

                        @Override
                        public void noInternetConnection(int requestCode) {

                        }
                    }).execute(source.equalsIgnoreCase("unsplash") ? WSUtils.PHOTO_LIST_URL : WSUtils.PHOTO_LIST_GOOGLE_URL);
    }
}