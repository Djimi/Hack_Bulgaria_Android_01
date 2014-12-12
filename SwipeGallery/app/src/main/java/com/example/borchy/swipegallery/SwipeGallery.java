package com.example.borchy.swipegallery;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileFilter;


public class SwipeGallery extends FragmentActivity {

    private ViewPager viewPager;
    private PicturesAdapter adapter;
    private File[] pics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_gallery);
        pics = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new PicturesAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private class PicturesAdapter extends FragmentStatePagerAdapter {

        public PicturesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return new PictureFragment(pics[i]);
        }

        @Override
        public int getCount() {
            return pics.length;
        }
    }
}
