package com.sinyuk.yukdaily.entity.Gank;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.sinyuk.yukdaily.BR;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankResult extends BaseObservable {

    @SerializedName("Android")
    private List<GankData> anzhuo;
    @SerializedName("iOS")
    private List<GankData> ios;
    @SerializedName("前端")
    private List<GankData> frontEnd;
    @SerializedName("拓展资源")
    private List<GankData> plus;


    @Bindable
    public List<GankData> getAnzhuo() {
        return anzhuo;
    }

    public void setAnzhuo(List<GankData> anzhuo) {
        this.anzhuo = anzhuo;
        notifyPropertyChanged(BR.anzhuo);
    }

    @Bindable
    public List<GankData> getIos() {
        return ios;
    }

    public void setIos(List<GankData> ios) {
        this.ios = ios;
        notifyPropertyChanged(BR.ios);
    }

    @Bindable
    public List<GankData> getFrontEnd() {
        return frontEnd;
    }

    public void setFrontEnd(List<GankData> frontEnd) {
        this.frontEnd = frontEnd;
        notifyPropertyChanged(BR.frontEnd);
    }

    @Bindable
    public List<GankData> getPlus() {
        return plus;
    }

    public void setPlus(List<GankData> plus) {
        this.plus = plus;
        notifyPropertyChanged(BR.plus);
    }


}
