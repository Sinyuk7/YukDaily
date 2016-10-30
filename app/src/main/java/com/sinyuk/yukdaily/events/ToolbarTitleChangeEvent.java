package com.sinyuk.yukdaily.events;

/**
 * Created by Sinyuk on 16.10.30.
 */
public class ToolbarTitleChangeEvent {
    private final String title;

    public String getTitle() {
        return title;
    }

    public ToolbarTitleChangeEvent(String title) {
        this.title = title;
    }
}
