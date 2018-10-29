package com.example.charles.opencv.Recording;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.charles.opencv.FeatureActivity.RecordActivity;
import com.example.charles.opencv.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RecordingAdapter extends BaseAdapter {
    private File[] files;
    private LayoutInflater inflater;

    public RecordingAdapter(Context context, File[] files) {
        this.files = files;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int i) {
        return files[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.rec_item, null);

        LinearLayout layout = view.findViewById(R.id.layout);
        TextView filename = view.findViewById(R.id.filename);
        TextView filelength = view.findViewById(R.id.length);

        filename.setText(files[i].getName());
        filelength.setText(audioLength(files[i]));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecordActivity) v.getContext()).playAudio(v, files[i]);
            }
        });

        return view;
    }

    private String audioLength(File file) {
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(file.getAbsolutePath());
            mp.prepare();
            int duration = mp.getDuration();
            mp.release();

            return String.format("%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        } catch (IOException ex) {
            return "??:??";
        }
    }
}
