package com.example.charles.opencv.FeatureActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charles.opencv.R;
import com.example.charles.opencv.Recording.RecordingAdapter;
import com.example.charles.opencv.Settings.ReferenceActivity;
import com.example.charles.opencv.Settings.ResetActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Shows the settings available to the user, this currently only displays the references for information
 * gathered in the database and images.
 */
public class RecordActivity extends AppCompatActivity {
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;
    private static SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");

    private static String folder = "fluttr_audio";

    private Button recordButton;
    private Button stopButton;
    private TextView currentlyPlaying;
    private TextView currentlyPlayingAudio;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);

        recordButton = findViewById(R.id.startBtn);
        stopButton = findViewById(R.id.stopBtn);
        currentlyPlaying = findViewById(R.id.currently_playing);
        currentlyPlayingAudio = findViewById(R.id.currently_playing_audio);

        if (!hasMicrophone()) {
            stopButton.setEnabled(false);
            stopButton.setEnabled(false);
        } else {
            stopButton.setEnabled(false);
        }

        verifyStoragePermissions(this);

        makeDirectory();
        updateAudioFiles();
    }

    public void recordAudio(View v) {
        stopButton.setEnabled(true);

        Date currentTime = Calendar.getInstance().getTime();

        String audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/" + format.format(currentTime)+ " Recording.3pg";

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
        recordButton.setEnabled(false);
    }

    public void stopAudio(View v) {
        stopButton.setEnabled(false);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        recordButton.setEnabled(true);

        updateAudioFiles();
    }

    private void stopPlaying() {
        currentlyPlaying.setVisibility(View.GONE);
        currentlyPlayingAudio.setVisibility(View.GONE);
        LinearLayout createRecording = findViewById(R.id.create_recording);
        createRecording.setVisibility(View.VISIBLE);

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public void playAudio(View v, File file) {
        currentlyPlaying.setVisibility(View.VISIBLE);
        currentlyPlayingAudio.setVisibility(View.VISIBLE);
        currentlyPlayingAudio.setText(file.getName());
        LinearLayout createRecording = findViewById(R.id.create_recording);
        createRecording.setVisibility(View.GONE);

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            Log.e("RecordActivity", "Failed to find audio file");
            updateAudioFiles();
        }
    }

    /**
     * Checks if audio directory exists and if not, creates folder
     */
    private void makeDirectory() {
        if (!(new File(Environment.getExternalStorageDirectory().getPath() + "/" + folder)).isDirectory()) {
            //Create directory
            File directory = new File(Environment.getExternalStorageDirectory().getPath(), folder);
            directory.mkdirs();
        }
    }

    /**
     * Retrieve list of audio files, null if none found
     * @return List of audio files
     */
    private File[] getFiles() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + folder);
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files == null || files.length == 0) {
                Log.d("AudioTest", "No files found in fluttr_audio folder");
            } else {
                return files;
            }
        } else {
            Log.e("RecordActivity", "fluttr_audio has not been initialised as a directory");
        }

        return null;
    }

    private void updateAudioFiles() {
        File[] files = getFiles();
        if (files == null) {
            TextView noResults = findViewById(R.id.no_recordings);
            noResults.setVisibility(View.VISIBLE);
        } else {
            ListView listView = findViewById(R.id.list_recordings);
            RecordingAdapter adapter = new RecordingAdapter(this, files);
            listView.setAdapter(adapter);
        }
    }

    /**
     * Check user has given microphone permissions
     * @return True if microphone permissions are given
     */
    protected boolean hasMicrophone() {
        PackageManager packageManager = this.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Back on click function for the "GO BACK" button
     *
     * @param v View of the button
     */
    public void goBack(View v) {
        super.finish();
    }
}
