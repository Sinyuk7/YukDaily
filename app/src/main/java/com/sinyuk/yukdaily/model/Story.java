package com.sinyuk.yukdaily.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.sinyuk.yukdaily.BR;

import java.util.List;

/**
 * Created by Sinyuk on 2016/10/11.
 */

public class Story extends BaseObservable {

    @SerializedName("title")
    private String title;
    @SerializedName("ga_prefix")
    private String gaPrefix;
    @SerializedName("multipic")
    private boolean multiPic;
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private int id;
    @SerializedName("images")
    private List<String> imageList;
    @SerializedName("image")
    private String image;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public boolean isMultiPic() {
        return multiPic;
    }

    public void setMultiPic(boolean multiPic) {
        this.multiPic = multiPic;
        notifyPropertyChanged(BR.multiPic);
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
        notifyPropertyChanged(BR.imageList);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);

    }

    @Override
    public String toString() {
        return "Story{" +
                "title='" + title + '\'' +
                ", gaPrefix='" + gaPrefix + '\'' +
                ", multiPic=" + multiPic +
                ", type=" + type +
                ", id=" + id +
                ", imageList=" + imageList +
                ", image='" + image + '\'' +
                '}';
    }
}
