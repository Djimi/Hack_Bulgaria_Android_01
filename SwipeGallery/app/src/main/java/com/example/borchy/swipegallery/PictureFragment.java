package com.example.borchy.swipegallery;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

public class PictureFragment extends Fragment {

    File pic;

    public PictureFragment(File pic) {
        this.pic = pic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_picture, null);
        ImageView img = (ImageView) res.findViewById(R.id.picture);
        img.setImageBitmap(BitmapFactory.decodeFile(pic.getPath()));
        return res;
    }
}