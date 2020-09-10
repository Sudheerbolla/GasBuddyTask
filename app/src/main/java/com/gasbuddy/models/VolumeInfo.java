package com.gasbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class VolumeInfo implements Parcelable {

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("publishedDate")
    @Expose
    public String publishedDate;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("imageLinks")
    @Expose
    public ImageLinks imageLinks;

    public VolumeInfo(JSONObject jsonObject) {
        if (jsonObject != null) {
            title = jsonObject.optString("title");
            publishedDate = jsonObject.optString("publishedDate");
            description = jsonObject.optString("description");
            imageLinks = new ImageLinks(jsonObject.optJSONObject("imageLinks"));
        }
    }

    protected VolumeInfo(Parcel in) {
        title = in.readString();
        publishedDate = in.readString();
        description = in.readString();
        imageLinks = in.readParcelable(ImageLinks.class.getClassLoader());
    }

    public static final Creator<VolumeInfo> CREATOR = new Creator<VolumeInfo>() {
        @Override
        public VolumeInfo createFromParcel(Parcel in) {
            return new VolumeInfo(in);
        }

        @Override
        public VolumeInfo[] newArray(int size) {
            return new VolumeInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publishedDate);
        dest.writeString(description);
        dest.writeParcelable(imageLinks, flags);
    }
}
