package com.juaracoding.absensidika.Login.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.juaracoding.absensidika.MainMenu.activity.MainMenu;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;


public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private static int splashTimeOut=5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo=(ImageView)findViewById(R.id.logo);

        if (AppUtil.getSetting(SplashScreen.this,"isLogin","").equalsIgnoreCase("1")){
            startActivity(new Intent(SplashScreen.this, MainMenu.class));
            finish();
        }else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimatorSet set = (AnimatorSet) AnimatorInflater
                            .loadAnimator(SplashScreen.this, R.animator.logoanimation);
                    set.setTarget(logo);
                    set.start();
                }
            }, 4500);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, splashTimeOut);

            Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);
            logo.startAnimation(myanim);
        }
    }
}