package com.sinyuk.yukdaily.events;

/**
 * Created by Sinyuk on 16.10.30.
 */
public class GankSwitchEvent {
    private final String type;

    public GankSwitchEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
