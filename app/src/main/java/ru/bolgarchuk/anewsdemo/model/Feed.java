package ru.bolgarchuk.anewsdemo.model;

import com.google.gson.annotations.SerializedName;

public class Feed {
    @SerializedName("id")
    private int mId;
    @SerializedName("img")
    private String mImg;

    @SerializedName("title")
    private String mTitle;

    public int getId() {
        return mId;
    }

    public String getImg() {
        return mImg;
    }

    public String getTitle() {
        return mTitle;
    }
}