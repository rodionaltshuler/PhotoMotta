package com.ottamotta.photomotta.menu;

import android.content.Context;
import android.widget.ImageView;

public interface MenuItem {

    public ImageView createImageView(Context context);
    public String getTitle();

}
