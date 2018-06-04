package com.example.charles.opencv.BirdBank;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        //ImageView birdImage = (ImageView)v.findViewById(R.id.bird_image);
        birdName.setText(mList.get(position).getName());
        birdSeen.setText("Identified on " + dateList.get(position));
        //birdImage.setImageBitmap(mList.get(position).getBirdImage(mContext));
        return v;
    }
}
