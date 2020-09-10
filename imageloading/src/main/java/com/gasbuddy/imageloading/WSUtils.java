package com.gasbuddy.imageloading;

public class WSUtils {

    //SECONDS
    public static final long CONNECTION_TIMEOUT = 125;

    public static final String BASE_URL_UNSPLASH = "https://api.unsplash.com/";
    //    public static final String BASE_URL_GOOGLE = "https://www.googleapis.com/books/v1/volumes?q=cars";
    public static final String BASE_URL_GOOGLE = "https://www.googleapis.com/books/v1/";

    public static final int REQ_FOR_GET_PHOTO_LIST = 100;
    public static final int REQ_FOR_GET_PHOTO_ITEM_DETAILS = 101;
    public static final int REQ_FOR_GET_PHOTO_LIST_GOOGLE = 102;
    public static final int REQ_FOR_GET_PHOTO_ITEM_DETAILS_GOOGLE = 103;

    public static final String PHOTO_LIST_URL = "https://api.unsplash.com/photos/?&page=1&per_page=30";
    public static final String PHOTO_ITEM_URL = "https://api.unsplash.com/photos/";

    public static final String PHOTO_LIST_GOOGLE_URL = "https://www.googleapis.com/books/v1/volumes?q=cars";
    public static final String PHOTO_ITEM_DETAILS_GOOGLE_URL = "https://www.googleapis.com/books/v1/volumes/";

}