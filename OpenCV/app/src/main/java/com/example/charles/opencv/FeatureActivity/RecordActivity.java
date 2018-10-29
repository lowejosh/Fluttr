package com.example.charles.opencv.FeatureActivity;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charles.opencv.Database.AchievementsDatabase;
import com.example.charles.opencv.R;
import com.example.charles.opencv.Recording.RecordingAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Shows the settings available to the user, this currently only displays the references for information
 * gathered in the database and images.
 */
public class RecordActivity extends AppCompatActivity {
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;
    private static SimpleDateFormat format = new SimpleDateFormat("ddmmyyyyhhmmss");

    private static String folder = "fluttr_audio";

    private Button recordButton;
    private Button stopButton;
    private TextView currentlyPlaying;
    private TextView currentlyPlayingAudio;
    private TextView noRecordings;

    private static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);

        //Retrieve important objects
        recordButton = findViewById(R.id.startBtn);
        stopButton = findViewById(R.id.stopBtn);
        currentlyPlaying = findViewById(R.id.currently_playing);
        currentlyPlayingAudio = findViewById(R.id.currently_playing_audio);
        noRecordings = findViewById(R.id.no_recordings);

        stopButton.setEnabled(false);
        currentlyPlaying.setVisibility(View.GONE);
        currentlyPlayingAudio.setVisibility(View.GONE);
        noRecordings.setVisibility(View.GONE);

        makeDirectory();
        updateAudioFiles();
    }

    public void recordAudio(View v) {
        if (!checkPermission()) {
            requestPermission();
            return;
        }

        recordButton.setEnabled(false);
        stopButton.setEnabled(true);

        Date currentTime = Calendar.getInstance().getTime();
        String audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/" + format.format(currentTime)+ "Recording.mp3";

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(RecordActivity.this, "Recording Started", Toast.LENGTH_LONG).show();
    }

    public void stopAudio(View v) {
        stopButton.setEnabled(false);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        recordButton.setEnabled(true);

        updateAudioFiles();
        Toast.makeText(RecordActivity.this, "Recording Completed", Toast.LENGTH_LONG).show();
    }

    private void stopPlaying() {
        currentlyPlaying.setVisibility(View.GONE);
        currentlyPlayingAudio.setVisibility(View.GONE);
        LinearLayout createRecording = findViewById(R.id.create_recording);
        createRecording.setVisibility(View.VISIBLE);

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

        AchievementsDatabase db = new AchievementsDatabase(this.getApplicationContext());
        db.incrementAchievement(AchievementsDatabase.RECORD);
    }

    public void playAudio(View v, File file) {
        if (!checkPermission()) {
            requestPermission();
            return;
        }

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
            noRecordings.setVisibility(View.VISIBLE);
        } else {
            ListView listView = findViewById(R.id.list_recordings);
            RecordingAdapter adapter = new RecordingAdapter(this, files);
            listView.setAdapter(adapter);
        }
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean ReadPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission && ReadPermission) {
                        Toast.makeText(RecordActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED;
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
