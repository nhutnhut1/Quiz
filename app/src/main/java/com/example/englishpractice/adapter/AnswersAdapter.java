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

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<QuestionDB> questionList;

    public AnswersAdapter(List<QuestionDB> questionList) {
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
        int selected = questionList.get(position).getSelectedAns();
        int ans = questionList.get(position).getCorrectAns();


        holder.setData(position, ques, a, b, c, d, selected, ans);
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

        private void setData(int pos, String ques, String a, String b, String c, String d, int selected, int corrcectAns) {

            quesNo.setText("Question No. " + String.valueOf(pos + 1));
            question.setText(ques);
            questionA.setText("A. " + a);
            questionB.setText("B. " + b);
            questionC.setText("C. " + c);
            questionD.setText("D. " + d);

            if (selected == -1) {
                result.setText("UN-ANSWERS");
                result.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                setOptionColor(selected, R.color.text_normal);
            }
            else {

                if (selected == corrcectAns) {

                    result.setText("CORRECT");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.green));
                    setOptionColor(selected, R.color.green);
                }else {
                    result.setText("WRONG");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                    setOptionColor(selected, R.color.red);
                }

            }
        }
        private void setOptionColor(int selected, int color) {

//            switch (selected)
//            {
//                case 1:
//                    questionA.setTextColor(itemView.getContext().getResources().getColor(color));
//                    break;
//
//                case 2:
//                    questionB.setTextColor(itemView.getContext().getResources().getColor(color));
//                    break;
//
//                case 3:
//                    questionC.setTextColor(itemView.getContext().getResources().getColor(color));
//                    break;
//
//                case 4:
//                    questionD.setTextColor(itemView.getContext().getResources().getColor(color));
//                    break;
//
//                default:
//                    break;
//            }

            if (selected == 1) {

                questionA.setTextColor(itemView.getContext().getResources().getColor(color));
            }
            else {

                questionA.setTextColor(itemView.getContext().getResources().getColor(R.color.text_normal));
            }

            if (selected == 2) {

                questionB.setTextColor(itemView.getContext().getResources().getColor(color));

            }
            else {

                questionB.setTextColor(itemView.getContext().getResources().getColor(R.color.text_normal));
            }

            if (selected == 3) {

                questionC.setTextColor(itemView.getContext().getResources().getColor(color));

            }
            else {

                questionC.setTextColor(itemView.getContext().getResources().getColor(R.color.text_normal));
            }

            if (selected == 4) {

                questionD.setTextColor(itemView.getContext().getResources().getColor(color));

            }
            else {

                questionD.setTextColor(itemView.getContext().getResources().getColor(R.color.text_normal));
            }

        }
    }


}
