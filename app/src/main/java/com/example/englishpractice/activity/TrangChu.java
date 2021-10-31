package com.example.englishpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.englishpractice.R;
import com.example.englishpractice.fragment.AccountFragment;
import com.example.englishpractice.fragment.CategoryFragment;
import com.example.englishpractice.fragment.HomeFragment;
import com.example.englishpractice.fragment.LeaderFragment;
import com.example.englishpractice.model.DBQuery;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class TrangChu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_ACCOUNT = 1;
    private static final int FRAGMENT_LEADER = 2;
    private static final int FRAGMENT_COURSE = 3;

    private int mCurrentFragment = FRAGMENT_HOME;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private TextView drawerProfileName,drawerProfileEmail,drawerProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);


        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        toolbar =findViewById(R.id.toolbarTrangChu);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar,R.string.navigation_drawer_open ,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_email);
        drawerProfileImage = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_pic);

        String name = DBQuery.myProfile.getName();
        drawerProfileName.setText(name);
        String email = DBQuery.myProfile.getEmail();
        drawerProfileEmail.setText(email);
        drawerProfileImage.setText(name.toUpperCase().substring(0,1));

        replaceFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_course) {
            if (mCurrentFragment != FRAGMENT_COURSE) {
                replaceFragment(new CategoryFragment());
                mCurrentFragment = FRAGMENT_COURSE;
            }
        } else if (id == R.id.nav_account) {
            if (mCurrentFragment != FRAGMENT_ACCOUNT) {
                replaceFragment(new AccountFragment());
                mCurrentFragment = FRAGMENT_ACCOUNT;
            }
        }

        else if ( id == R.id.nav_bookmarks) {
            Intent intent = new Intent(getApplicationContext(), BookmarksActivity.class);
            startActivity(intent);
        } else if ( id == R.id.nav_leaderboard) {
            if (mCurrentFragment != FRAGMENT_LEADER) {
                replaceFragment(new LeaderFragment());
                mCurrentFragment = FRAGMENT_LEADER;
            }
        } else if (id == R.id.nav_LogOut) {
            FirebaseAuth.getInstance().signOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getApplicationContext(), gso);

            mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Intent intent = new Intent(getApplicationContext() , SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            });
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}