package com.lamesa.netfilms.video.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by Taurus on 2017/5/20.
 */

public class ImageDisplayEngine {

    public static void display(Context context, ImageView view, String path, int defaultHolder){
        Glide.with(context)
                .load(path)
                .centerCrop()
                .placeholder(defaultHolder)
                .into(view);
    }

    public static void displayNoCenterCrop(Context context, ImageView view, String path, int defaultHolder){
        Glide.with(context)
                .load(path)
                .placeholder(defaultHolder)
                .into(view);
    }

    public static void displayAsBitmap(Context context, ImageView view, String path, int defaultHolder){
        Glide.with(context)
                .load(path)
                .centerCrop()
                .placeholder(defaultHolder)
                .into(view);
    }

    public static void displayAsBitmap(Context context, String path, final OnBitmapResourceCallBack callBack){
        Glide.with(context)
                .load(path)
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if(callBack!=null){
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                            Bitmap bitmap = null;
                            if(bitmapDrawable!=null){
                                bitmap = bitmapDrawable.getBitmap();
                            }
                            callBack.onResourceReady(bitmap);
                        }
                    }
                });
    }

    public interface OnBitmapResourceCallBack{
        void onResourceReady(Bitmap bitmap);
    }

}
