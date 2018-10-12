package com.example.charles.opencv.FeatureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charles.opencv.BirdBank.ListBirdAdapter;
import com.example.charles.opencv.Database.AchievementsDatabase;
import com.example.charles.opencv.Database.BirdBankDatabase;
import com.example.charles.opencv.R;
import com.example.charles.opencv.Tables.Bird;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity is the self contained file for the Bird Bank Feature.
 */
public class BirdBankActivity extends AppCompatActivity {

    // temp global
    List<Bird> mList = new ArrayList<>();
    List<String> dateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdbank);

        /*//toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bird Bank");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeScreen.class));
            }
        });*/


        // Init vars
        BirdBankDatabase db = new BirdBankDatabase(this);

        try {
            mList = db.getSeenBirdList();
            dateList = db.getSeenBirdDateList();
        } catch (RuntimeException e) {
            // If the table doesn't exist - create one
            db.onCreate(db.getWritableDatabase());
        }


        //If no Birds have been found, display message
        TextView tv_no_birds = findViewById(R.id.no_birds);
        Button btn_share = findViewById(R.id.btn_share);
        if (mList.size() == 0) {
            tv_no_birds.setVisibility(View.VISIBLE);
            btn_share.setVisibility(View.GONE);
        } else {
            tv_no_birds.setVisibility(View.GONE);
            btn_share.setVisibility(View.VISIBLE);
        }

        //Get list view
        ListView lvBird = findViewById(R.id.listview_birdbank);

        // Initiate and set the adapter
        ListBirdAdapter adapter = new ListBirdAdapter(this, mList, dateList);
        lvBird.setAdapter(adapter);
    }

    public void onClickShare(View v) {
        int birdsFound = mList.size();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! I have identified " + birdsFound + " birds with Fluttr!\nCheck it out here: (placeholder for link)");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

        AchievementsDatabase db = new AchievementsDatabase(this.getApplicationContext());
        db.incrementAchievement(db.SHARE);
    }

    /**
     * OnClick function for bf_failure button and bf_topresults button
     *
     * @param v View attached to the onclick
     */
    public void goBack(View v) {
        super.finish();
    }
}
