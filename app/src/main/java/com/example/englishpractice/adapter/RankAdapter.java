package com.example.englishpractice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.englishpractice.R;
import com.example.englishpractice.model.RankDB;

import java.util.List;
import java.util.Locale;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<RankDB> userList;

    public RankAdapter(List<RankDB> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();

        holder.setData(name,score,rank);

    }

    @Override
    public int getItemCount() {

        if (userList.size() > 10) {

            return 10;

        }
        else {
            return userList.size();
        }

    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, rankTV, scoreTV, imgTV;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            nameTV = itemView.findViewById(R.id.name_rank_item);
            rankTV = itemView.findViewById(R.id.rank_num);
            scoreTV = itemView.findViewById(R.id.score_rank_item);
            imgTV = itemView.findViewById(R.id.img_text_item_rank);

        }

        private void setData (String name, int score, int rank) {

            nameTV.setText(name);
            scoreTV.setText("Score : " + score);
            rankTV.setText("Rank : " + rank);
            imgTV.setText(name.toUpperCase(Locale.ROOT).substring(0,1));

        }
    }
}
