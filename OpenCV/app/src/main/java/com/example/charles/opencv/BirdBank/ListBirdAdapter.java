package com.example.charles.opencv.BirdBank;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.charles.opencv.Activity.IndividualBirdActivity;
import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;

import java.util.List;


public class ListBirdAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bird> mList;
    private List<String> dateList;

    public ListBirdAdapter(Context mContext, List<Bird> mList, List<String> dateList) {
        this.mContext = mContext;
        this.mList = mList;
        this.dateList = dateList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getBirdID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.bird_listview, null);
        TextView birdName = (TextView)v.findViewById(R.id.bird_name);
        TextView birdSeen = (TextView)v.findViewById(R.id.bird_status);
        birdName.setText(mList.get(position).getName());
        birdSeen.setText("Identified on " + dateList.get(position));
        return v;
    }
}
