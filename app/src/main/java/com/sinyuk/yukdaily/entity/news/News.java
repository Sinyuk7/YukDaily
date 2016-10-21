package com.sinyuk.yukdaily.entity.news;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sinyuk.yukdaily.BR;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class News extends BaseObservable implements Parcelable {
    @SerializedName("body")
    private String body;
    @SerializedName("image_source")
    private String imageSource;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("share_url")
    private String shareUrl;
    @SerializedName("ga_prefix")
    private String gaPrefix;


    @SerializedName("section")
    private Section section;
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private int id;
    @SerializedName("js")
    private List<String> js;
    @SerializedName("images")
    private List<String> images;
    @SerializedName("css")
    private List<String> css;

    @Bindable
    public String getBody() { return body;}

    public void setBody(String body) {
        this.body = body;
        notifyPropertyChanged(BR.body);
    }

    @Bindable
    public String getImageSource() { return imageSource;}

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
        notifyPropertyChanged(BR.imageSource);
    }

    @Bindable
    public String getTitle() { return title;}

    public void setTitle(String title) {
        this.title = title;

        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getImage() { return image;}

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);

    }

    @Bindable
    public String getShareUrl() { return shareUrl;}

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
        notifyPropertyChanged(BR.shareUrl);
    }

    public String getGaPrefix() { return gaPrefix;}

    public void setGaPrefix(String gaPrefix) { this.gaPrefix = gaPrefix;}

    @Bindable
    public Section getSection() { return section;}

    public void setSection(Section section) {
        this.section = section;
        notifyPropertyChanged(BR.section);
    }

    public int getType() { return type;}

    public void setType(int type) { this.type = type;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public List<String> getJs() { return js;}

    public void setJs(List<String> js) { this.js = js;}

    @Bindable
    public List<String> getImages() { return images;}

    public void setImages(List<String> images) {
        this.images = images;
        notifyPropertyChanged(BR.images);

    }

    @Bindable
    public List<String> getCss() { return css;}

    public void setCss(List<String> css) {
        this.css = css;
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeString(this.imageSource);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.shareUrl);
        dest.writeString(this.gaPrefix);
        dest.writeParcelable(this.section, flags);
        dest.writeInt(this.type);
        dest.writeInt(this.id);
        dest.writeStringList(this.js);
        dest.writeStringList(this.images);
        dest.writeStringList(this.css);
    }

    public News() {}

    protected News(Parcel in) {
        this.body = in.readString();
        this.imageSource = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.shareUrl = in.readString();
        this.gaPrefix = in.readString();
        this.section = in.readParcelable(Section.class.getClassLoader());
        this.type = in.readInt();
        this.id = in.readInt();
        this.js = in.createStringArrayList();
        this.images = in.createStringArrayList();
        this.css = in.createStringArrayList();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {return new News(source);}

        @Override
        public News[] newArray(int size) {return new News[size];}
    };
}
