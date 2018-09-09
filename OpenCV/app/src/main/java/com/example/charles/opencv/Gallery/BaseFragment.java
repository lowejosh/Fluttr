package com.example.charles.opencv.Gallery;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;


public class BaseFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER";

    public BaseFragment(){
        //
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void onFragmentInteraction(String id);
        public void onFragmentInteraction(int actionId);
    }
}