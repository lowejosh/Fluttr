package com.example.charles.opencv.BirdBank;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;

import java.util.List;


public class ListProductAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bird> mList;

    public ListProductAdapter(Context mContext, List<Bird> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
        ImageView birdImage = (ImageView)v.findViewById(R.id.bird_image);
        birdName.setText(mList.get(position).getName();
        birdSeen.setText("dd/mm/yy (placeholder)");
        birdImage.setImageBitmap(mList.get(position).getBirdImage(mContext));
        return v;
    }
}
