package com.sinyuk.yukdaily.entity.news;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * Created by Sinyuk on 16.10.24.
 */

public class NewsComment implements Comparable {

    @SerializedName("author")
    private String author;
    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("likes")
    private int likes;
    @SerializedName("time")
    private long time = 0;
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

    @Override
    public String toString() {
        return "NewsComment{" +
                "author='" + author + '\'' +
                ", id=" + id +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", time=" + time +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (time == 0) {
            return -1;
        }
        if (o instanceof NewsComment) {
            Timestamp timestamp1 = new Timestamp(time);
            long time2 = ((NewsComment) o).getTime();
            if (time2 == 0) { return -1; }

            Timestamp timestamp2 = new Timestamp(time2);
            return timestamp2.compareTo(timestamp1);
        }
        return -1;
    }
}
