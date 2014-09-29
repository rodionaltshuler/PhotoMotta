package com.ottamotta.photomotta.filler;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class Filler  {

    public static final int ALPHA = 128;

    private Callback callback;

    private static final float IMPATIENCE_KOEF = 0.7f;
    public static final int REVERSE_FILL_DURATION = 500;
    public static final int FILL_DURATION = 1500;

    private float touchX, touchY;
    private int currentFillRadius;

    private Paint paint;
    private static final int COLOR = Color.GREEN;
    private ValueAnimator progressAnimator;
    private TimeInterpolator startFillInterpolator = new AccelerateInterpolator();

    private boolean isFilling = false;

    private float viewWidth, viewHeight;

    public static class FillTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v instanceof Fillable) {
                Fillable fv = (Fillable) v;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fv.startFill(event.getX(), event.getY());
                        return true;
                    case MotionEvent.ACTION_UP:
                        fv.stopFill();
                        return true;
                }
            }
            return false;
        }
    }

    public interface Callback {
        void onFilled(); //call on click here
        void onFillUpdate(); //call invalidate() here and drawFill() in onDraw() after super method
    }

    /**
     * Implement by your view to use Filler.FillTouchListener
     * but it's not necessary
     */
    public interface Fillable {
        void startFill(float touchX, float touchY);
        void stopFill();
        void setFiller(Filler filler);
        Filler getFiller();
    }


    public Filler(Callback callback, float viewWidth, float viewHeight) {
        this.callback = callback;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        paint = getPaint();
    }

    protected Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setAlpha(ALPHA);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
        if (callback != null) callback.onFillUpdate();
    }

    ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isFilling && animation.getAnimatedFraction() >= IMPATIENCE_KOEF) {
                callback.onFilled();
                isFilling = false;
            }
            currentFillRadius = (Integer) animation.getAnimatedValue();
            callback.onFillUpdate();
        }
    };


    public void startFill(float touchX, float touchY) {
        if (progressAnimator != null) progressAnimator.cancel();
        isFilling = true;
        this.touchX = touchX;
        this.touchY = touchY;
        int maxRadius = getMaxDistanceToCorner(touchX, touchY, viewWidth, viewHeight);
        progressAnimator = ValueAnimator.ofInt(currentFillRadius, maxRadius);
        progressAnimator.setInterpolator(startFillInterpolator);
        progressAnimator.addUpdateListener(animatorUpdateListener);
        progressAnimator.setDuration(FILL_DURATION);
        progressAnimator.start();
    }

    private static int getMaxDistanceToCorner(float touchX, float touchY, float w, float h) {
        float deltaX = touchX < w / 2 ? w - touchX : touchX;
        float deltaY = touchY < h / 2 ? h - touchY : touchY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return (int) distance;
    }


    public void stopFill() {
        isFilling = false;
        if (progressAnimator != null) {
            progressAnimator.removeAllListeners();
            progressAnimator.cancel();
            progressAnimator = ValueAnimator.ofInt(currentFillRadius, 0);
            progressAnimator.addUpdateListener(animatorUpdateListener);
            progressAnimator.setDuration(REVERSE_FILL_DURATION);
            progressAnimator.start();
        }
    }

    /**
     * Override this method 
     * @param canvas
     */
    public void drawFill(Canvas canvas) {
        canvas.drawCircle(touchX, touchY, currentFillRadius, paint);
    }
}
