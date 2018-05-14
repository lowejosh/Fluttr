package charles.database.adapter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import charles.database.R;
import charles.database.database.DatabaseHelper;
import charles.database.model.Duck;
import charles.database.model.FeatureOptions;
import charles.database.model.Question;

public class QuestionAdapter {
    private DatabaseHelper dbHandler;
    ViewFlipper flipper;
    private TextView tvQuestion;
    private TextView tvQuestionNo;
    private TextView tvResultDuck;
    private int questionNo;
    private Question question;
    private List<Button> buttons;
    private List<Integer> duckIDs;
    private List<Question> questions;
    private List<Integer> features = new ArrayList<>();

    public QuestionAdapter(DatabaseHelper dbHandler, ViewFlipper flipper, TextView tvQuestion, TextView tvQuestionNo, TextView tvResultDuck, List<Button> buttons) {
        this.dbHandler = dbHandler;
        this.flipper = flipper;
        this.tvQuestion = tvQuestion;
        this.tvQuestionNo = tvQuestionNo;
        this.tvResultDuck = tvResultDuck;
        this.buttons = buttons;

        duckIDs = dbHandler.getDuckIDs();
        questions = dbHandler.getListQuestions();
        questionNo = 0;

        for (Button btn : buttons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnNextQuestion(v);
                }
            });
        }

        nextQuestion();
    }

    private void nextQuestion() {
        questionNo++;
        features.clear();
        question = dbHandler.getBestOption(duckIDs, questions);
        features = dbHandler.getListFeatures(question.getTable(), duckIDs);

        //Get and set Question and Question No
        tvQuestionNo.setText(String.format("%s.", String.valueOf(questionNo)));
        tvQuestion.setText(question.getQuestion());

        questions.remove(question);

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
        Duck duck = dbHandler.getDuck(duckIDs.get(0));
        tvResultDuck.setText(duck.getName());
        flipper.setDisplayedChild(1);
    }

    private void btnNextQuestion(View v) {
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
            Log.i("QuestionAdapter", "Size: " + String.valueOf(duckIDs.size()));
            nextQuestion();
        }
    }
}
