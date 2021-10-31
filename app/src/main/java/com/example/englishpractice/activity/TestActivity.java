package com.example.englishpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishpractice.R;
import com.example.englishpractice.adapter.TestAdapter;

import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;

public class TestActivity extends AppCompatActivity {

    private RecyclerView tests_recycView;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        tests_recycView = findViewById(R.id.tests_Recycview);
        toolbar =  findViewById(R.id.toolbarTests);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DBQuery.g_cateDBList.get(DBQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        tests_recycView.setLayoutManager(linearLayoutManager);

        dialogText = findViewById(R.id.progressBar_text);
        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.progress_bar_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.progressBar_text);
        dialogText.setText("Đang tải dử liệu.....");

        progressDialog.show();


        DBQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                DBQuery.loadMyScore(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        adapter = new TestAdapter(DBQuery.g_testList);
                        tests_recycView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Thất Bại", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "Thất Bại", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            TestActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}