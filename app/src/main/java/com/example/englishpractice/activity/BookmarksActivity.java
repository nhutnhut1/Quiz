package com.example.englishpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.englishpractice.R;
import com.example.englishpractice.adapter.BookmarksAdapter;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;

public class BookmarksActivity extends AppCompatActivity {

    private RecyclerView questionView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);


        toolbar =  findViewById(R.id.toolbarBookmark);
        questionView = findViewById(R.id.bookmark_Recycview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle("Saved Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        questionView.setLayoutManager(linearLayoutManager);


        DBQuery.loadBookmarks(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                BookmarksAdapter adapter = new BookmarksAdapter(DBQuery.g_bookmarksList);
                questionView.setAdapter(adapter);

            }

            @Override
            public void onFailure() {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            BookmarksActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}