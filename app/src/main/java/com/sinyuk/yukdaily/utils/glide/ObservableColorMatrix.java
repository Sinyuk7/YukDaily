package com.sinyuk.yukdaily.utils.glide;

import android.graphics.ColorMatrix;
import android.util.Property;

import com.sinyuk.myutils.animation.AnimUtils;


/**
 * An extension to {@link ColorMatrix} which caches the saturation value for animation purposes.
 * TODO: Look into this: https://github
 * .com/square/picasso/commit/a181e0fbead6889347b39ac49363dbedde308120
 * https://blog.neteril.org/blog/2014/11/23/android-material-image-loading/
 */
public class ObservableColorMatrix extends ColorMatrix {

    public static final Property<ObservableColorMatrix, Float> SATURATION = new AnimUtils
            .FloatProperty<ObservableColorMatrix>("saturation") {

        @Override
        public void setValue(ObservableColorMatrix cm, float value) {
            cm.setSaturation(value);
        }

        @Override
        public Float get(ObservableColorMatrix cm) {
            return cm.getSaturation();
        }
    };
    private float saturation = 1f;

    public ObservableColorMatrix() {
        super();
    }

    public float getSaturation() {
        return saturation;
    }

    @Override
    public void setSaturation(float saturation) {
        this.saturation = saturation;
        super.setSaturation(saturation);
    }
}
