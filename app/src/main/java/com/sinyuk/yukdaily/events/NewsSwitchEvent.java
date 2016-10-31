package com.sinyuk.yukdaily.events;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class NewsSwitchEvent {
    private final String type;
    private final int index;

    public int getIndex() {
        return index;
    }

    public NewsSwitchEvent(String type, int index){
        this.type = type;
        this.index = index;
    }

    public String getType() {
        return type;
    }


}
