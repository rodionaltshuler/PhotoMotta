package com.ottamotta.photomotta.filler;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FillableImageView extends ImageView implements Filler.Fillable {

    private Filler filler;

    private Filler.Callback callback = new Filler.Callback() {
        @Override
        public void onFilled() {
            callOnClick();
        }

        @Override
        public void onFillUpdate() {
            invalidate();
        }
    };

    public FillableImageView(Context context) {
        super(context);
    }

    public FillableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        filler = new Filler(callback, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        filler.drawFill(canvas);
    }

    @Override
    public void startFill(float touchX, float touchY) {
        filler.startFill(touchX, touchY);
    }

    @Override
    public void stopFill() {
        filler.stopFill();
    }

    @Override
    public void setFiller(Filler filler) {
        this.filler = filler;
        invalidate();
    }

    @Override
    public Filler getFiller() {
        return filler;
    }
}
