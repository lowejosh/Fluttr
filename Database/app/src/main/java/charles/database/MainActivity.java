package charles.database;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import charles.database.adapter.QuestionAdapter;
import charles.database.database.DatabaseHelper;
import charles.database.model.FeatureOptions;
import charles.database.model.Question;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tq_question);
        mDBHelper = new DatabaseHelper(this);

        //Check database exists
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);

        if (!database.exists()) {
            mDBHelper.getReadableDatabase();

            if (copyDatabase(this)) {
                Toast.makeText(this, "Copy database success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //test20Q();

        setContentView(R.layout.tq_home);

        List<Button> buttons = new ArrayList<>();
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

        QuestionAdapter questionAdapter = new QuestionAdapter(mDBHelper, (ViewFlipper)findViewById(R.id.vf_question), (TextView)findViewById(R.id.tv_question), (TextView)findViewById(R.id.tv_question_no), (TextView)findViewById(R.id.tv_result_duck_name), buttons);
        //questionAdapter.nextQuestion();
    }

    private void test20Q() {
        List<Integer> duckIDList = mDBHelper.getDuckIDs();
        List<Question> questionList = mDBHelper.getListQuestions();

        Random rand = new Random();

        for (int i = 0; i < questionList.size(); i++) {
            Question question = mDBHelper.getBestOption(duckIDList, questionList);
            if (question == null || duckIDList.size() == 1) {
                break;
            }

            //Select a random answer
            List<Integer> listFeatures = mDBHelper.getListFeatures(question.getTable(), duckIDList);
            int random = rand.nextInt(listFeatures.size());

            Log.i("MainActivity", question.getQuestion() + " " + FeatureOptions.getValue(listFeatures.get(random)));

            questionList.remove(question);
            mDBHelper.updateDuckIDs(listFeatures.get(random), question.getTable(), duckIDList);
        }
        for (Integer duckID : duckIDList) {
            Log.i("MainActivity", "Bird: " + mDBHelper.getDuck(duckID).getName());
        }
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
