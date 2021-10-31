package com.example.englishpractice.activity;



import static com.example.englishpractice.model.DBQuery.ANSWERED;
import static com.example.englishpractice.model.DBQuery.NOT_VISITED;
import static com.example.englishpractice.model.DBQuery.REVIEW;
import static com.example.englishpractice.model.DBQuery.UNANSWERED;
import static com.example.englishpractice.model.DBQuery.g_cateDBList;
import static com.example.englishpractice.model.DBQuery.g_questionList;
import static com.example.englishpractice.model.DBQuery.g_selected_cat_index;
import static com.example.englishpractice.model.DBQuery.g_selected_test_index;
import static com.example.englishpractice.model.DBQuery.g_testList;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import com.example.englishpractice.R;
import com.example.englishpractice.adapter.QuestionGridAdapter;
import com.example.englishpractice.adapter.QuestionsAdapter;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView questionView;
    private TextView tvQuesID, timerTV, catNameTV;
    private Button submitB, markB, clearSelB;
    private ImageButton prevQuesB, nextQuesB, drawerCloseB;
    private ImageView quesListB;
    private int quesID;
    QuestionsAdapter quesAdapter;
    private DrawerLayout drawer;
    private GridView quesListGV;
    private QuestionGridAdapter gridAdapter;
    private ImageView markImage;
    private CountDownTimer timer;
    private long  timeLeft;
    private ImageView bookmarkB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list_layout);

        init();

        quesAdapter = new QuestionsAdapter(g_questionList);
        questionView.setAdapter(quesAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionView.setLayoutManager(layoutManager);

        gridAdapter = new QuestionGridAdapter(this, g_questionList.size());
        quesListGV.setAdapter(gridAdapter);


        setSnapHelper();

        setClickListeners();

        startTimer();
    }


    private void startTimer() {

        long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;

        timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {

                timeLeft = remainingTime;
                String time = String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );
                timerTV.setText(time);

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(getApplicationContext() , ScoreActivity.class);
                long totalTime = g_testList.get(g_selected_test_index).getTime()*60*1000;
                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();

            }
        };
        timer.start();
    }

    private void setClickListeners() {

        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesID > 0) {
                    questionView.smoothScrollToPosition(quesID - 1);
                }
            }
        });

        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesID < g_questionList.size() - 1) {
                    questionView.smoothScrollToPosition(quesID + 1);

                }
            }
        });

        clearSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                g_questionList.get(quesID).setSelectedAns(-1);
                g_questionList.get(quesID).setStatus(UNANSWERED);
                markImage.setVisibility(View.GONE);
                quesAdapter.notifyDataSetChanged();
            }
        });

        quesListB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawer.isDrawerOpen(GravityCompat.END)) {

                    gridAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        drawerCloseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });

        markB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markImage.getVisibility() != View.VISIBLE) {
                    markImage.setVisibility(View.VISIBLE);

                    g_questionList.get(quesID).setStatus(REVIEW);

                } else {

                    markImage.setVisibility(View.GONE);

                    if (g_questionList.get(quesID).getSelectedAns() != -1) {
                        g_questionList.get(quesID).setStatus(ANSWERED);
                    } else {
                        g_questionList.get(quesID).setStatus(UNANSWERED);
                    }

                }
            }
        });

        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitTest();
            }
        });
        bookmarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToBookMark();
            }
        });
    }

    private void addToBookMark() {

        if (g_questionList.get(quesID).isBookmarked()) {

            g_questionList.get(quesID).setBookmarked(false);
            bookmarkB.setImageResource(R.drawable.ic_bookmark);

        }else {

            g_questionList.get(quesID).setBookmarked(true);
            bookmarkB.setImageResource(R.drawable.ic_bookmarks_selected);

        }
    }

    private void submitTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.alert_diolog_layout , null );
        Button cancelB = view.findViewById(R.id.cancelB);
        Button comfirmB = view.findViewById(R.id.confirmB);

        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        comfirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer.cancel();
                alertDialog.dismiss();

                Intent intent = new Intent(getApplicationContext() , ScoreActivity.class);
                long totalTime = g_testList.get(g_selected_test_index).getTime()*60*1000;
                intent.putExtra("TIME_TAKEN", totalTime - timeLeft);
                startActivity(intent);
                QuestionsActivity.this.finish();

            }
        });

        alertDialog.show();
    }

    public void goToQuestion(int possition) {

        questionView.smoothScrollToPosition(possition);

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }

    }

    private void setSnapHelper() {

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionView);

        questionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                if (g_questionList.get(quesID).getStatus() == NOT_VISITED)
                    g_questionList.get(quesID).setStatus(UNANSWERED);

                if (g_questionList.get(quesID).getStatus() == REVIEW) {
                    markImage.setVisibility(View.VISIBLE);
                }else {
                    markImage.setVisibility(View.GONE);
                }

                tvQuesID.setText(String.valueOf(quesID + 1) + "/" + String.valueOf(g_questionList.size()));

                if (g_questionList.get(quesID).isBookmarked()) {
                    bookmarkB.setImageResource(R.drawable.ic_bookmarks_selected);
                }
                else {

                    bookmarkB.setImageResource(R.drawable.ic_bookmark);

                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private void init() {

        questionView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_catName);
        submitB = findViewById(R.id.submitB);
        markB = findViewById(R.id.markB);
        clearSelB = findViewById(R.id.clear_selection);
        prevQuesB = findViewById(R.id.prev_quesB);
        nextQuesB = findViewById(R.id.next_quesB);
        quesListB = findViewById(R.id.ques_list_gridB);
        drawer = findViewById(R.id.drawer_layout);
        drawerCloseB = findViewById(R.id.drawerCloseB);
        markImage = findViewById(R.id.mark_image);
        quesListGV = findViewById(R.id.ques_list_gv);
        bookmarkB = findViewById(R.id.qa_bookmarkB);

        quesID = 0;
        tvQuesID.setText("1/" + String.valueOf(g_questionList.size()));
        catNameTV.setText(g_cateDBList.get(g_selected_cat_index).getName());

        g_questionList.get(0).setStatus(UNANSWERED);

        if (g_questionList.get(0).isBookmarked()) {
            bookmarkB.setImageResource(R.drawable.ic_bookmarks_selected);
        }
        else {

            bookmarkB.setImageResource(R.drawable.ic_bookmark);

        }
    }
}