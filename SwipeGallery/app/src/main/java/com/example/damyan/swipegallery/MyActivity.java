package com.example.damyan.swipegallery;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;


public class MyActivity extends FragmentActivity {

    ViewPager viewPager;
    ArrayList<Drawable> pictures;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        viewPager = (ViewPager)findViewById(R.id.viewPager);

        File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), files);

        viewPager.setAdapter(adapter);


    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public static class MyFragment extends Fragment{

        File picture;

        View view;
        public MyFragment(){
        }


        public MyFragment(File picture) {
            this.picture = picture;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_layout, container, false);

            ((ImageView)view.findViewById(R.id.picture)).setImageURI(Uri.parse(picture.getAbsolutePath()));


//            ((ImageView)view.findViewById(R.id.picture)).setImageBitmap(BitmapFactory.decodeFile(picture.getPath()));

            return view;
        }

    }

    class MyAdapter extends FragmentPagerAdapter {


        MyFragment[] fragments;

        public MyAdapter(FragmentManager fm, File[] files) {
            super(fm);

            fragments = new MyFragment[files.length];

            for(int i = 0; i < files.length; ++i){
                fragments[i] = new MyFragment(files[i]);
            }

        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }


        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}