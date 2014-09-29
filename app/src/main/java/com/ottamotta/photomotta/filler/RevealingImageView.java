package com.ottamotta.photomotta.filler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;

public class RevealingImageView extends FillableImageView {

    public RevealingImageView(Context context) {
        super(context);
    }

    public RevealingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RevealingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupPaint();
    }

    private void setupPaint() {
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            getFiller().setPaint(paint);
        }
    }

    private Bitmap getBitmap()
    {
        Drawable drawable = this.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                return Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);
            }
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getFiller().drawFill(canvas);
    }
}
