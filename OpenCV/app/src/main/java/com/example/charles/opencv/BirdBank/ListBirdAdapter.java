package com.example.charles.opencv.BirdBank;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charles.opencv.Activity.IndividualBirdActivity;
import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;

import java.util.List;


public class ListBirdAdapter extends BaseAdapter implements ListAdapter {
    private Context mContext;
    private List<Bird> mList;
    private List<String> dateList;
    public static Bird mBirdClicked;
    public static String mDateOfBirdClicked;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bird_listview, null);
        }

        ConstraintLayout constraintLayout = (ConstraintLayout)view.findViewById(R.id.list_item_constraint);
        TextView birdName = (TextView)view.findViewById(R.id.bird_name);
        TextView birdSeen = (TextView)view.findViewById(R.id.bird_status);
        birdName.setText(mList.get(position).getName());
        birdSeen.setText("Identified on " + dateList.get(position));

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBirdClicked = mList.get(position);
                mDateOfBirdClicked = dateList.get(position);
                Intent intent = new Intent(mContext, IndividualBirdActivity.class);
                mContext.startActivity(intent);
            }
        });


        return view;
    }
}
