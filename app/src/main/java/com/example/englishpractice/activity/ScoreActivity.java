package com.example.englishpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishpractice.R;
import com.example.englishpractice.fragment.LeaderFragment;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.model.QuestionDB;
import com.example.englishpractice.other.MyCompleteListener;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreTV, timeTV, totalQTV, correctQTV, wrongQTV, unattemptedQTV;
    private Button leaderB, reAttemptB, viewAnsB;
    private long timeTaken;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar =  findViewById(R.id.toolbarScore);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        loadData();

        setBookMark();

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, LeaderFragment.class);
                startActivity(intent);
            }
        });

        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);
            }
        });

        reAttemptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reAttempt();
            }
        });

        saveResult();
    }

    private void loadData() {

        int correctQ = 0, wrongQ = 0, unattemptQ = 0;
        for (int i = 0; i < DBQuery.g_questionList.size(); i++) {
            if (DBQuery.g_questionList.get(i).getSelectedAns() == -1) {
                unattemptQ++;
            } else {
                if (DBQuery.g_questionList.get(i).getSelectedAns() == DBQuery.g_questionList.get(i).getCorrectAns()) {
                    correctQ++;
                } else {
                    wrongQ++;
                }
            }
        }
        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unattemptQ));

        totalQTV.setText(String.valueOf(DBQuery.g_questionList.size()));

        finalScore = (correctQ*100)/DBQuery.g_questionList.size();
        scoreTV.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN", 0);

        String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );

        timeTV.setText(time);
    }

    private void reAttempt() {

        for (int i =0 ; i < DBQuery.g_questionList.size(); i++) {
            DBQuery.g_questionList.get(i).setSelectedAns(-1);
            DBQuery.g_questionList.get(i).setStatus(DBQuery.NOT_VISITED);

        }

        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        finish();

    }

    private void saveResult() {

        DBQuery.saveresult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "Saiii rồi a zai à??", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private  void setBookMark() {

        for (int i =0; i < DBQuery.g_questionList.size() ; i++) {

            QuestionDB question = DBQuery.g_questionList.get(i);

            if (question.isBookmarked()) {

                if (! DBQuery.g_bmIdList.contains(question.getqID())) {

                    DBQuery.g_bmIdList.add(question.getqID());
                    DBQuery.myProfile.setBookmarkCount(DBQuery.g_bmIdList.size());

                }

            }else {

                if (DBQuery.g_bmIdList.contains(question.getqID())) {

                    DBQuery.g_bmIdList.remove(question.getqID());
                    DBQuery.myProfile.setBookmarkCount(DBQuery.g_bmIdList.size());
                }

            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            ScoreActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void init() {

        scoreTV = findViewById(R.id.score);
        timeTV = findViewById(R.id.timeQ);
        totalQTV = findViewById(R.id.totalQ);
        correctQTV = findViewById(R.id.correctQ);
        wrongQTV = findViewById(R.id.wrongQ);
        unattemptedQTV = findViewById(R.id.un_attemptedQ);
        leaderB = findViewById(R.id.leaderB);
        reAttemptB = findViewById(R.id.reattemptB);
        viewAnsB = findViewById(R.id.view_answers);
    }
}