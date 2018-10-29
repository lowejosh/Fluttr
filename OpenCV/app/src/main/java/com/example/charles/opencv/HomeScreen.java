package com.example.charles.opencv;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.charles.opencv.FeatureActivity.AIActivity;
import com.example.charles.opencv.FeatureActivity.AchievementActivity;
import com.example.charles.opencv.FeatureActivity.BirdBankActivity;
import com.example.charles.opencv.FeatureActivity.BirdFinderActivity;
import com.example.charles.opencv.FeatureActivity.ProfileActivity;
import com.example.charles.opencv.FeatureActivity.RecordActivity;
import com.example.charles.opencv.FeatureActivity.SettingActivity;
import com.example.charles.opencv.FeatureActivity.NewsActivity;

import java.util.ArrayList;

/**
 * Main Menu Controls
 */
public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Fluttr");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_voice_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Choose Feature");
                startActivityForResult(intent, 3432);
            }
        });

        /*Bitmap image = null;

        try {
            image = BitmapFactory.decodeStream(this.getAssets().open("images/black_swan.jpg"));
        } catch (IOException ex) {
            Log.i("OnCreate", "Image didnt load.");
        }

        GoogleAI ai = new GoogleAI();
        List<Integer> list = ai.identify(image);

        for (Integer ID : list) {
            Log.i("Birds", String.valueOf(ID));
        }
        ai.searchBirds(this);*/
    }

    /**
     * On Button Press, Change Screen to Profile
     * @param v Attached View Object (Unused)
     */
    public void profileOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, ProfileActivity.class));
    }

    /**
     * On Button Press, Change Screen to Bird Finder
     * @param v Attached View Object (Unused)
     */
    public void finderOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, BirdFinderActivity.class));
    }

    /**
     * On Button Press, Change Screen to Search AI
     * @param v Attached View Object (Unused)
     */
    public void identifierOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, AIActivity.class));
    }

    /**
     * On Button Press, Change Screen to Bird Bank
     * @param v Attached View Object (Unused)
     */
    public void bankOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, BirdBankActivity.class));
    }

    /**
     * On Button Press, Change Screen to Settings
     * @param v Attached View Object (Unused)
     */
    public void settingOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, SettingActivity.class));
    }

    /**
     * On Button Press, Change Screen to Achievements
     * @param v Attached View Object (Unused)
     */
    public void achievementOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, AchievementActivity.class));
    }

    /**
     * On Button Press, Change Screen to News Feed
     * @param v Attached View Object (Unused)
     */
    public void newsOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, NewsActivity.class));
    }

    /**
     * On Button Press, Change Screen to Recordings
     * @param v Attached View Object (Unused)
     */
    public void recordOnClick(View v) {
        startActivity(new Intent(HomeScreen.this, RecordActivity.class));
    }

    /**
     * On Button Press, Start Voice Controls
     * @param v Attached View Object (Unused)
     */
    public void voiceClick(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Choose Feature");
        startActivityForResult(intent, 3432);
    }

//temp TODO abstract to another class (if possible)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 3432){
            if (resultCode == RESULT_OK){
                ArrayList<String> matches = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (matches.size() == 0) {
                    // didn't hear anything
                } else {
                    String mostLikelyThingHeard = matches.get(0);
                    // toUpperCase() used to make string comparison equal
                    if(mostLikelyThingHeard.toUpperCase().contains("BANK")){
                        startActivity(new Intent(this, BirdBankActivity.class));
                    } else if(mostLikelyThingHeard.toUpperCase().contains("FINDER")){
                        startActivity(new Intent(this, BirdFinderActivity.class));
                    } else if(mostLikelyThingHeard.toUpperCase().contains("VISUAL")){
                        startActivity(new Intent(this, AIActivity.class));
                    } else if(mostLikelyThingHeard.toUpperCase().contains("SETTINGS")){
                        startActivity(new Intent(this, SettingActivity.class));
                    } else if(mostLikelyThingHeard.toUpperCase().contains("PROFILE")){
                        startActivity(new Intent(this, ProfileActivity.class));
                    } else if(mostLikelyThingHeard.toUpperCase().contains("ACHIEVEMENTS")){
                        startActivity(new Intent(this, AchievementActivity.class));
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
