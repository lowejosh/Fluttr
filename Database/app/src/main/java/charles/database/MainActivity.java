package charles.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import charles.database.database.DatabaseHelper;
import charles.database.model.Duck;
import charles.database.model.FeatureOptions;
import charles.database.model.Question;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHandler;
    private List<Integer> duckIDs, featureList, answers;
    private List<Question> questionsAsked, questionsLeft;
    private Question currentQuestion;
    private int questionNo;
    private final int MAX_NUM_FEATURES = 14;
    public static final int TOP_RESULT_NUM_DUCKS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        dbHandler = new DatabaseHelper(this);

        //Check database exists
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);

        //If database does not exist, create it
        if (!database.exists()) {
            dbHandler.getReadableDatabase();
            copyDatabase(this);
        }

        //Begin Twenty Question Game
        twentyQuestions();
    }

    /**
     * Begin the Twenty Question Game
     */
    private void twentyQuestions() {
        //Change view to twenty questions
        setContentView(R.layout.tq_home);

        //Get full list of ducks and questions
        duckIDs = dbHandler.getDuckIDs();
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

                    //Update DuckIDs and answers List
                    dbHandler.updateDuckIDs(selectedFeature, currentQuestion.getFeature(), duckIDs);
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

        //Get new question and feature list
        currentQuestion = dbHandler.getBestOption(duckIDs, questionsLeft);
        featureList = dbHandler.getListFeatures(currentQuestion.getFeature(), duckIDs);

        if (featureList.size() == 1) {
            questionsLeft.remove(currentQuestion);
            nextStage();
            return;
        }

        //Move question from questionsLeft to questionsAsked
        questionsLeft.remove(currentQuestion);
        questionsAsked.add(currentQuestion);

        //Get Views in tq_home
        TextView tv_question_no = findViewById(R.id.tv_home_question_no);
        TextView tv_question = findViewById(R.id.tv_home_question);

        //Update Views in tq_home
        tv_question_no.setText(String.format("%s.", questionNo));
        tv_question.setText(currentQuestion.getQuestion());

        //Make relevant buttons visible
        for (int btnOption = 0; btnOption < MAX_NUM_FEATURES; btnOption++) {
            Button btn = findViewById(getHomeButtonID(btnOption));

            //If there is a feature for the button, show the feature and make it visible
            if (btnOption < featureList.size()) {
                btn.setText(FeatureOptions.getValue(featureList.get(btnOption)));
                btn.setVisibility(View.VISIBLE);
            } else {
                btn.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * If one duck is left, this will display the bird in tq_result
     */
    private void showAnswer() {
        //Change the View to tq_result
        setContentView(R.layout.tq_result);

        //Get the final Duck
        Duck duck = dbHandler.getDuck(duckIDs.get(0));

        //Get Views
        TextView tvDuckName = findViewById(R.id.tv_result_duck_name);
        ImageView ivDuckImage = findViewById(R.id.iv_result_image);

        //Get Buttons
        Button btnAccept = findViewById(R.id.btn_result_yes);
        Button btnDeny = findViewById(R.id.btn_result_no);

        //Update Views
        tvDuckName.setText(duck.getName());
        ivDuckImage.setImageBitmap(getBirdImage(duck.getImage()));

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
                duckIDs = dbHandler.getClosestDucks(questionsAsked, answers, duckIDs.get(0));

                if (duckIDs.size() == 0) {
                    showFailure();
                } else {
                    showMultiAnswer();
                }
            }
        });

        Log.d("MainActivity", "Questions Asked: " + questionsAsked);
        Log.d("MainActivity", "Answers: " + answers);
    }

    /**
     * Display result page against, deny button displays tq_failure instead of tq_topresults
     */
    private void showFinalAnswer() {
        //Change the View to tq_result
        setContentView(R.layout.tq_result);

        //Get the final Duck
        Duck duck = dbHandler.getDuck(duckIDs.get(0));

        //Get Views
        TextView tvDuckName = findViewById(R.id.tv_result_duck_name);
        ImageView ivDuckImage = findViewById(R.id.iv_result_image);

        //Get Buttons
        Button btnAccept = findViewById(R.id.btn_result_yes);
        Button btnDeny = findViewById(R.id.btn_result_no);

        //Update Views
        tvDuckName.setText(duck.getName());
        ivDuckImage.setImageBitmap(getBirdImage(duck.getImage()));

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
     * If multiple ducks are left, this will display the top 5 ducks
     */
    private void showMultiAnswer() {
        final int IMAGE = 1;
        final int TEXT = 2;

        setContentView(R.layout.tq_topresults);

        //For each result in tq_topresults
        for (int duckOption = 0; duckOption < TOP_RESULT_NUM_DUCKS; duckOption++) {
            //Get image and text view
            View[] result = getTopResultViews(duckOption);
            if (duckOption < duckIDs.size()) {
                Duck duck = dbHandler.getDuck(duckIDs.get(duckOption));

                //Update Views
                ((ImageView) result[IMAGE]).setImageBitmap(getBirdImage(duck.getImage()));
                ((TextView) result[TEXT]).setText(duck.getName());

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
        Log.d("MainActivity", "nextStage: QuestionsLeft: " + questionsLeft.size());
        if (duckIDs.size() == 1) {
            //Show Answer
            showAnswer();
        } else if (questionsLeft.size() == 0){
            //Show Answer List
            showMultiAnswer();
        } else {
            //Show Next Question
            nextQuestion();
        }
    }

    /**
     * Get the ID of the button from its option number in tq_home
     *
     * @param optionNo Option number (0-13)
     * @return ID for use in findViewById()
     */
    private int getHomeButtonID(int optionNo) {
        return getResources().getIdentifier("btn_option_" + optionNo, "id", getPackageName());
    }

    /**
     * Get the Option number of the button from its ID in tq_home
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
     * Get the image and text view for the option number from tq_topresult, array output is View[linearLayout, imageView, textView]
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
        for (int btnOption = 0; btnOption < TOP_RESULT_NUM_DUCKS; btnOption++) {
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
        Integer duckID = duckIDs.get(option);

        Log.i("MainActivity", "Option Selected: " + dbHandler.getDuck(duckID).getName());

        //Clear duckIDs and insert
        duckIDs.clear();
        duckIDs.add(duckID);

        showFinalAnswer();
    }

    /**
     * OnClick function for tq_failure button and tq_topresults button
     * @param v View attached to the onclick
     */
    public void backOnClick(View v) {
        twentyQuestions();
    }

    /**
     * Get the Bitmap Image of a Picture in the assets folder
     *
     * @param file Name of the image inside assets including extension
     * @return Image from file path in Bitmap form
     */
    public Bitmap getBirdImage(String file) {
        //Update image for ImageView
        try {
            return BitmapFactory.decodeStream(this.getAssets().open(file));
        } catch (IOException unused) {
            //If duck image does not exist, display noImage file
            try {
                return BitmapFactory.decodeStream(this.getAssets().open("noImage.jpg"));
            } catch (IOException ex){
                Log.e("MainActivity", "noImage Failed to Load");
                Log.e("MainActivity", ex.getMessage());
            }
            Log.e("MainActivity", "Failed to load image: " + file);
        }

        return null;
    }

    /**
     * On initialisation of the app, copy the database into system files
     *
     * @param context Context of the application
     */
    private void copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length;

            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
