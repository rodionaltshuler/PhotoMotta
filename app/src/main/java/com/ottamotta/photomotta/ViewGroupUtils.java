package com.ottamotta.photomotta;

import android.view.View;
import android.view.ViewGroup;

public class ViewGroupUtils {

    public static ViewGroup getParent(View view) {
        return (ViewGroup)view.getParent();
    }

    public static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if(parent != null) {
            parent.removeView(view);
        }
    }

    public static void replaceView(View currentView, View newView) {
        ViewGroup parent = getParent(currentView);
        if(parent == null) {
            return;
        }
        final int index = parent.indexOfChild(currentView);
        ViewGroup.LayoutParams params = currentView.getLayoutParams();
        removeView(currentView);
        removeView(newView);
        newView.setLayoutParams(params);
        parent.addView(newView, index);
    }
}