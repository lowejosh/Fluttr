package com.example.charles.opencv.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.util.SparseArray;

import com.example.charles.opencv.FeatureActivity.BirdFinderActivity;
import com.example.charles.opencv.Tables.Feature;
import com.example.charles.opencv.Tables.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Controls Bird Finder specific database functionality
 */
public class BirdFinderDatabase extends Database {

    public BirdFinderDatabase(Context context) {
        super(context);
        loadFeatures();
    }

    private void loadFeatures() {
        if (Feature.isLoaded())
            return;

        SparseArray<String> featureNames = new SparseArray<>();
        SparseArray<String> featureImages = new SparseArray<>();

        openDatabase();
        Log.d("loadFeatures", "SELECT * From Feature");
        Cursor cursor = rawQuery("SELECT * From Feature");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            featureNames.put(cursor.getInt(0), cursor.getString(1));
            featureImages.put(cursor.getInt(0), cursor.getString(2));

            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();

        Feature.loadFeatures(featureNames, featureImages);
    }

    /**
     * Returns a list containing all BirdIDs from the Birds table
     *
     * @return List of BirdIDs
     */
    public List<Integer> getBirdIDs() {
        List<Integer> birdIDs = new ArrayList<>();

        openDatabase();
        Log.d("getBirdIDs", "SELECT BirdID FROM Birds");
        Cursor cursor = rawQuery("SELECT BirdID FROM Birds");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            birdIDs.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return birdIDs;
    }

    /**
     * Updates the List of Integers containing the IDs of all birds matching the goal feature
     *
     * @param goalFeature FeatureOption containing the goal bird feature
     * @param feature Feature name
     * @param birdIDs List of BirdIDs to refine search
     */
    public void updateBirdIDs(int goalFeature, String feature, List<Integer> birdIDs) {
        //If feature is "Unknown" or size of birdID list is 0
        if (Feature.isUnknown(goalFeature) || birdIDs.size() == 0) {
            return;
        }

        //SELECT BirdID FROM table WHERE feature = goalFeature AND BirdID IN (VALUE (birdID), (birdID))
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT birdID FROM ");
        query.append(feature);
        query.append(" WHERE ");

        if (!Feature.isOther(goalFeature)) {
            query.append("FeatureID = ");
            query.append(goalFeature);
            query.append(" AND ");
        }

        query.append("BirdID IN (VALUES");
        query.append(listIDs(birdIDs));

        if (!Feature.isOther(goalFeature)) {
            birdIDs.clear();
        }

        openDatabase();
        Log.d("updateBirdIDs", query.toString());
        Cursor cursor = rawQuery(query.toString());
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (Feature.isOther(goalFeature)) {
                birdIDs.remove((Integer)cursor.getInt(0));
            } else {
                birdIDs.add(cursor.getInt(0));
            }
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
    }

    /**
     * Retrieve the list of feature tables and their associated questions
     *
     * @return List of Questions containing the table name and question
     */
    public List<Question> getListQuestions() {
        List<Question> questionList = new ArrayList<>();
        Question question;

        openDatabase();
        Log.d("getListQuestions", "SELECT * FROM Question");
        Cursor cursor = rawQuery("SELECT * FROM Question");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            question = new Question(cursor.getString(0), cursor.getString(1));
            questionList.add(question);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();

        return questionList;
    }

    /**
     * Get the list of possible features from a table with the list of BirdIDs
     *
     * @param table Table
     * @param birdIDs List of BirdIDs
     * @return List of Features
     */
    public List<Integer> getListFeatures(String table, List<Integer> birdIDs) {
        List<Integer> featureList = new ArrayList<>();

        //If birdID list is empty, return empty feature list
        if (birdIDs.size() == 0) {
            return featureList;
        }

        //Add Unknown to Feature List
        featureList.add(Feature.UNKNOWN);

        //Create query to find all features that any bird in the list of birdIDs has
        String query = "SELECT DISTINCT(FeatureID) FROM " + table +
                " WHERE BirdID IN (VALUES" + listIDs(birdIDs) + " ORDER BY FeatureID";

        //Open Database and create cursor
        openDatabase();
        Cursor cursor;
        try {
            Log.d("getListFeatures", query);
            cursor = rawQuery(query);
            cursor.moveToFirst();

            //Add each feature available
            while (!cursor.isAfterLast()) {
                featureList.add(cursor.getInt(0));
                cursor.moveToNext();
            }

            cursor.close();
        } catch (SQLiteException ex) {
            //If SQL table does not exist, log error and return empty feature list
            Log.e("Database", "Invalid Table Selected: " + table);
            return new ArrayList<>();
        }

        //Determine if a bird has none of the listed features
        Log.d("getListFeatures", "SELECT birdID FROM Birds WHERE birdID NOT IN (SELECT birdID FROM " + table + ")");
        cursor = rawQuery("SELECT birdID FROM Birds WHERE birdID NOT IN (SELECT birdID FROM " + table + ")");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            //If one birdID matches any birdID in the list of birdIDs
            if (birdIDs.contains(cursor.getInt(0))) {
                //Add other to featureList
                featureList.add(Feature.OTHER);
                break;
            }
            cursor.moveToNext();
        }

        //Close cursor and database
        cursor.close();
        closeDatabase();

        return featureList;
    }

    /**
     * Determine the best question to ask
     *
     * @param birdIDs List of BirdID's
     * @param questions List of Questions
     * @return Most optimal question to reduce the number of BirdID's
     */
    public Question getBestOption(List<Integer> birdIDs, List<Question> questions) {
        //Check if question exists
        if (birdIDs.size() == 0 || questions.size() == 0) {
            return null;
        }

        //Create SQL statement, track best table option, and number of birds in the best table
        String whereStatement = " WHERE BirdID IN(VALUES " + listIDs(birdIDs);
        Question bestOption = null;
        int maxNumBirds = 0;
        List<Question> questionsToRemove = new ArrayList<>();

        Log.i("getBestOption", questions.toString());

        openDatabase();
        for (Question question : questions) {
            try {
                Log.d("getBestOption", "SELECT Count(DISTINCT(FeatureID)) FROM " + question.getTable() + whereStatement);
                Cursor cursor = rawQuery("SELECT Count(DISTINCT(FeatureID)) FROM " + question.getTable() + whereStatement);

                cursor.moveToFirst();
                int count = cursor.getInt(0);

                //If no birds exist in the table
                if (count == 0) {
                    Log.d("BirdFinderActivity", "Bad Question");
                    questionsToRemove.add(question);
                }

                //If table is the new best table
                else if (count > maxNumBirds) {
                    bestOption = question;
                    maxNumBirds = count;
                }

                cursor.close();
            } catch (SQLiteException ex) {
                closeDatabase();
                Log.e("Database", "getBestOption: Invalid Question: " + questions);
                questions.remove(question);
            }
        }
        closeDatabase();


        //Update list of questions
        questions.removeAll(questionsToRemove);
        questionsToRemove.clear();

        return bestOption;
    }

    /**
     * Get a list of at most 5 birds closest in attributes to the wrong bird
     *
     * @param questions List of Questions Asked
     * @param answers List of Answers to the Questions Asked
     * @param wrongBirdID ID of the Bird which has been Identified as Incorrect by the User
     * @return List of at most 5 birdIDs matching closest to the bird
     */
    public List<Integer> getClosestBirds(List<Question> questions, List<Integer> answers, Integer wrongBirdID) {
        List<Integer> birdIDs;
        List<List<Integer>> birdMatches; //Stores all the birds for each question that match the answer

        if (questions.size() <= 1) {
            return new ArrayList<>();
        }

        if (answers.size() != questions.size()) {
            Log.e("Database", "getClosestBirds: Answers and Questions Size Must Match");
            return new ArrayList<>();
        }

        //Populate bird matches
        birdMatches = getMatchingBirdIDs(questions, answers, wrongBirdID);

        //Get Birds that Match Multiple Questions
        birdIDs = getSimilarBirds(questions, birdMatches);

        return birdIDs;
    }

    /**
     * Get a list of birds that match all but one question is the list of questions
     *
     * @param questions List of Questions Asked
     * @param birdMatches List of Birds which are Linked to each Question
     * @return List of birdIDs matching all but one question
     */
    private List<Integer> getSimilarBirds(List<Question> questions, List<List<Integer>> birdMatches) {
        List<Integer> birdIDs = new ArrayList<>();

        //Find all values that match between all arrays bar one
        int[] questionMatch = new int[questions.size() - 1];
        for (int skip = 0; skip < questions.size(); skip++) {
            //Get List of Questions to Compare Against Where the Questions with the smallest number of birds are prioritized
            int index = 0;
            for (int questionNo = 0; questionNo < questions.size(); questionNo++) {
                if (skip == questionNo) {
                    continue;
                }

                questionMatch[index] = questionNo;
                index++;
            }

            //Compare list of Questions
            Collection<Integer> similar = new HashSet<>(birdMatches.get(questionMatch[0]));
            for (int i = 0; i < questionMatch.length - 1; i++) {
                Collection<Integer> list = new HashSet<>(birdMatches.get(questionMatch[i + 1]));

                similar.retainAll(list);
            }

            //Add matching birds to list
            for (Integer birdID : similar) {
                if (birdIDs.size() < BirdFinderActivity.TOP_RESULT_NUM_BIRDS && !birdIDs.contains(birdID)) {
                    birdIDs.add(birdID);
                } else {
                    break;
                }
            }
        }

        return birdIDs;
    }

    /**
     * Returns a list containing a list of all birdIDs matching the answer of one of the questions
     *
     * @param questions List of Questions to Match Against
     * @param answers List of Answers to the Questions
     * @param wrongBirdID ID of the Bird which has been Identified as Incorrect by the User
     */
    private List<List<Integer>> getMatchingBirdIDs(List<Question> questions, List<Integer> answers, Integer wrongBirdID) {
        List<List<Integer>> birdMatches = new ArrayList<>();
        openDatabase();

        //For each question and answer
        for (int questionNo = 0; questionNo < questions.size(); questionNo++) {
            Question question = questions.get(questionNo);
            Integer answer = answers.get(questionNo);

            //Stores all matching birdIDs
            List<Integer> match = new ArrayList<>();

            try {
                //EG. SELECT birdID FROM Colour WHERE Colour == 24
                Log.d("getMatchingBirdIDs", "SELECT birdID FROM " + question.getTable() + " WHERE FeatureID == " + answer);
                Cursor cursor = rawQuery("SELECT birdID FROM " + question.getTable() + " WHERE FeatureID == " + answer);

                //Add all matching birdIDs
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    match.add(cursor.getInt(0));

                    cursor.moveToNext();
                }
                cursor.close();
            } catch (SQLiteException ex) {
                Log.e("Database", "getMatchingBirdIDs: Invalid Table: " + question.getTable());
            }

            //Remove wrong bird and add list to bird matches
            match.remove(wrongBirdID);
            birdMatches.add(match);
        }

        closeDatabase();
        return birdMatches;
    }

    /**
     * Returns the List of birdIDs for use in the SQL statement VALUES
     *
     * @param birdIDs List of BirdIDs
     * @return String containing all the birdID's formatted for the SQL VALUES statement
     */
    private String listIDs(List<Integer> birdIDs) {
        StringBuilder query = new StringBuilder();

        for (Integer birdID : birdIDs) {
            query.append("(");
            query.append(birdID.toString());
            query.append("),");
        }

        query.deleteCharAt(query.length() - 1);
        query.append(")");

        return query.toString();
    }
}
