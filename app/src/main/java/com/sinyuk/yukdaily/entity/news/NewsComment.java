package com.sinyuk.yukdaily.entity.news;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sinyuk on 16.10.24.
 */

public class NewsComment {

    /**
     * author : EleganceWorld
     * id : 545442
     * content : 上海到济南，无尽的猪排盖饭… （后略）
     * likes : 0
     * time : 1413589303
     * avatar : http://pic2.zhimg.com/1f76e6a25_im.jpg
     */

    @SerializedName("author")
    private String author;
    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("likes")
    private int likes;
    @SerializedName("time")
    private long time;
    @SerializedName("avatar")
    private String avatar;

    public String getAuthor() { return author;}

    public void setAuthor(String author) { this.author = author;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getContent() { return content;}

    public void setContent(String content) { this.content = content;}

    public int getLikes() { return likes;}

    public void setLikes(int likes) { this.likes = likes;}

    public long getTime() { return time;}

    public void setTime(long time) { this.time = time;}

    public String getAvatar() { return avatar;}

    public void setAvatar(String avatar) { this.avatar = avatar;}
}
