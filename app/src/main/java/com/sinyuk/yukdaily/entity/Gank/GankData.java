package com.sinyuk.yukdaily.entity.Gank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.25.
 */

public class GankData {

    /**
     * _id : 57fd9964421aa95dd351b106
     * createdAt : 2016-10-12T10:01:08.961Z
     * desc : 一个漂亮的 Share Button UI 效果。
     * images : ["http://img.gank.io/ee1fcfbd-20a5-4819-943c-80d55301dc4d"]
     * publishedAt : 2016-10-12T11:40:02.146Z
     * source : chrome
     * type : Android
     * url : https://github.com/kayan1990/ShareButton
     * used : true
     * who : 代码家
     */

    @SerializedName("_id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("desc")
    private String title;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("source")
    private String source;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;
    @SerializedName("used")
    private boolean used;
    @SerializedName("who")
    private String author;
    @SerializedName("images")
    private List<String> images;

    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    public String getCreatedAt() { return createdAt;}

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title;}

    public String getPublishedAt() { return publishedAt;}

    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt;}

    public String getSource() { return source;}

    public void setSource(String source) { this.source = source;}

    public String getType() { return type;}

    public void setType(String type) { this.type = type;}

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url;}

    public boolean isUsed() { return used;}

    public void setUsed(boolean used) { this.used = used;}

    public String getAuthor() { return author;}

    public void setAuthor(String author) { this.author = author;}

    public List<String> getImages() { return images;}

    public void setImages(List<String> images) { this.images = images;}

    @Override
    public String toString() {
        return "GankData{" +
                "id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", title='" + title + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", used=" + used +
                ", author='" + author + '\'' +
                ", images=" + images +
                '}';
    }
}
