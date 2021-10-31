package com.example.englishpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.englishpractice.R;
import com.example.englishpractice.adapter.AnswersAdapter;
import com.example.englishpractice.model.DBQuery;

public class AnswersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView answersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        toolbar =  findViewById(R.id.toolbarAnswers);
        answersView = findViewById(R.id.answers_Recycview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ANSWERS");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(linearLayoutManager);

        AnswersAdapter adapter = new AnswersAdapter(DBQuery.g_questionList);
        answersView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            AnswersActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}