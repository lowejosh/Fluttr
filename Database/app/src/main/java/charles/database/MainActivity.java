package charles.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import charles.database.adapter.ResultListAdapter;
import charles.database.database.DatabaseHelper;
import charles.database.model.Duck;
import charles.database.model.FeatureOptions;
import charles.database.model.Question;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHandler;
    private int questionNo;
    private Question question;
    private List<Button> buttons = new ArrayList<>();
    private List<Integer> duckIDs;
    private List<Question> questions;
    private List<Integer> features = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tq_question);
        dbHandler = new DatabaseHelper(this);

        //Check database exists
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);

        if (!database.exists()) {
            dbHandler.getReadableDatabase();

            if (copyDatabase(this)) {
                Toast.makeText(this, "Copy database success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //test20Q();

        setContentView(R.layout.tq_question);

        setButtons();
        duckIDs = dbHandler.getDuckIDs();
        questions = dbHandler.getListQuestions();
        questionNo = 0;
        nextQuestion();
    }

    private void setButtons() {
        Log.d("MainActivity", "setButtons");
        buttons.clear();
        buttons.add((Button)findViewById(R.id.btn_option_1));
        buttons.add((Button)findViewById(R.id.btn_option_2));
        buttons.add((Button)findViewById(R.id.btn_option_3));
        buttons.add((Button)findViewById(R.id.btn_option_4));
        buttons.add((Button)findViewById(R.id.btn_option_5));
        buttons.add((Button)findViewById(R.id.btn_option_6));
        buttons.add((Button)findViewById(R.id.btn_option_7));
        buttons.add((Button)findViewById(R.id.btn_option_8));
        buttons.add((Button)findViewById(R.id.btn_option_9));
        buttons.add((Button)findViewById(R.id.btn_option_10));
        buttons.add((Button)findViewById(R.id.btn_option_11));
        buttons.add((Button)findViewById(R.id.btn_option_12));
        buttons.add((Button)findViewById(R.id.btn_option_13));
        buttons.add((Button)findViewById(R.id.btn_option_14));

        for (Button btn : buttons) {
            //Add onClick listener
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHandler.updateDuckIDs(features.get(getBtnID(v.getId())), question.getTable(), duckIDs);

                    //Append answer to array and remove old question
                    //answers.put(question, features.get(getBtnID(v.getId())));
                    questions.remove(question);

                    if (duckIDs.size() == 1) {
                        showAnswer();
                    } else if (questions.size() == 0) {
                        showAnswerList();
                    } else {
                        nextQuestion();
                    }
                }
            });
        }
    }

    private void nextQuestion() {
        Log.d("MainActivity", "nextQuestion");
        questionNo++;
        features.clear();
        question = dbHandler.getBestOption(duckIDs, questions);
        features = dbHandler.getListFeatures(question.getTable(), duckIDs);

        //Get and set Question and Question No
        TextView tvQuestionNo = (TextView)findViewById(R.id.tv_question_no);
        TextView tvQuestion = (TextView)findViewById(R.id.tv_question_question);
        tvQuestionNo.setText(String.format("%s.", String.valueOf(questionNo)));
        tvQuestion.setText(question.getQuestion());

        //Create Feature Options
        for (int i = 0; i < 14; i++) {
            Button btn = buttons.get(i);

            //Change button to show feature
            if (i < features.size()) {
                btn.setText(FeatureOptions.getValue(features.get(i)));
                btn.setVisibility(View.VISIBLE);

                //} else if (i == features.size()) {
                //    btn.setText(R.string.none_of_the_above);
                //    btn.setVisibility(View.VISIBLE);
            } else {
                btn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showAnswer() {
        Log.d("MainActivity", "showAnswer");
        Duck duck = dbHandler.getDuck(duckIDs.get(0));
        setContentView(R.layout.tq_result);

        TextView tvDuckName = (TextView)findViewById(R.id.tv_result_duck_name);
        ImageView ivDuckImage = (ImageView)findViewById(R.id.iv_result_image);
        Button btnAccept = (Button)findViewById(R.id.btn_result_yes);
        Button btnDeny = (Button)findViewById(R.id.btn_result_no);

        tvDuckName.setText(duck.getName());
        ivDuckImage.setImageBitmap(MainActivity.getBirdImage(this, "ibis.jpg"));

        //Restart Game
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.tq_question);

                //Output to console answers
                //Log.d("MainAcitivity", "Answers: " + answers);

                setButtons();
                //answers.clear();
                duckIDs.clear();
                questions.clear();
                duckIDs = dbHandler.getDuckIDs();
                questions = dbHandler.getListQuestions();
                questionNo = 0;
                nextQuestion();
            }
        });

        //Show List
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duckIDs.clear();
                duckIDs = dbHandler.getDuckIDs();
                showAnswerList();
            }
        });
    }

    private void showAnswerList() {
        Log.d("MainActivity", "showAnswerList");
        //Get List of Ducks
        List<Duck> ducks = new ArrayList<>();
        for (Integer duckID : duckIDs) {
            ducks.add(dbHandler.getDuck(duckID));
        }

        setContentView(R.layout.tq_result_list);

        ListView lvResultList = (ListView)findViewById(R.id.lv_result_list);
        ResultListAdapter adapter = new ResultListAdapter(getApplicationContext(), ducks);
        lvResultList.setAdapter(adapter);

    }

    private int getBtnID(int id) {
        Log.d("MainActivity", "getBtnID");
        switch (id) {
            case R.id.btn_option_1:
                return 0;
            case R.id.btn_option_2:
                return 1;
            case R.id.btn_option_3:
                return 2;
            case R.id.btn_option_4:
                return 3;
            case R.id.btn_option_5:
                return 4;
            case R.id.btn_option_6:
                return 5;
            case R.id.btn_option_7:
                return 6;
            case R.id.btn_option_8:
                return 7;
            case R.id.btn_option_9:
                return 8;
            case R.id.btn_option_10:
                return 9;
            case R.id.btn_option_11:
                return 10;
            case R.id.btn_option_12:
                return 11;
            case R.id.btn_option_13:
                return 12;
            case R.id.btn_option_14:
                return 13;
            default:
                Log.d("MainActivity", "getBtnID: Unknown Button Pressed.");
                return -1;
        }
    }

    static public void onClickResult(String duckName) {
        Log.d("ResultListAdapter", "ImageView has been Clicked: " + duckName);
    }

    static public Bitmap getBirdImage(Context context, String filePath) {
        //Update image for ImageView
        try {
            Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(filePath));
            return image;
        } catch (IOException unused) {
            //If duck image does not exist, display noImage file
            try {
                Bitmap image = BitmapFactory.decodeStream(context.getAssets().open("noImage.jpg"));
                return image;
            } catch (IOException ex){
                Log.e("ResultListAdapter", ex.getMessage());
            }

            Log.e("ResultListAdapter", "Failed to load image: " + filePath);
        }

        return null;
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
