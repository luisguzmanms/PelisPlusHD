package com.lamesa.netfilms.otros.statics;


import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lamesa.netfilms.R;

public class Animacion {


    public static Animation alpha_in(Context mContext){
        Animation animacion_alpha_in = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_out);
        return animacion_alpha_in;
    }

    public static  Animation alpha_out(Context mContext){
        Animation animacion_alpha_out = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_out);
        return animacion_alpha_out;
    }



}