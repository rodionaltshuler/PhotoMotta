package com.ottamotta.photomotta.menu;

import android.content.Context;
import android.widget.ImageView;

import com.ottamotta.photomotta.filler.FillableImageView;

public class GreenMenuItem implements MenuItem {

    @Override
    public ImageView createImageView(Context context) {
        return new FillableImageView(context);
    }

    @Override
    public String getTitle() {
        return "Color filter";
    }
}
