package com.sinyuk.yukdaily.entity.news;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class ThemeData {

    @SerializedName("description")
    private String description;
    @SerializedName("background")
    private String backdropUrl;
    @SerializedName("color")
    private int color;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String thumbnailUrl;
    @SerializedName("image_source")
    private String imageSource;


    @SerializedName("stories")
    private List<Story> stories;

    @SerializedName("editors")
    private List<Editor> editors;

    public String getDescription() { return description;}

    public void setDescription(String description) { this.description = description;}

    public String getBackdropUrl() { return backdropUrl;}

    public void setBackdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl;}

    public int getColor() { return color;}

    public void setColor(int color) { this.color = color;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getThumbnailUrl() { return thumbnailUrl;}

    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl;}

    public String getImageSource() { return imageSource;}

    public void setImageSource(String imageSource) { this.imageSource = imageSource;}

    public List<Story> getStories() { return stories;}

    public void setStories(List<Story> stories) { this.stories = stories;}

    public List<Editor> getEditors() { return editors;}

    public void setEditors(List<Editor> editors) { this.editors = editors;}


    public static class Editor {
        @SerializedName("url")
        private String url;
        @SerializedName("bio")
        private String bio;
        @SerializedName("id")
        private int id;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("name")
        private String name;

        public String getUrl() { return url;}

        public void setUrl(String url) { this.url = url;}

        public String getBio() { return bio;}

        public void setBio(String bio) { this.bio = bio;}

        public int getId() { return id;}

        public void setId(int id) { this.id = id;}

        public String getAvatar() { return avatar;}

        public void setAvatar(String avatar) { this.avatar = avatar;}

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}
    }

    @Override
    public String toString() {
        return "ThemeData{" +
                "description='" + description + '\'' +
                ", backdropUrl='" + backdropUrl + '\'' +
                ", color=" + color +
                ", name='" + name + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", imageSource='" + imageSource + '\'' +
                ", stories=" + stories +
                ", editors=" + editors +
                '}';
    }
}
