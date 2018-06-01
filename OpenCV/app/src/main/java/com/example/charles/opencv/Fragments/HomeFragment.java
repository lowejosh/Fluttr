package com.example.charles.opencv.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.charles.opencv.R;

import java.io.IOException;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);

        ViewFlipper slideShow = v.findViewById(R.id.slide_show);

        ImageView bird1 = new ImageView(slideShow.getContext());
        bird1.setImageBitmap(getBirdImage(v.getContext(),"australasian_grebe.jpg"));
        slideShow.addView(bird1);

        ImageView bird2 = new ImageView(slideShow.getContext());
        bird2.setImageBitmap(getBirdImage(v.getContext(),"australasian_shoveler.jpg"));
        slideShow.addView(bird2);

        slideShow.setFlipInterval(5000);
        slideShow.startFlipping();

        return v;
    }

    /**
     * Get the Bitmap Image of a Picture in the assets folder
     *
     * @param file Name of the image inside assets including extension
     * @return Image from file path in Bitmap form
     */
    public Bitmap getBirdImage(Context context, String file) {
        //Update image for ImageView
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(file));
        } catch (IOException unused) {
            //If duck image does not exist, display noImage file
            try {
                return BitmapFactory.decodeStream(context.getAssets().open("noImage.jpg"));
            } catch (IOException ex){
                Log.e("MainActivity", "noImage Failed to Load");
                Log.e("MainActivity", ex.getMessage());
            }
            Log.e("MainActivity", "Failed to load image: " + file);
        }

        return null;
    }
}
