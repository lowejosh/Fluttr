package com.example.charles.opencv.Achievements;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.charles.opencv.R;

import java.util.List;

public class AchievementAdapter extends BaseAdapter {
    private List<Achievement> achievements;
    private LayoutInflater inflater;

    public AchievementAdapter(Context context, List<Achievement> achievements) {
        this.achievements = achievements;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return achievements.size();
    }

    @Override
    public Object getItem(int i) {
        return achievements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.ach_listview, null);
        Achievement achievement = achievements.get(i);

        TextView title = view.findViewById(R.id.tv_title);
        TextView description = view.findViewById(R.id.tv_description);
        TextView progress = view.findViewById(R.id.tv_progress);
        ImageView completed = view.findViewById(R.id.iv_complete);
        ProgressBar bar = view.findViewById(R.id.pb_progress);

        title.setText(achievement.getName());
        description.setText(achievement.getDescription());

        if (achievement.isSingular()) {
            progress.setVisibility(View.GONE);
            bar.setVisibility(View.GONE);
        } else {
            //Progress
            MultiStepAchievement multi = (MultiStepAchievement)(achievement);
            progress.setText(String.format("%s/%s", String.valueOf(multi.getCurrentValue()), String.valueOf(multi.getMaxValue())));

            //Progress Bar
            bar.setMax(multi.getMaxValue());
            bar.setProgress(multi.getCurrentValue());

            //If the achievement is only partially complete, display an orange circle
            if (multi.partialComplete()) {
                completed.setImageResource(R.drawable.orange_rounded_button);
            }
        }

        if (achievement.isComplete()) {
            //Update completed imageview with green image
            completed.setImageResource(R.drawable.positive_rounded_button);
        }

        return view;
    }
}
