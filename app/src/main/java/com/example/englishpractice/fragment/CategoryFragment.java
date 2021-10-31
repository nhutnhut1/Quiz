package com.example.englishpractice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishpractice.R;
import com.example.englishpractice.adapter.CategoryAdapter;
import com.example.englishpractice.model.DBQuery;


public class CategoryFragment extends Fragment {

    private GridView cate_grid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        cate_grid = view.findViewById(R.id.cate_grid);

//        loadCategory();
//
        CategoryAdapter adapter = new CategoryAdapter(DBQuery.g_cateDBList);
        cate_grid.setAdapter(adapter);


        return view;
    }

    private void loadCategory() {

//        cateDBList.clear();
//
//        cateDBList.add(new CategoryDB("1","Từ Vựng",25));
//        cateDBList.add(new CategoryDB("2","Danh Từ",20));
//        cateDBList.add(new CategoryDB("3","Danh Từ",20));
//        cateDBList.add(new CategoryDB("4","Danh Từ",20));
//        cateDBList.add(new CategoryDB("5","Danh Từ",20));
    }
}
