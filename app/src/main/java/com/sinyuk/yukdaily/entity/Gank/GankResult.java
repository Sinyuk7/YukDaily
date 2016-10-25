package com.sinyuk.yukdaily.entity.Gank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class GankResult {

    @SerializedName("Android")
    private List<GankData> andriod;

    @SerializedName("iOS")
    private List<GankData> ios;
    @SerializedName("休息视频")
    private List<GankData> video;
    @SerializedName("前端")
    private List<GankData> frontEnd;
    @SerializedName("拓展资源")
    private List<GankData> plus;
    @SerializedName("福利")
    private List<GankData> fuli;

    public List<GankData> getAndriod() {
        return andriod;
    }

    public void setAndriod(List<GankData> andriod) {
        this.andriod = andriod;
    }

    public List<GankData> getIos() {
        return ios;
    }

    public void setIos(List<GankData> ios) {
        this.ios = ios;
    }

    public List<GankData> getVideo() {
        return video;
    }

    public void setVideo(List<GankData> video) {
        this.video = video;
    }

    public List<GankData> getFrontEnd() {
        return frontEnd;
    }

    public void setFrontEnd(List<GankData> frontEnd) {
        this.frontEnd = frontEnd;
    }

    public List<GankData> getPlus() {
        return plus;
    }

    public void setPlus(List<GankData> plus) {
        this.plus = plus;
    }

    public List<GankData> getFuli() {
        return fuli;
    }

    public void setFuli(List<GankData> fuli) {
        this.fuli = fuli;
    }
}
