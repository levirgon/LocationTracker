package com.levirgon.tutexplocation;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.levirgon.tutexplocation.R;
import com.vstechlab.easyfonts.EasyFonts;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Animation tv_anim = AnimationUtils.loadAnimation(this, R.anim.tv_anim);
        Animation icon_anim = AnimationUtils.loadAnimation(this, R.anim.iv_anim);

        TextView tv_title = (TextView) findViewById(R.id.titleTextView);
        tv_title.setAnimation(tv_anim);
        tv_title.setTypeface(EasyFonts.caviarDreamsBold(this));
        ImageView iv_icon = (ImageView) findViewById(R.id.iconImageView);
        iv_icon.setAnimation(icon_anim);


        TextView tv_bee = (TextView) findViewById(R.id.tv_bee);

        tv_bee.setTypeface(EasyFonts.caviarDreamsBold(this));
        filpIt(tv_bee);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, CategoryLoaderActivity.class));
                    finish();
            }
        }, 4000);
    }
    private void filpIt(TextView view) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(view, "rotationY", 0f, 360f);
        flip.setDuration(4000);
        flip.start();
    }
}
