package com.example.chat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.chat.R;

import java.util.Objects;

public class custom_loading extends Dialog {
    public custom_loading(@NonNull Context context,String what) {
        super(context);
        WindowManager.LayoutParams params= getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        View view;
        if(Objects.equals(what, "ls"))
        {
            RotateAnimation rotate = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            view = LayoutInflater.from(context).inflate(R.layout.custom_loading,null);
        }
        else{
            view=LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
        }
        setContentView(view);
    }
}
