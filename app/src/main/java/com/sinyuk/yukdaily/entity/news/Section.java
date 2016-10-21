package com.sinyuk.yukdaily.entity.news;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sinyuk.yukdaily.BR;

/**
 * Created by Sinyuk on 16.10.21.
 */

public final class Section extends BaseObservable implements Parcelable {
    /**
     * thumbnail : http://pic4.zhimg.com/8a30e405a887c111b14b3d83d608709f.jpg
     * id : 35
     * name : 小事
     */

    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    @Bindable
    public String getThumbnail() { return thumbnail;}

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        notifyPropertyChanged(BR.thumbnail);
    }

    @Bindable
    public int getId() { return id;}

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbnail);
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Section() {}

    protected Section(Parcel in) {
        this.thumbnail = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel source) {return new Section(source);}

        @Override
        public Section[] newArray(int size) {return new Section[size];}
    };
}