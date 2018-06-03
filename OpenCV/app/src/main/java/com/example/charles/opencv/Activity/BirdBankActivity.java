package com.example.charles.opencv.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.charles.opencv.BirdBank.ListBirdAdapter;
import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;
import com.example.charles.opencv.Database.Database;

import java.io.InputStream;
import java.util.List;

public class BirdBankActivity extends AppCompatActivity {
    private ListView lvBird;
    private ListBirdAdapter adapter;
    private List<Bird> mList;
    private Database mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdbank);

        // Init database and bird list
        mDBHelper = new Database(this);
        mList = mDBHelper.getBirdList();
        lvBird = (ListView)findViewById(R.id.listview_birdbank);

        // Initiate and set the adapter
        adapter = new ListBirdAdapter(this, mList);
        lvBird.setAdapter(adapter);
    }
}
