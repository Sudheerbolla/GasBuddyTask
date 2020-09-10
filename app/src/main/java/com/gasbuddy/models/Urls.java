package com.gasbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Urls implements Parcelable {

    @SerializedName("raw")
    @Expose
    public String raw;
    @SerializedName("full")
    @Expose
    public String full;
    @SerializedName("regular")
    @Expose
    public String regular;
    @SerializedName("small")
    @Expose
    public String small;
    @SerializedName("thumb")
    @Expose
    public String thumb;

    public Urls(JSONObject jsonObject) {
        raw = jsonObject.optString("raw");
        full = jsonObject.optString("full");
        regular = jsonObject.optString("regular");
        small = jsonObject.optString("small");
        thumb = jsonObject.optString("thumb");
    }

    protected Urls(Parcel in) {
        raw = in.readString();
        full = in.readString();
        regular = in.readString();
        small = in.readString();
        thumb = in.readString();
    }

    public static final Creator<Urls> CREATOR = new Creator<Urls>() {
        @Override
        public Urls createFromParcel(Parcel in) {
            return new Urls(in);
        }

        @Override
        public Urls[] newArray(int size) {
            return new Urls[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(raw);
        dest.writeString(full);
        dest.writeString(regular);
        dest.writeString(small);
        dest.writeString(thumb);
    }
}