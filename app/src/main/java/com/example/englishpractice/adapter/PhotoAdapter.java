package com.example.englishpractice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.englishpractice.R;
import com.example.englishpractice.model.PhotoDB;


import java.util.List;

public class PhotoAdapter extends PagerAdapter {

    private Context context;
    private List<PhotoDB> mListPhoto;

    public PhotoAdapter(Context context, List<PhotoDB> mListPhoto) {
        this.context = context;
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo,container,false);

        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        PhotoDB photo = mListPhoto.get(position);
        if (photo != null) {

            Glide.with(context).load(photo.getResultID()).into(imgPhoto);

        }
        // Add view to view group
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {

        if (mListPhoto != null){

            return mListPhoto.size();

        }



        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        // remove view
        container.removeView((View) object);
    }
}
