package com.example.englishpractice.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.englishpractice.R;
import com.example.englishpractice.activity.StartTestActivity;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.model.TestDB;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<TestDB> testDBList;

    public TestAdapter(List<TestDB> testDBList) {
        this.testDBList = testDBList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);


        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int progress = testDBList.get(position).getTopScore();

        holder.setData(position, progress);

    }

    @Override
    public int getItemCount() {
        return testDBList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView testNo;
        private TextView topScore;
        private ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            testNo = itemView.findViewById(R.id.testNo_test);
            topScore = itemView.findViewById(R.id.scoretext);
            progressBar = itemView.findViewById(R.id.progress_Tests);


        }

        private void setData(int pos, int progress) {
            testNo.setText("Test No : " + String.valueOf(pos + 1));
            topScore.setText(String.valueOf(progress) + "%");

            progressBar.setProgress(progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DBQuery.g_selected_test_index = pos;

                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }


}
