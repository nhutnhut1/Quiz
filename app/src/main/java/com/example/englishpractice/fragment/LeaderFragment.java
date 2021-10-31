package com.example.englishpractice.fragment;



import static com.example.englishpractice.model.DBQuery.g_usersCount;
import static com.example.englishpractice.model.DBQuery.g_usersList;
import static com.example.englishpractice.model.DBQuery.myPerformance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishpractice.R;
import com.example.englishpractice.activity.TrangChu;
import com.example.englishpractice.adapter.RankAdapter;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;


public class LeaderFragment extends Fragment {

    private TextView totalUsersTV, myImgTextTV, myScoreTV, myRankTV;
    private RecyclerView usersView;
    private RankAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ((TrangChu) getActivity()).getSupportActionBar().setTitle("Bảng Xếp Hạng");

        init(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        // set adapter rank
        adapter = new RankAdapter(g_usersList);
        usersView.setAdapter(adapter);

        DBQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                adapter.notifyDataSetChanged();

                if (myPerformance.getScore() != 0) {

                    if (!DBQuery.isMeOnTopList) {
                        calculateRank();
                    }
                    myScoreTV.setText("Score : " + myPerformance.getScore());
                    myRankTV.setText("Rank - " + myPerformance.getRank());
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Lại sai rồi a zai à", Toast.LENGTH_SHORT).show();
            }
        });


        totalUsersTV.setText("Total Users : " + g_usersCount);
        myImgTextTV.setText(myPerformance.getName().toUpperCase().substring(0,1));


        return view;
    }

    private void calculateRank() {

        int lowTopScore = g_usersList.get(g_usersList.size() - 1).getScore();

        int remaining_slots = g_usersCount - 20;

        int mySlot = (myPerformance.getScore()*remaining_slots) / lowTopScore;

        int rank;

        if (lowTopScore != myPerformance.getScore()) {

            rank = g_usersCount - mySlot;

        } else {

            rank = 21;

        }

        myPerformance.setRank(rank);
    }

    private void init(View view) {

        totalUsersTV = view.findViewById(R.id.total_users);
        myImgTextTV = view.findViewById(R.id.img_text_lboard);
        myScoreTV = view.findViewById(R.id.total_score_lboard);
        myRankTV = view.findViewById(R.id.rank_lboard);
        usersView = view.findViewById(R.id.users_view_lboard);

    }
}
