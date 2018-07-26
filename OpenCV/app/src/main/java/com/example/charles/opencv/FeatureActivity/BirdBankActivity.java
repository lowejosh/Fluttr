package com.example.charles.opencv.FeatureActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charles.opencv.BirdBank.ListBirdAdapter;
import com.example.charles.opencv.BirdFinder.Bird;
import com.example.charles.opencv.Database.BirdBankDatabase;
import com.example.charles.opencv.R;

import java.util.List;

public class BirdBankActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdbank);

        // Init vars
        BirdBankDatabase db = new BirdBankDatabase(this);
        List<Bird> mList = db.getSeenBirdList();
        List<String> dateList = db.getSeenBirdDateList();

        //If no Birds have been found, display message
        TextView tv_no_birds = findViewById(R.id.no_birds);
        if (mList.size() == 0) {
            tv_no_birds.setVisibility(View.VISIBLE);
        } else {
            tv_no_birds.setVisibility(View.GONE);
        }

        //Get list view
        ListView lvBird = findViewById(R.id.listview_birdbank);

        // Initiate and set the adapter
        ListBirdAdapter adapter = new ListBirdAdapter(this, mList, dateList);
        lvBird.setAdapter(adapter);
    }
}
