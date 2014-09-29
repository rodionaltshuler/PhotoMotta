package com.ottamotta.photomotta.menu;

import android.content.Context;
import android.widget.ImageView;

import com.ottamotta.photomotta.filler.RevealingImageView;

public class RevealMenuItem implements MenuItem {

    @Override
    public ImageView createImageView(Context context) {
        return new RevealingImageView(context);
    }

    @Override
    public String getTitle() {
        return "Reveal image";
    }
}
