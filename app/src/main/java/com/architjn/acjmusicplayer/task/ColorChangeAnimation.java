package com.architjn.acjmusicplayer.task;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.widget.LinearLayout;

import com.afollestad.async.Action;
import com.architjn.acjmusicplayer.R;

/**
 * Created by architjn on 13/12/15.
 */
public abstract class ColorChangeAnimation extends Action {

    private Context context;
    private LinearLayout detailHolder;
    private String artPath;
    private Integer colorFrom;
    private ValueAnimator colorAnimation;

    public ColorChangeAnimation(Context context, LinearLayout detailHolder, String artPath) {
        this.context = context;
        this.detailHolder = detailHolder;
        this.artPath = artPath;
        colorFrom = ((ColorDrawable) detailHolder.getBackground()).getColor();
    }

    @NonNull
    @Override
    public String id() {
        return artPath;
    }

    @Nullable
    @Override
    protected Object run() throws InterruptedException {
        try {
            Bitmap bmp = BitmapFactory.decodeFile(artPath);
            Palette.generateAsync(bmp,
                    new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(final Palette palette) {
                            Integer colorTo = palette.getVibrantColor(palette.getDarkVibrantColor(
                                    palette.getDarkMutedColor(palette.getMutedColor(
                                            ContextCompat.getColor(context,
                                                    R.color.colorPrimary)))));
                            onColorFetched(colorTo);
                            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(2000);
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    detailHolder.setBackgroundColor((Integer) animator.getAnimatedValue());
                                }

                            });
                            colorAnimation.start();
                        }
                    }
            );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void onColorFetched(Integer colorPrimary);

}
