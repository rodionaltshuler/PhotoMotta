package com.ottamotta.photomotta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class MaskImageView extends ImageView {

    private Bitmap mask;
    private Paint paint;

    private int w, h;

    int scaledWidth;
    int scaledHeight;


    /**
     * Converts any bitmap to bitmap could be used as alpha mask
     * Source: http://www.curious-creature.org/2012/12/13/android-recipe-2-fun-with-shaders/
     *
     * @param mask
     * @return
     */
    private static Bitmap convertToAlphaMask(Bitmap mask) {
        Bitmap bitmap = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas c = new Canvas(bitmap);
        c.drawBitmap(mask, 0.0f, 0.0f, null);
        return bitmap;
    }


    private static Shader createShader(Bitmap b) {
        return new BitmapShader(b, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    public MaskImageView(Context context) {
        super(context);
    }

    public MaskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void setupMask(Bitmap targetBitmap) {

        float wRatio = (targetBitmap.getWidth()) / (float) w;
        float hRatio = (targetBitmap.getHeight()) / (float) h;
        float zoom = Math.max(wRatio, hRatio);

        Log.d("[PHOTOMOTTA]", "zoom: " + zoom);


        if (zoom > 1) {
            scaledWidth = (int) ((float) (targetBitmap.getWidth()) / zoom);
            scaledHeight = (int) ((float) (targetBitmap.getHeight()) / zoom);
            targetBitmap = Bitmap.createScaledBitmap(targetBitmap, scaledWidth, scaledHeight, false);
        } else {
            scaledWidth = targetBitmap.getWidth();
            scaledHeight = targetBitmap.getHeight();
        }

        Log.d("[PHOTOMOTTA]", "scaledWidth  =  " + scaledWidth);
        Log.d("[PHOTOMOTTA]", "scaledHeight  =  " + scaledHeight);

        mask = convertToAlphaMask(BitmapFactory.decodeResource(getResources(), R.drawable.spot_mask));
        int side = Math.min(scaledWidth, scaledHeight);
        Log.d("[PHOTOMOTTA]", "mask side  =  " + side);
        mask = Bitmap.createScaledBitmap(mask, side, side, false);
        paint = new Paint();

        Shader targetShader = createShader(targetBitmap);
        paint.setShader(targetShader);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            this.w = w; this.h = h;
            //init(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        BitmapDrawable d = (BitmapDrawable) getDrawable();
        setupMask(d.getBitmap());


        float left = (w - mask.getWidth()) / 2;
        float top = (h -  mask.getHeight()) / 2;

        Log.d("[PHOTOMOTTA]", "w =  " + w + "; getWidth() = " + mask.getWidth());
        Log.d("[PHOTOMOTTA]", "h =  " + h + "; getHeight() = " + mask.getHeight());
        Log.d("[PHOTOMOTTA]", "left =  " + left + "; top = " + top);
        //canvas.drawBitmap(mask, 0f, 0f, paint);
        canvas.drawBitmap(mask, left, top, paint);

    }
}
