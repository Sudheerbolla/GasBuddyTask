package com.gasbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class PhotoDetails implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("promoted_at")
    @Expose
    public Object promotedAt;
    @SerializedName("width")
    @Expose
    public Integer width;
    @SerializedName("height")
    @Expose
    public Integer height;
    @SerializedName("color")
    @Expose
    public String color;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("alt_description")
    @Expose
    public String altDescription;
    @SerializedName("urls")
    @Expose
    public Urls urls;
    @SerializedName("likes")
    @Expose
    public Integer likes;
    @SerializedName("views")
    @Expose
    public Integer views;
    @SerializedName("downloads")
    @Expose
    public Integer downloads;

    @SerializedName("selfLink")
    @Expose
    public String selfLink;
    @SerializedName("volumeInfo")
    @Expose
    public VolumeInfo volumeInfo;

    public PhotoDetails(JSONObject jsonObject) {
        id = jsonObject.optString("id");
        createdAt = jsonObject.optString("created_at");
        description = jsonObject.optString("description");
        altDescription = jsonObject.optString("alt_description");
        likes = jsonObject.optInt("likes");
        views = jsonObject.optInt("views");
        downloads = jsonObject.optInt("downloads");
        urls = new Urls(jsonObject.optJSONObject("urls"));

        selfLink=jsonObject.optString("selfLink");
        volumeInfo = new VolumeInfo(jsonObject.optJSONObject("volumeInfo"));
    }

    protected PhotoDetails(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            width = null;
        } else {
            width = in.readInt();
        }
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readInt();
        }
        color = in.readString();
        description = in.readString();
        altDescription = in.readString();
        urls = in.readParcelable(Urls.class.getClassLoader());
        if (in.readByte() == 0) {
            likes = null;
        } else {
            likes = in.readInt();
        }
        if (in.readByte() == 0) {
            views = null;
        } else {
            views = in.readInt();
        }
        if (in.readByte() == 0) {
            downloads = null;
        } else {
            downloads = in.readInt();
        }
    }

    public static final Creator<PhotoDetails> CREATOR = new Creator<PhotoDetails>() {
        @Override
        public PhotoDetails createFromParcel(Parcel in) {
            return new PhotoDetails(in);
        }

        @Override
        public PhotoDetails[] newArray(int size) {
            return new PhotoDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        if (width == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(width);
        }
        if (height == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(height);
        }
        dest.writeString(color);
        dest.writeString(description);
        dest.writeString(altDescription);
        dest.writeParcelable(urls, Parcelable.CONTENTS_FILE_DESCRIPTOR);
        if (likes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(likes);
        }
        if (views == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(views);
        }
        if (downloads == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(downloads);
        }
    }

}