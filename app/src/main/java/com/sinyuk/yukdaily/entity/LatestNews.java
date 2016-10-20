package com.sinyuk.yukdaily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sinyuk on 2016/10/13.
 */
public class LatestNews {

    @SerializedName("date")
    private String date;
    @SerializedName("stories")
    private List<Story> stories;
    @SerializedName("top_stories")
    private List<Story> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<Story> topStories) {
        this.topStories = topStories;
    }

}
