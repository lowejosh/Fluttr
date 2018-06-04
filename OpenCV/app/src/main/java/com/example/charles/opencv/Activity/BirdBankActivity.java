package com.example.charles.opencv.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.charles.opencv.BirdBank.ListBirdAdapter;
import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;

import java.util.List;

public class BirdBankActivity extends AppCompatActivity {
    private ListView lvBird;
    private ListBirdAdapter adapter;
    private List<Bird> mList;
    private List<String> dateList;
    private Database mDBHelper;


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
    }
}
