package com.system.architecture.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by eferraz on 19/08/16.
 */

public class AnimationUtil {

    public static void animateCircularReval(final View mRevealView, View mAnchorView){

        // finding X and Y co-ordinates
        int cx = (mRevealView.getLeft() + mRevealView.getRight());
        int cy = (mRevealView.getTop());

        // to find  radius when icon is tapped for showing layout
        int startradius=0;
        int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

        // performing circular reveal when icon will be tapped
        Animator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, startradius, endradius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);

        //reverse animation
        // to find radius when icon is tapped again for hiding layout


        //  starting radius will be the radius or the extent to which circular reveal animation is to be shown
        int reverse_startradius = Math.max(mRevealView.getWidth(),mRevealView.getHeight());
        //endradius will be zero
        int reverse_endradius=0;


        // performing circular reveal for reverse animation
        Animator animate = ViewAnimationUtils.createCircularReveal(mRevealView,cx,cy,reverse_startradius,reverse_endradius);
        animate.setInterpolator(new AccelerateDecelerateInterpolator());
        animate.setDuration(1000);

        if(!mRevealView.isShown()) {

            // to show the layout when icon is tapped
            mRevealView.setVisibility(View.VISIBLE);
            animator.start();
        }
        else {

            // to hide layout on animation end
            animate.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mRevealView.setVisibility(View.INVISIBLE);
                }
            });
            animate.start();

        }
    }

}
