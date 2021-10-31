package com.example.englishpractice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.englishpractice.R;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView powereByLine;
    private ScrollView backgroundImage;

    Animation sideAnim, bottomAnim;
    private static int SPLASH_TIMER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();


        // Hooks

        backgroundImage = findViewById(R.id.background_image);
        powereByLine = findViewById(R.id.content_logo);

        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        // set anim
        backgroundImage.setAnimation(sideAnim);
        powereByLine.setAnimation(bottomAnim);

        DBQuery.g_firestore = FirebaseFirestore.getInstance();






        if (mAuth.getCurrentUser() != null) {

            DBQuery.loadData(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(getApplicationContext(), TrangChu.class));
                    finish();
                }

                @Override
                public void onFailure() {

                }
            });


        }



    }





    public void CallLoginScreen (View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.btnLogin),"transition_login");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);

        startActivity(intent, options.toBundle());

    }

    public void CallSignUpScreen (View view) {

        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.btnSignup),"transition_next_btn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);

        startActivity(intent, options.toBundle());

    }

}