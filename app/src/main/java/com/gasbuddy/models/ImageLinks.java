package com.gasbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class ImageLinks implements Parcelable {

    @SerializedName("smallThumbnail")
    @Expose
    public String smallThumbnail;
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    public ImageLinks(JSONObject jsonObject) {
        if (jsonObject != null) {
            smallThumbnail = jsonObject.optString("smallThumbnail");
            thumbnail = jsonObject.optString("thumbnail");
        }
    }

    protected ImageLinks(Parcel in) {
        smallThumbnail = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<ImageLinks> CREATOR = new Creator<ImageLinks>() {
        @Override
        public ImageLinks createFromParcel(Parcel in) {
            return new ImageLinks(in);
        }

        @Override
        public ImageLinks[] newArray(int size) {
            return new ImageLinks[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(smallThumbnail);
        dest.writeString(thumbnail);
    }
}