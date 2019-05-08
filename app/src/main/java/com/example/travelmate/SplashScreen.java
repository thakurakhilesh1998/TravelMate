package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener {
    ImageView appLogo;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce1);
        appLogo = findViewById(R.id.appLogo);
        appLogo.setVisibility(View.VISIBLE);
        appLogo.startAnimation(animation);
        animation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        }, 2000);


    }

    private void checkUser() {


        if (!(FirebaseAuth.getInstance().getCurrentUser() == null)) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
