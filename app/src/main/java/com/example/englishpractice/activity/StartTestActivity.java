package com.example.englishpractice.activity;

import static com.example.englishpractice.model.DBQuery.g_cateDBList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishpractice.R;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName , testNo , bestScore , time , totalQ;
    private Button startTestBtn;
    private ImageView backB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);


        init();

        DBQuery.loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                setData();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "Sai Rồi A Zai À", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void init() {

        catName = findViewById(R.id.str_test_cate_name);
        testNo = findViewById(R.id.str_test_no);
        bestScore = findViewById(R.id.str_test_best_score);
        time = findViewById(R.id.str_test_time);
        startTestBtn = findViewById(R.id.start_testBtn);
        backB = findViewById(R.id.strTest_back_button);
        totalQ = findViewById(R.id.str_total_ques);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });

        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData() {

        catName.setText(g_cateDBList.get(DBQuery.g_selected_cat_index).getName());
        testNo.setText("Test No. " + String.valueOf(DBQuery.g_selected_test_index + 1));
        totalQ.setText(String.valueOf(DBQuery.g_questionList.size()));
        bestScore.setText(String.valueOf(DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime()));

    }
}