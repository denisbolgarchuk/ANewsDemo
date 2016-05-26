package ru.bolgarchuk.anewsdemo.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "top_news")
public class TopNews {
    @SerializedName("id")
    @DatabaseField(id = true)
    private int mId;
    private Feed mFeed;
    @SerializedName("img_alt")
    private String mImgAlt;
    @SerializedName("ts")
    private long mTs;
    @SerializedName("link")
    private String mLink;
    @SerializedName("img")
    @DatabaseField()
    private String mImg;
    @SerializedName("title")
    @DatabaseField()
    private String mTitle;
    @SerializedName("region")
    private String mRegion;
    @SerializedName("modified")
    private long mModifiedTimeStamp;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("content")
    private String mContent;
    @SerializedName("mobile_link")
    private String mMobileLink;

    TopNews() {
    }

    public int getId() {
        return mId;
    }

    public String getImgAlt() {
        return mImgAlt;
    }

    public long getTs() {
        return mTs;
    }

    public String getLink() {
        return mLink;
    }

    public String getImg() {
        return mImg;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getRegion() {
        return mRegion;
    }

    public long getModifiedTimeStamp() {
        return mModifiedTimeStamp;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getContent() {
        return mContent;
    }

    public String getMobileLink() {
        return mMobileLink;
    }
}
