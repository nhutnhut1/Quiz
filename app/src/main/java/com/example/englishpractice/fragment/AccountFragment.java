package com.example.englishpractice.fragment;



import static com.example.englishpractice.model.DBQuery.g_usersCount;
import static com.example.englishpractice.model.DBQuery.g_usersList;
import static com.example.englishpractice.model.DBQuery.myPerformance;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.example.englishpractice.R;
import com.example.englishpractice.activity.BookmarksActivity;
import com.example.englishpractice.activity.LoginActivity;
import com.example.englishpractice.activity.MyProfileActivity;
import com.example.englishpractice.activity.TrangChu;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    private LinearLayout logoutB, leaderB, profileB, bookmarksB;
    private TextView profile_img_text, name, score, rank;

    private MyProfileActivity myProfileActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(view);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbarScore);

        ((TrangChu) getActivity()).getSupportActionBar().setTitle("My Account");
        ((TrangChu) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        String userName = DBQuery.myProfile.getName();
        profile_img_text.setText(userName.toUpperCase().substring(0, 1));

        name.setText(userName);

        score.setText(String.valueOf(DBQuery.myPerformance.getScore()));

        if (DBQuery.g_usersList.size() == 0) {

            DBQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    if (myPerformance.getScore() != 0) {

                        if (!DBQuery.isMeOnTopList) {
                            calculateRank();
                        }
                        score.setText("Score : " + myPerformance.getScore());
                        rank.setText("Rank - " + myPerformance.getRank());
                    }
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "Lại sai rồi a zai à", Toast.LENGTH_SHORT).show();
                }
            });

        }else {

            score.setText("Score : " + myPerformance.getScore());
            if (myPerformance.getScore() != 0)
            rank.setText("Rank - " + myPerformance.getRank());
        }


        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getContext(), gso);

                mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();

                    }
                });
            }
        });

        bookmarksB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BookmarksActivity.class);
                startActivity(intent);
            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LeaderFragment.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void calculateRank() {

        int lowTopScore = g_usersList.get(g_usersList.size() - 1).getScore();

        int remaining_slots = g_usersCount - 20;

        int mySlot = (myPerformance.getScore() * remaining_slots) / lowTopScore;

        int rank;

        if (lowTopScore != myPerformance.getScore()) {

            rank = g_usersCount - mySlot;

        } else {

            rank = 21;

        }

        myPerformance.setRank(rank);
    }

    private void initViews(View view) {

        logoutB = view.findViewById(R.id.logoutB);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        profileB = view.findViewById(R.id.profileB);
        name = view.findViewById(R.id.name);
        score = view.findViewById(R.id.total_score);
        rank = view.findViewById(R.id.rank);
        bookmarksB = view.findViewById(R.id.bookmarkB);
        leaderB = view.findViewById(R.id.leaderB_account);


    }
}
