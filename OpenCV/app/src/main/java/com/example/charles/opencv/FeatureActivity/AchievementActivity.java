package com.example.charles.opencv.FeatureActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charles.opencv.Achievements.Achievement;
import com.example.charles.opencv.Achievements.AchievementAdapter;
import com.example.charles.opencv.Achievements.AchievementManager;
import com.example.charles.opencv.R;

import java.util.List;

public class AchievementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements);

        AchievementManager manager = new AchievementManager(this);
        List<Achievement> achievements = manager.getAchievements();

        ListView listView = findViewById(R.id.achievement_list);
        AchievementAdapter adapter = new AchievementAdapter(this, achievements);
        listView.setAdapter(adapter);

        TextView percentage = findViewById(R.id.achievement_percentage);
        percentage.setText(String.valueOf(manager.getCompletion()) + "%");
    }

    public void goBack(View v) {
        super.finish();
    }
}
