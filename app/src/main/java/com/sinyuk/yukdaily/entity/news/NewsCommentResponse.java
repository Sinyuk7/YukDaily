package com.sinyuk.yukdaily.entity.news;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyuk on 16.10.24.
 */

public class NewsCommentResponse {

    @SerializedName("comments")
    private List<NewsComment> comments = new ArrayList<>();

    public List<NewsComment> getComments() { return comments;}

    public void setComments(List<NewsComment> comments) { this.comments = comments;}

}
