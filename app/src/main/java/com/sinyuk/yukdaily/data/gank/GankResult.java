package com.sinyuk.yukdaily.data.gank;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankResult {

    @SerializedName("_id")
    private String id;
    @SerializedName("content")
    private String content;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("title")
    private String title;

    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    public String getContent() { return content;}

    public void setContent(String content) { this.content = content;}

    public String getPublishedAt() { return publishedAt;}

    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt;}

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title;}

}
