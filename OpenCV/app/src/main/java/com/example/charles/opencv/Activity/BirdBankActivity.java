package com.example.charles.opencv.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.charles.opencv.BirdBank.ListBirdAdapter;
import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;
import com.example.charles.opencv.Database.Database;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;

public class BirdBankActivity extends AppCompatActivity {
    private ListView lvBird;
    private ListBirdAdapter adapter;
    private List<Bird> mList;
    private List<String> dateList;
    private Database mDBHelper;
    public static Bird mBirdClicked;
    public static String mDateOfBirdClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdbank);

        // Init vars
        Database db = new Database(this);
        mList = db.getSeenBirdList();
        dateList = db.getSeenBirdDateList();
        lvBird = (ListView)findViewById(R.id.listview_birdbank);

        // Initiate and set the adapter
        adapter = new ListBirdAdapter(this, mList, dateList);
        lvBird.setAdapter(adapter);


        // ListView Item Click Listener
        lvBird.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                mBirdClicked = mList.get(position);
                mDateOfBirdClicked = dateList.get(position);
                Intent intent = new Intent (lvBird.getContext(),IndividualBirdActivity.class);
                startActivity(intent);


            }

        });


    }



}
