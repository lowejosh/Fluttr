package com.example.charles.opencv.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.R;
import com.example.charles.opencv.TwentyQuestion.Bird;
import com.example.charles.opencv.TwentyQuestion.Feature;
import com.example.charles.opencv.TwentyQuestion.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity is the self contained file for the Twenty Questions feature.
 */
public class TwentyQuestionActivity extends AppCompatActivity {
    private Database dbHandler;
    private List<Integer> birdIDs, featureList, answers;
    private List<Question> questionsAsked, questionsLeft;
    private Question currentQuestion;
    private int questionNo;
    private final int MAX_NUM_FEATURES = 14;
    public static final int TOP_RESULT_NUM_BIRDS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        dbHandler = new Database(this);

        //Begin Twenty Question Game
        twentyQuestions();
    }

    /**
     * Begin the Twenty Question Game
     */
    private void twentyQuestions() {
        //Change view to twenty questions
        setContentView(R.layout.twenty_question);

        //Get full list of birds and questions
        birdIDs = dbHandler.getBirdIDs();
        questionsLeft = dbHandler.getListQuestions();
        questionsAsked = new ArrayList<>();
        answers = new ArrayList<>();
        questionNo = 0;

        //Set Onclick Events for Option Buttons
        for (int btnOption = 0; btnOption < MAX_NUM_FEATURES; btnOption++) {
            Button btn = findViewById(getHomeButtonID(btnOption));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get Selected Feature
                    Integer selectedFeature = featureList.get(getHomeButtonOption(v.getId()));

                    //Move question from questionsLeft to questionsAsked
                    questionsLeft.remove(currentQuestion);
                    if (!Feature.isUnknown(selectedFeature)) {
                        questionsAsked.add(currentQuestion);
                    }

                    //Update BirdIDs and answers List
                    dbHandler.updateBirdIDs(selectedFeature, currentQuestion.getFeature(), birdIDs);
                    answers.add(selectedFeature);

                    //Move to Next Stage
                    nextStage();
                }
            });
        }

        //Ask question
        nextQuestion();
    }

    /**
     * Ask the next question for twenty questions
     */
    private void nextQuestion() {
        //Increment Question Number
        questionNo++;

        //Get new question
        currentQuestion = dbHandler.getBestOption(birdIDs, questionsLeft);

        //Check if any question is left to identify differences between the birds
        if (questionsLeft.size() == 0) {
            nextStage();
            return;
        }

        //Get feature list
        featureList = dbHandler.getListFeatures(currentQuestion.getFeature(), birdIDs);

        //Check if enough features exist to warrant a question
        if (featureList.size() == 1) {
            questionsLeft.remove(currentQuestion);
            nextStage();
            return;
        }

        //Get Views in twenty_question
        TextView tv_question_no = findViewById(R.id.tv_home_question_no);
        TextView tv_question = findViewById(R.id.tv_home_question);

        //Update Views in twenty_question
        tv_question_no.setText(String.format("%s.", questionNo));
        tv_question.setText(currentQuestion.getQuestion());

        //Make relevant buttons visible
        for (int btnOption = 0; btnOption < MAX_NUM_FEATURES; btnOption++) {
            Button btn = findViewById(getHomeButtonID(btnOption));

            //If there is a feature for the button, show the feature and make it visible
            if (btnOption < featureList.size()) {
                btn.setText(Feature.valueOf(featureList.get(btnOption)));
                btn.setVisibility(View.VISIBLE);
            } else {
                btn.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * If one bird is left, this will display the bird in tq_result
     */
    private void showAnswer() {
        //Change the View to tq_result
        setContentView(R.layout.tq_result);

        //Get the final Bird
        Bird bird = dbHandler.getBird(birdIDs.get(0));

        //Get Views
        TextView tvBirdName = findViewById(R.id.tv_result_bird_name);
        ImageView ivBirdImage = findViewById(R.id.iv_result_image);

        //Get Buttons
        Button btnAccept = findViewById(R.id.btn_result_yes);
        Button btnDeny = findViewById(R.id.btn_result_no);

        //Update Views
        tvBirdName.setText(bird.getName());
        ivBirdImage.setImageBitmap(bird.getBirdImage(getApplicationContext()));

        //Restart Game
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twentyQuestions();
            }
        });

        //Show List
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birdIDs = dbHandler.getClosestBirds(questionsAsked, answers, birdIDs.get(0));

                if (birdIDs.size() == 0) {
                    showFailure();
                } else {
                    showMultiAnswer();
                }
            }
        });
    }

    /**
     * Display result page against, deny button displays tq_failure instead of tq_topresults
     */
    private void showFinalAnswer() {
        //Change the View to tq_result
        setContentView(R.layout.tq_result);

        //Get the final Bird
        Bird bird = dbHandler.getBird(birdIDs.get(0));

        //Get Views
        TextView tvBirdName = findViewById(R.id.tv_result_bird_name);
        ImageView ivBirdImage = findViewById(R.id.iv_result_image);

        //Get Buttons
        Button btnAccept = findViewById(R.id.btn_result_yes);
        Button btnDeny = findViewById(R.id.btn_result_no);

        //Update Views
        tvBirdName.setText(bird.getName());
        ivBirdImage.setImageBitmap(bird.getBirdImage(getApplicationContext()));

        //Restart Game
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twentyQuestions();
            }
        });

        //Show List
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFailure();
            }
        });
    }

    /**
     * If multiple birds are left, this will display the top 5 birds
     */
    private void showMultiAnswer() {
        final int IMAGE = 1;
        final int TEXT = 2;

        setContentView(R.layout.tq_topresults);

        //For each result in tq_topresults
        for (int birdOption = 0; birdOption < TOP_RESULT_NUM_BIRDS; birdOption++) {
            //Get image and text view
            View[] result = getTopResultViews(birdOption);
            if (birdOption < birdIDs.size()) {
                Bird bird = dbHandler.getBird(birdIDs.get(birdOption));

                //Update Views
                ((ImageView) result[IMAGE]).setImageBitmap(bird.getBirdImage(getApplicationContext()));
                ((TextView) result[TEXT]).setText(bird.getName());

                //Set Visibility
                result[IMAGE].setVisibility(View.VISIBLE);
                result[TEXT].setVisibility(View.VISIBLE);
            } else {
                //Set Invisible
                result[IMAGE].setVisibility(View.GONE);
                result[TEXT].setVisibility(View.GONE);
            }
        }
    }

    /**
     * Display tq_failure
     */
    private void showFailure() {
        setContentView(R.layout.tq_failure);
    }

    /**
     * Determine which screen twenty questions should show
     */
    private void nextStage() {
        if (birdIDs.size() == 1) {
            //Show Answer
            showAnswer();
        } else if (questionsLeft.size() == 0) {
            //Check if any questions were asked and if the search has been refined far enough
            if (questionsAsked.size() > 0) {
                //Show Answer List
                showMultiAnswer();
            } else {
                showFailure();
            }
        } else {
            //Show Next Question
            nextQuestion();
        }
    }

    /**
     * Get the ID of the button from its option number in twenty_question
     *
     * @param optionNo Option number (0-13)
     * @return ID for use in findViewById()
     */
    private int getHomeButtonID(int optionNo) {
        return getResources().getIdentifier("btn_option_" + optionNo, "id", getPackageName());
    }

    /**
     * Get the Option number of the button from its ID in twenty_question
     *
     * @param id ID of the btn used in findViewById()
     * @return Option Number of the button
     */
    private int getHomeButtonOption(int id) {
        //Match id against all option buttons and return option number
        for (int btnOption = 0; btnOption < MAX_NUM_FEATURES; btnOption++) {
            if (id == getHomeButtonID(btnOption)) {
                return btnOption;
            }
        }

        //Failed to find button
        return -1;
    }

    /**
     * Get the image and text view for the option number from tq_topresults, array output is View[linearLayout, imageView, textView]
     *
     * @param optionNo Option number (0-4)
     * @return Array containing linear layout, image and text view
     */
    private View[] getTopResultViews(int optionNo) {
        ImageView imageView = findViewById(getResources().getIdentifier("top_result_image_" + optionNo, "id", getPackageName()));
        TextView textView = findViewById(getResources().getIdentifier("top_result_text_" + optionNo, "id", getPackageName()));
        LinearLayout linearLayout = findViewById(getResources().getIdentifier("top_result_" + optionNo, "id", getPackageName()));

        return new View[] {linearLayout, imageView, textView};
    }

    /**
     * Get the Option number of the view from its ID in tq_topresults
     *
     * @param id ID of the btn used in findViewById()
     * @return Option Number of the button
     */
    private int getTopResultOption(int id) {
        //Match id against all result options and return option number
        for (int btnOption = 0; btnOption < TOP_RESULT_NUM_BIRDS; btnOption++) {
            View[] views = getTopResultViews(btnOption);
            if (id == views[0].getId()) {
                return btnOption;
            }
        }

        return -1;
    }

    /**
     * OnClick function for image and text views in tq_topresults
     *
     * @param v View attached to the onclick
     */
    public void multiAnswerOnClick(View v) {
        int option = getTopResultOption(v.getId());
        Integer birdID = birdIDs.get(option);

        //Clear birdIDs and insert
        birdIDs.clear();
        birdIDs.add(birdID);

        showFinalAnswer();
    }

    /**
     * OnClick function for tq_failure button and tq_topresults button
     * @param v View attached to the onclick
     */
    public void backOnClick(View v) {
        twentyQuestions();
    }
}
