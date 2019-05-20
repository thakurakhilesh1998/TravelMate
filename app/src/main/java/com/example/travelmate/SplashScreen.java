package com.example.travelmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        }, 1500);
    }

    private void checkUser() {
        if (!(FirebaseAuth.getInstance().getCurrentUser() == null)) {
            FirebaseDatabase.getInstance().getReference().child("User Profile").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Log.e("firebase child", String.valueOf(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())));
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {

                        Snackbar.make(findViewById(android.R.id.content), "User Profile is not completed,so register again", Snackbar.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().getCurrentUser().delete();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Snackbar.make(findViewById(android.R.id.content), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
