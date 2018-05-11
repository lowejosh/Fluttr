package charles.database.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import charles.database.R;
import charles.database.database.DatabaseHelper;
import charles.database.model.Duck;
import charles.database.model.FeatureOptions;
import charles.database.model.Question;

public class QuestionAdapter extends AppCompatActivity {
    private Context context;
    private DatabaseHelper dbHandler;
    private View v;
    private int questionNo;
    Question question;
    private List<Button> buttons = new ArrayList<>();
    private List<Integer> duckIDs = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private List<Integer> features = new ArrayList<>();

    public QuestionAdapter(Context context, DatabaseHelper dbHandler) {
        this.context = context;
        this.dbHandler = dbHandler;

        duckIDs = dbHandler.getDuckIDs();
        questions = dbHandler.getListQuestions();
        questionNo = 0;

        v = View.inflate(context, R.layout.tq_question, null);
        if (buttons.size() == 0) {
            buttons.add((Button) v.findViewById(R.id.btn_option_1));
            buttons.add((Button) v.findViewById(R.id.btn_option_2));
            buttons.add((Button) v.findViewById(R.id.btn_option_3));
            buttons.add((Button) v.findViewById(R.id.btn_option_4));
            buttons.add((Button) v.findViewById(R.id.btn_option_5));
            buttons.add((Button) v.findViewById(R.id.btn_option_6));
            buttons.add((Button) v.findViewById(R.id.btn_option_7));
            buttons.add((Button) v.findViewById(R.id.btn_option_8));
            buttons.add((Button) v.findViewById(R.id.btn_option_9));
            buttons.add((Button) v.findViewById(R.id.btn_option_10));
            buttons.add((Button) v.findViewById(R.id.btn_option_11));
            buttons.add((Button) v.findViewById(R.id.btn_option_12));
            buttons.add((Button) v.findViewById(R.id.btn_option_13));
            buttons.add((Button) v.findViewById(R.id.btn_option_14));

            for (Button btn : buttons) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnNextQuestion();
                    }
                });
            }
        }

        nextQuestion();
    }

    public View nextQuestion() {
        questionNo++;
        features.clear();
        question = dbHandler.getBestOption(duckIDs, questions);
        features = dbHandler.getListFeatures(question.getTable(), duckIDs);

        //Get and set Question and Question No
        TextView tvQuestionNo = (TextView) v.findViewById(R.id.tv_question_no);
        TextView tvQuestion = (TextView) v.findViewById(R.id.tv_question);
        tvQuestionNo.setText(String.format("%s.", String.valueOf(questionNo)));
        tvQuestion.setText(question.getQuestion());

        questions.remove(question);

        //Create Feature Options
        for (int i = 0; i < 14; i++) {
            Button btn = buttons.get(i);

            //Change button to show feature
            if (i < features.size()) {
                Log.i("MainActivity", String.valueOf(i));
                btn.setText(FeatureOptions.getValue(features.get(i)));
                btn.setVisibility(View.VISIBLE);
                //} else if (i == features.size()) {
                //    btn.setText(R.string.unknown);
                //    btn.setVisibility(View.VISIBLE);
                //} else if (i == features.size() + 1) {
                //    btn.setText(R.string.none_of_the_above);
                //    btn.setVisibility(View.VISIBLE);
            } else {
                btn.setVisibility(View.INVISIBLE);
            }
        }

        return v;
    }

    public void showAnswer() {
        Duck duck = dbHandler.getDuck(duckIDs.get(0));
        v = View.inflate(context, R.layout.tq_result, null);
        setContentView(v);

        TextView tvBirdName = (TextView)v.findViewById(R.id.tv_result_duck_name);
        tvBirdName.setText(duck.getName());

        setContentView(v);
    }

    private void btnNextQuestion() {
        //Get new DuckID list
        int btn_id = 0;
        switch (v.getId()) {
            case R.id.btn_option_1:
                btn_id = 0;
                break;
            case R.id.btn_option_2:
                btn_id = 1;
                break;
            case R.id.btn_option_3:
                btn_id = 2;
                break;
            case R.id.btn_option_4:
                btn_id = 3;
                break;
            case R.id.btn_option_5:
                btn_id = 4;
                break;
            case R.id.btn_option_6:
                btn_id = 5;
                break;
            case R.id.btn_option_7:
                btn_id = 6;
                break;
            case R.id.btn_option_8:
                btn_id = 7;
                break;
            case R.id.btn_option_9:
                btn_id = 8;
                break;
            case R.id.btn_option_10:
                btn_id = 9;
                break;
            case R.id.btn_option_11:
                btn_id = 10;
                break;
            case R.id.btn_option_12:
                btn_id = 11;
                break;
            case R.id.btn_option_13:
                btn_id = 12;
                break;
            case R.id.btn_option_14:
                btn_id = 13;
                break;
        }

        dbHandler.updateDuckIDs(features.get(btn_id), question.getTable(), duckIDs);

        if (duckIDs.size() == 1) {
            showAnswer();
        } else {
            nextQuestion();
        }
    }
}
