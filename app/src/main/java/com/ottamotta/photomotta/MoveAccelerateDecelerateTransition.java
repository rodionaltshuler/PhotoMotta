package com.ottamotta.photomotta;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

public class MoveAccelerateDecelerateTransition extends Transition {

    String PROPNAME_X = "position_x";
    String PROPNAME_Y = "position_y";

    TimeInterpolator interpolatorX = new AccelerateDecelerateInterpolator();
    TimeInterpolator interpolatorY = new OvershootInterpolator();

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    /**
     * Convenience method: Add the background Drawable property value
     * to the TransitionsValues.value Map for a target.
     */
    private void captureValues(TransitionValues values) {
        // Capture the property values of views for later use
        values.values.put(PROPNAME_X, values.view.getX());
        values.values.put(PROPNAME_Y, values.view.getY());
    }


    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {

        final View view = endValues.view;

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "y", (Float) startValues.values.get(PROPNAME_Y), (Float) endValues.values.get(PROPNAME_Y));
        ObjectAnimator animatorY1 = ObjectAnimator.ofFloat(view, "x", (Float) startValues.values.get(PROPNAME_X), (Float) startValues.values.get(PROPNAME_X));

        final float deltaX = (Float) endValues.values.get(PROPNAME_X) - (Float) startValues.values.get(PROPNAME_X);
        final int rotationsCount = deltaX > 0 ? (int) (deltaX / 200) + 1 : (int) (deltaX / 200) - 1;

        ObjectAnimator X1Animator = ObjectAnimator.ofFloat(view, "x", (Float) startValues.values.get(PROPNAME_X), (Float) endValues.values.get(PROPNAME_X));

        X1Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                switch (view.getId()) {
                    case R.id.item1:
                    case R.id.item2:
                    case R.id.item3:
                        float rotation = valueAnimator.getAnimatedFraction() * 360 * rotationsCount;
                        view.setRotation(rotation);
                }
            }
        });

        ObjectAnimator[] animatorsX = new ObjectAnimator[]{
                X1Animator
                , ObjectAnimator.ofFloat(view, "y", (Float) endValues.values.get(PROPNAME_Y), (Float) endValues.values.get(PROPNAME_Y))
        };


        //ObjectAnimator animatorRotate =
        animatorY.setInterpolator(interpolatorY);

        AnimatorSet setY = new AnimatorSet();
        setY.playTogether(animatorY, animatorY1);
        setY.setDuration(400);

        AnimatorSet setX = new AnimatorSet();
        setX.playTogether(animatorsX);
        setX.setInterpolator(interpolatorX);
        setX.setDuration(1200);

        AnimatorSet set = new AnimatorSet();

        set.play(setX).after(setY);

        return set;

    }


}
