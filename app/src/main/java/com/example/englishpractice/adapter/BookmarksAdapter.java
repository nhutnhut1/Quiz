package com.example.englishpractice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.englishpractice.R;
import com.example.englishpractice.model.QuestionDB;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {

    private List<QuestionDB> questionList;

    public BookmarksAdapter(List<QuestionDB> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item_layout,parent,false);


        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String ques = questionList.get(position).getQuestion();
        String a = questionList.get(position).getOptionA();
        String b = questionList.get(position).getOptionB();
        String c = questionList.get(position).getOptionC();
        String d = questionList.get(position).getOptionD();
        int ans = questionList.get(position).getCorrectAns();


        holder.setData(position, ques, a, b, c, d, ans);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView  quesNo , questionA, questionB, questionC, questionD, result, question;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quesNo = itemView.findViewById(R.id.ques_no);
            question = itemView.findViewById(R.id.question_item_answers);
            questionA = itemView.findViewById(R.id.optionA_item_answers);
            questionB = itemView.findViewById(R.id.optionB_item_answers);
            questionC = itemView.findViewById(R.id.optionC_item_answers);
            questionD = itemView.findViewById(R.id.optionD_item_answers);
            result = itemView.findViewById(R.id.result_answers);

        }

        private void setData(int pos, String ques, String a, String b, String c, String d, int corrcectAns) {

            quesNo.setText("Question No. " + String.valueOf(pos + 1));
            question.setText(ques);
            questionA.setText("A. " + a);
            questionB.setText("B. " + b);
            questionC.setText("C. " + c);
            questionD.setText("D. " + d);

            if (corrcectAns == 1) {

                result.setText("ANSWER : " + a);

            }else if (corrcectAns == 2) {

                result.setText("ANSWER : " + b);

            } else if (corrcectAns == 3) {

                result.setText("ANSWER : " + c);

            }else  {
                result.setText("ANSWER : " + d);

            }

        }

    }


}
