package com.example.englishpractice.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.englishpractice.R;
import com.example.englishpractice.adapter.PhotoAdapter;
import com.example.englishpractice.model.PhotoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<PhotoDB> mlistPhoto;
    private Timer mTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container , false);


        viewPager = view.findViewById(R.id.viewpaper);
        circleIndicator = view.findViewById(R.id.circle_indicator);

        mlistPhoto = getListPhoto();
        photoAdapter = new PhotoAdapter(getContext(), mlistPhoto);
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);

        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoSlideImages();

        return view;
    }

    private List<PhotoDB> getListPhoto() {
        List<PhotoDB> list = new ArrayList<>();
        list.add(new PhotoDB(R.drawable.quangcao1));
        list.add(new PhotoDB(R.drawable.quangcao2));
        list.add(new PhotoDB(R.drawable.quangcao3));

        return list;
    }

    private void autoSlideImages() {

        if (mlistPhoto == null || mlistPhoto.isEmpty() || viewPager == null ) {

            return;

        }

        if (mTimer == null) {

            mTimer = new Timer();

        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mlistPhoto.size() -1;
                        if (currentItem < totalItem) {

                            currentItem++;
                            viewPager.setCurrentItem(currentItem);

                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500,3000);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mTimer != null) {

            mTimer.cancel();
            mTimer = null;

        }
    }
}
