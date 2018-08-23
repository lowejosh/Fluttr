package com.example.charles.opencv;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.example.charles.opencv.Tables.Bird;
import com.example.charles.opencv.Tables.Question;
import com.example.charles.opencv.Database.BirdFinderDatabase;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the Database Class, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BirdFinderDatabaseInstrumentedTest extends ActivityTestCase {
    private BirdFinderDatabase db;

    @Before
    public void setup() {
        db = new BirdFinderDatabase(InstrumentationRegistry.getTargetContext());
    }

    //aaa So that this test occurs first
    @Test
    public void aaaDatabaseCreation() {
        //Checks if database was made in startup
        assertTrue(true);
    }

    @Test
    public void multiDatabaseCreation() {
        //Checks if database was made in startup
        BirdFinderDatabase db = new BirdFinderDatabase(InstrumentationRegistry.getTargetContext());
        BirdFinderDatabase db2 = new BirdFinderDatabase(InstrumentationRegistry.getTargetContext());

        assertTrue(true);
    }

    @Test
    public void getBirdSuccess() {
        Bird bird = db.getBird(1);
        assertEquals(1, bird.getBirdID());
    }

    @Test
    public void getBirdInvalid() {
        Bird bird = db.getBird(0);
        assertNull(bird);
    }

    @Test
    public void getBirdIDsSuccess() {
        List<Integer> birdIDs = db.getBirdIDs();

        //Check size of array contains all the birds currently in the database
        assertTrue(birdIDs.size() == 89);

        for (Integer birdID : birdIDs) {
            assertNotNull(db.getBird(birdID));
        }
    }

    @Test
    public void updateBirdIDsSingleAnswer() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        //Prominent Bill
        String feature = "Bill";
        int goalFeature = 1;
        Integer success = 2; //Australasian Shoveler

        //Update bird list
        db.updateBirdIDs(goalFeature, feature, birdIDs);

        //Check function has run correctly
        assertEquals(1, birdIDs.size());
        assertEquals(success, birdIDs.get(0));
    }

    @Test
    public void updateBirdIDsMultiAnswer() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        //Prominent Bill
        String feature = "Colour";
        int goalFeature = 23;
        List<Integer> success = new ArrayList<>();
        success.add(1);
        success.add(2);
        success.add(3);

        //Update bird list
        db.updateBirdIDs(goalFeature, feature, birdIDs);

        //Check function has run correctly
        assertEquals(3, birdIDs.size());
        for (int birdID = 0; birdID < birdIDs.size(); birdID++) {
            assertEquals(birdIDs.get(birdID), success.get(birdID));
        }
    }

    @Test
    public void updateBirdIDsNoAnswer() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        //Prominent Bill
        String feature = "Cheek";
        int goalFeature = 5;

        //Update bird list
        db.updateBirdIDs(goalFeature, feature, birdIDs);

        //Check function has run correctly
        assertEquals(0, birdIDs.size());
    }

    @Test
    public void updateBirdIDsInvalidBird() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(0); //Invalid bird

        //Prominent Bill
        String feature = "Bill";
        int goalFeature = 1;

        //Invalid Bird
        db.updateBirdIDs(goalFeature, feature, birdIDs);
        assertEquals(0, birdIDs.size());
    }

    @Test
    public void updateBirdIDsInvalidFeature() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(2);

        //Prominent Bill
        String feature = "Bill";
        int goalFeature = -2;

        //Bird Should be Filtered Out I
        db.updateBirdIDs(goalFeature, feature, birdIDs);
        assertEquals(0, birdIDs.size());
    }

    @Test
    public void updateBirdIDsInvalidBirdList() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();

        //Prominent Bill
        String feature = "Bill";
        int goalFeature = 1;

        //bird Should be Filtered Out I
        db.updateBirdIDs(goalFeature, feature, birdIDs);
        assertEquals(0, birdIDs.size());
    }

    @Test
    public void updateBirdIDsUnknownFeature() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe

        //Prominent Bill
        String feature = "Bill";
        int goalFeature = -1; //Unknown
        Integer success = 1; //Australasian Shoveler

        //Update bird list
        db.updateBirdIDs(goalFeature, feature, birdIDs);

        //Check function has run correctly
        assertEquals(1, birdIDs.size());
        assertEquals(success, birdIDs.get(0));
    }

    @Test
    public void getListFeatureSuccess() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        //Feature
        String feature = "Bill";

        //Success Features
        List<Integer> success = new ArrayList<>();
        success.add(-1); //Other
        success.add(1);  //Prominent
        success.add(2);  //Curved
        success.add(3);  //Long

        //Get list of features
        List<Integer> features = db.getListFeatures(feature, birdIDs);

        //Match features in order
        for (int featureID = 0; featureID < success.size(); featureID++) {
            assertEquals(success.get(featureID), features.get(featureID));
        }
    }

    @Test
    public void getListFeatureCapitalFeature() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        //Feature Wrong Capitalisation
        String feature = "BILL";

        //Success Features
        List<Integer> success = new ArrayList<>();
        success.add(-1); //Other
        success.add(1);  //Prominent
        success.add(2);  //Curved
        success.add(3);  //Long

        //Get list of features
        List<Integer> features = db.getListFeatures(feature, birdIDs);

        //Match features in order
        for (int featureID = 0; featureID < success.size(); featureID++) {
            assertEquals(success.get(featureID), features.get(featureID));
        }
    }

    @Test
    public void getListFeatureOneFeature() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Shoveler

        //Feature Wrong Capitalisation
        String feature = "Bill";

        //Get list of features
        List<Integer> features = db.getListFeatures(feature, birdIDs);

        assertEquals(2, features.size());
    }

    @Test
    public void getListFeatureSingleBird() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(2); //Australasian Shoveler

        //Feature Wrong Capitalisation
        String feature = "Bill";

        //Success Features
        List<Integer> success = new ArrayList<>();
        success.add(-1); //Other
        success.add(1);  //Prominent
        success.add(2);  //Curved
        success.add(3);  //Long

        //Get list of features
        List<Integer> features = db.getListFeatures(feature, birdIDs);

        //Match features in order
        for (int featureID = 0; featureID < success.size(); featureID++) {
            assertEquals(success.get(featureID), features.get(featureID));
        }
    }

    @Test
    public void getListFeatureInvalidFeature() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        //Feature
        String feature = "INVALID";

        //Get list of features
        List<Integer> features = db.getListFeatures(feature, birdIDs);

        assertEquals(0, features.size());
    }

    @Test
    public void getListFeatureInvalidBirdIDs() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(-1);

        //Feature
        String feature = "Bill";

        //Get list of features
        List<Integer> features = db.getListFeatures(feature, birdIDs);

        assertEquals(1, features.size());
    }

    @Test
    public void getListFeatureInvalidBirdIDsList() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();

        //Feature
        String feature = "Bill";

        //Get list of features
        List<Integer> features = db.getListFeatures(feature, birdIDs);

        assertEquals(0, features.size());
    }

    @Test
    public void getListQuestions() {
        assertEquals(10, db.getListQuestions().size());
    }

    @Test
    public void getBestOptionSuccess() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        List<Question> questions = db.getListQuestions();
        String success = "Colour";

        assertEquals(success, db.getBestOption(birdIDs, questions).getTable());
    }

    @Test
    public void getBestOptionTwoEqualOptions() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Bill", "")); // 3 Options
        questions.add(new Question("Eye", ""));  // 3 Options

        assertTrue(db.getBestOption(birdIDs, questions).getTable().equals("Bill") ||
                            db.getBestOption(birdIDs, questions).getTable().equals("Eye"));
    }

    @Test
    public void getBestOptionEmptyBirdID() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();

        List<Question> questions = db.getListQuestions();
        db.getBestOption(birdIDs, questions);

        assertEquals(10, questions.size());
    }

    @Test
    public void getBestOptionInvalidBirdID() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(-1);

        List<Question> questions = db.getListQuestions();
        db.getBestOption(birdIDs, questions);

        assertEquals(0, questions.size());
    }

    @Test
    public void getBestOptionEmptyQuestion() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        List<Question> questions = new ArrayList<>();
        db.getBestOption(birdIDs, questions);

        assertEquals(0, questions.size());
    }

    @Test
    public void getBestOptionInvalidQuestion() {
        //List of birds
        List<Integer> birdIDs = new ArrayList<>();
        birdIDs.add(1); //Australasian Grebe
        birdIDs.add(2); //Australasian Shoveler
        birdIDs.add(3); //Australian Shelduck

        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Bill", "What Distinct Bill Shape does the Bird have?"));
        questions.add(new Question("", ""));
        db.getBestOption(birdIDs, questions);

        assertEquals(1, questions.size());
    }

    @Test
    public void getClosestBirdSuccess() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Colour", ""));
        questions.add(new Question("Eye", ""));

        List<Integer> answers = new ArrayList<>();
        answers.add(23);
        answers.add(4);

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);
        Integer success = 3;

        assertEquals(2, birds.size());
        assertEquals(success, birds.get(0));
    }

    @Test
    public void getClosestBirdOneQuestion() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Colour", ""));

        List<Integer> answers = new ArrayList<>();
        answers.add(23);

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);

        assertEquals(0, birds.size());
    }

    @Test
    public void getClosestBirdTooManyResults() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Colour", ""));
        questions.add(new Question("Bill", ""));

        List<Integer> answers = new ArrayList<>();
        answers.add(34); //White
        answers.add(2);  //Curved

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);

        assertEquals(5, birds.size());
    }

    @Test
    public void getClosestBirdBadQuestion() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("BAD", ""));
        questions.add(new Question("BAD", ""));

        List<Integer> answers = new ArrayList<>();
        answers.add(34); //White
        answers.add(2);  //Curved

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);

        assertEquals(0, birds.size());
    }

    @Test
    public void getClosestBirdBadAnswers() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Colour", ""));
        questions.add(new Question("Bill", ""));

        List<Integer> answers = new ArrayList<>();
        answers.add(-2);
        answers.add(-2);

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);

        assertEquals(0, birds.size());
    }

    @Test
    public void getClosestBirdBadDuck() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Colour", ""));
        questions.add(new Question("Eye", ""));

        List<Integer> answers = new ArrayList<>();
        answers.add(23);
        answers.add(4);

        List<Integer> birds = db.getClosestBirds(questions, answers, -1);
        Integer success = 3;

        assertEquals(3, birds.size());
        assertEquals(success, birds.get(0));
    }

    @Test
    public void getClosestBirdInvalidQuestion() {
        List<Question> questions = new ArrayList<>();

        List<Integer> answers = new ArrayList<>();
        answers.add(34); //White

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);

        assertEquals(0, birds.size());
    }

    @Test
    public void getClosestBirdInvalidAnswer() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Colour", ""));

        List<Integer> answers = new ArrayList<>();

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);

        assertEquals(0, birds.size());
    }

    @Test
    public void getClosestBirdInvalidQuestionAndAnswer() {
        List<Question> questions = new ArrayList<>();
        List<Integer> answers = new ArrayList<>();

        List<Integer> birds = db.getClosestBirds(questions, answers, 2);

        assertEquals(0, birds.size());
    }

    @Test
    public void getClosestBirdInvalidDuck() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Colour", ""));
        questions.add(new Question("Eye", ""));

        List<Integer> answers = new ArrayList<>();
        answers.add(23);
        answers.add(4);

        List<Integer> birds = db.getClosestBirds(questions, answers, null);
        Integer success = 3;

        assertEquals(3, birds.size());
        assertEquals(success, birds.get(0));
    }
}
