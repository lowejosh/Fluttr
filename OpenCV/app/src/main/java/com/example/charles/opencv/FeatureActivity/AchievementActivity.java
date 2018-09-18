package com.example.charles.opencv.FeatureActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.charles.opencv.Achievements.Achievement;
import com.example.charles.opencv.Achievements.AchievementAdapter;
import com.example.charles.opencv.Achievements.AchievementManager;
import com.example.charles.opencv.R;

import java.io.IOException;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements);

        updateImage((ImageView)findViewById(R.id.backButton), "back_button.png");

        AchievementManager manager = new AchievementManager(this);
        List<Achievement> achievements = manager.getAchievements();

        ListView listView = findViewById(R.id.achievement_list);
        AchievementAdapter adapter = new AchievementAdapter(this, achievements);
        listView.setAdapter(adapter);
    }

    public void goBack(View v) {
        super.finish();
    }

    /**
     * Updates the image view with a file stored in assets/icons
     * @param v ImageView
     * @param filename Filename and extension of the icon
     */
    private void updateImage(ImageView v, String filename) {
        //Update image for ImageView
        try {
            v.setImageBitmap(BitmapFactory.decodeStream(this.getAssets().open("icons/" + filename)));
        } catch (IOException | IllegalArgumentException unused) {
            Log.e("AIActivity", "Failed to load image: " + "icons/" + filename);
        }
    }
}
