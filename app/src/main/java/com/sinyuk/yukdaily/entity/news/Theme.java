package com.sinyuk.yukdaily.entity.news;

import com.google.gson.annotations.SerializedName;

public final class Theme {
    @SerializedName("color")
    private int color;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("description")
    private String description;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getColor() { return color;}

    public void setColor(int color) { this.color = color;}

    public String getThumbnail() { return thumbnail;}

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail;}

    public String getDescription() { return description;}

    public void setDescription(String description) { this.description = description;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    @Override
    public String toString() {
        return "Theme{" +
                "color=" + color +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}