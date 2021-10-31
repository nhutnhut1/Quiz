package com.example.englishpractice.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.englishpractice.R;
import com.example.englishpractice.activity.TestActivity;
import com.example.englishpractice.model.CategoryDB;
import com.example.englishpractice.model.DBQuery;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    public CategoryAdapter(List<CategoryDB> cateDB_list) {
        this.cateDB_list = cateDB_list;
    }

    private List<CategoryDB> cateDB_list;

    @Override
    public int getCount() {
        return cateDB_list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View myView;
        if (view == null) {
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cate_item, viewGroup , false) ;
        }
        else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBQuery.g_selected_cat_index = i ;
                Intent intent = new Intent(view.getContext() , TestActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        TextView cateName = myView.findViewById(R.id.cate_Name);
        TextView cate_no_of_tests = myView.findViewById(R.id.cate_no_of_tests);

        cateName.setText(cateDB_list.get(i).getName());
        cate_no_of_tests.setText(String.valueOf(cateDB_list.get(i).getNoOfTests()) + " Tests" );



        return myView;
    }
}
