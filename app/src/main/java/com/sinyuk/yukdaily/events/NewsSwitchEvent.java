package com.sinyuk.yukdaily.events;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class NewsSwitchEvent {
    private final int index;

    public int getIndex() {
        return index;
    }

    public NewsSwitchEvent(int index){
        this.index = index;
    }



}
