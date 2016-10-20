package com.sinyuk.yukdaily.entity.news;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import com.sinyuk.yukdaily.BR;
/**
 * Created by Sinyuk on 2016/10/11.
 */

public class StartImage extends BaseObservable {

    @SerializedName("text")
    private String author;
    @SerializedName("img")
    private String imageUrl;

    @Bindable
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        notifyPropertyChanged(BR.author);
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

}
