package com.ottamotta.photomotta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class BitmapEditor {

    private static final String KEY_URI = "key_last_uri";

    private static BitmapEditor instance;

    private Context context;

    private BitmapEditor(Context context) {
        this.context = context;
    }

    public static synchronized BitmapEditor getInstance(Context context) {
        if (null == instance) {
            instance = new BitmapEditor(context);
        }
        return instance;
    }

    public void save(Uri uri) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_URI, uri.toString()).commit();
    }

    public Uri getLastUri() {
        String uri = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_URI, null);
        return Uri.parse(uri);
    }

    public Bitmap create(Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // constant grayscale
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // get color on each channel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // apply grayscale sample
                B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // apply intensity level for sepid-toning on each channel
                R += (depth * red);
                if (R > 255) {
                    R = 255;
                }

                G += (depth * green);
                if (G > 255) {
                    G = 255;
                }

                B += (depth * blue);
                if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

}
