package com.sinyuk.yukdaily.events;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class NewsSwitchEvent {
    private final String type;
    public NewsSwitchEvent(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
