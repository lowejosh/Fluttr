package charles.database.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import charles.database.MainActivity;
import charles.database.model.Duck;
import charles.database.model.FeatureOptions;
import charles.database.model.Question;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "database.sqlite";
    public static String DBLOCATION;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * Create database from assets
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
        DBLOCATION = context.getDatabasePath(DBNAME).getParentFile().getPath() + "/";
    }

    /**
     * Not implemented
     *
     * @param db Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * Not implemented
     *
     * @param db Database
     * @param oldVersion IDK
     * @param newVersion IDK
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Open the sqlite database
     */
    private void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();

        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }

        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * Close the sqlite database if open
     */
    private void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    /**
     * Retrieve the duck attached to the DuckID
     *
     * @param duckID DuckID of the selected duck
     * @return Duck matching the DuckID
     */
    public Duck getDuck(int duckID) {
        Duck duck;

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Ducks WHERE DuckID = ?", new String[] { String.valueOf(duckID) });
        cursor.moveToFirst();
        duck = new Duck(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getShort(3),
                        (byte)cursor.getInt(4),
                        (byte)cursor.getInt(5));

        cursor.close();
        closeDatabase();
        return duck;
    }

    /**
     * Returns a list containing all DuckIDs from the Ducks table
     *
     * @return List of DuckIDs
     */
    public List<Integer> getDuckIDs() {
        List<Integer> duckIDs = new ArrayList<>();

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT DuckID FROM Ducks", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            duckIDs.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return duckIDs;
    }

    /**
     * Updates the List of Integers containing the IDs of all ducks matching the goal feature
     *
     * @param goalFeature FeatureOption containing the goal duck feature
     * @param feature Feature name
     * @param duckIDs List of DuckIDs to refine search
     */
    public void updateDuckIDs(int goalFeature, String feature, List<Integer> duckIDs) {
        //SELECT DuckID FROM table WHERE feature = goalFeature AND DuckID IN (VALUE (duckID), (duckID))
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT duckID FROM ");
        query.append(feature);
        query.append(" WHERE ");

        if (!isUnknown(goalFeature)) {
            query.append(feature);
            query.append(" = ");
            query.append(goalFeature);
            query.append(" AND ");
        }

        query.append("DuckID IN (VALUES");
        query.append(listIDs(duckIDs));

        if (!isUnknown(goalFeature)) {
            duckIDs.clear();
        }

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query.toString(), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (isUnknown(goalFeature)) {
                duckIDs.remove((Integer)cursor.getInt(0));
            } else {
                duckIDs.add(cursor.getInt(0));
            }
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();

    }

    /**
     * Get the list of possible features from a table with the list of DuckIDs
     *
     * @param feature Table
     * @param duckIDs List of DuckIDs
     * @return List of Features
     */
    public List<Integer> getListFeatures(String feature, List<Integer> duckIDs) {
        List<Integer> featureList = new ArrayList<>();

        //Create query to find all features that any duck in the list of duckIDs has
        String query = "SELECT DISTINCT(" + feature + ") FROM " + feature +
                " WHERE DuckID IN (VALUES" + listIDs(duckIDs) + " ORDER BY " + feature;

        //Open Database and create cursor
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();

        //Add each feature available
        while (!cursor.isAfterLast()) {
            featureList.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        //Close cursor
        cursor.close();

        //Determine if a duck has none of the listed features
        cursor = mDatabase.rawQuery("SELECT duckID FROM Ducks WHERE duckID NOT IN (SELECT duckID FROM " + feature + ")", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            //If one duckID matches any duckID in the list of duckIDs
            if (duckIDs.contains(cursor.getInt(0))) {
                //Add other to featureList
                featureList.add(0);
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
     * Retrieve the list of feature tables and their associated questions
     *
     * @return List of Questions containing the table name and question
     */
    public List<Question> getListQuestions() {
        List<Question> questionList = new ArrayList<>();
        Question question;

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Question", null);
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
     * Determine the best question to ask
     *
     * @param duckIDs List of DuckID's
     * @param questions List of Questions
     * @return Most optimal question to reduce the number of DuckID's
     */
    public Question getBestOption(List<Integer> duckIDs, List<Question> questions) {

        //Check if question exists
        if (questions.size() == 0) {
            Log.d("DatabaseHelper", "getBestOption: No Question Given");
            return null;
        }

        //Create SQL statement, track best table option, and number of ducks in the best table
        String whereStatement = " WHERE DuckID IN(VALUES " + listIDs(duckIDs);
        Question bestOption = null;
        int maxNumDucks = 0;
        List<Question> questionsToRemove = new ArrayList<>();

        openDatabase();
        for (Question question : questions) {
            Cursor cursor = mDatabase.rawQuery("SELECT Count(DISTINCT(" + question.getTable() +
                    ")) FROM " + question.getTable() + whereStatement, null);

            cursor.moveToFirst();
            int count = cursor.getInt(0);

            //If no ducks exist in the table
            if (count == 0) {
                questionsToRemove.add(question);
            }

            //If table is the new best table
            else if (count > maxNumDucks) {
                bestOption = question;
                maxNumDucks = count;
            }

            cursor.close();
        }
        closeDatabase();

        //Update list of questions
        questions.removeAll(questionsToRemove);
        questionsToRemove.clear();

        return bestOption;
    }

    /**
     * Get a list of at most 5 ducks closest in attributes to the wrong duck
     *
     * @param questions List of Questions Asked
     * @param answers List of Answers to the Questions Asked
     * @param wrongDuckID ID of the Duck which has been Identified as Incorrect by the User
     * @return List of at most 5 duckIDs matching closest to the duck
     */
    public List<Integer> getClosestDucks(List<Question> questions, List<Integer> answers, Integer wrongDuckID) {
        List<Integer> duckIDs;
        List<List<Integer>> duckMatches; //Stores all the ducks for each question that match the answer

        if (questions.size() == 1) {
            return new ArrayList<>();
        }

        //Populate duck matches
        duckMatches = getMatchingDuckIDs(questions, answers, wrongDuckID);

        //Get Birds that Match Multiple Questions
        duckIDs = getSimilarDucks(questions, duckMatches);

        return duckIDs;
    }

    /**
     * Get a list of ducks that match all but one question is the list of questions
     *
     * @param questions List of Questions Asked
     * @param duckMatches List of Ducks which are Linked to each Question
     * @return List of duckIDs matching all but one question
     */
    private List<Integer> getSimilarDucks(List<Question> questions, List<List<Integer>> duckMatches) {
        List<Integer> duckIDs = new ArrayList<>();

        //Find all values that match between all arrays bar one
        int[] questionMatch = new int[questions.size() - 1];
        for (int skip = 0; skip < questions.size(); skip++) {
            //Get List of Questions to Compare Against Where the Questions with the largest number of ducks attached is added to duck matches first
            int index = 0;
            for (int questionNo = questions.size() - 1; questionNo > 0; questionNo--) {
                if (skip == questionNo) {
                    continue;
                }

                questionMatch[index] = questionNo;
            }

            //Compare list of Questions
            Collection<Integer> similar = new HashSet<>(duckMatches.get(questionMatch[0]));
            for (int i = 0; i < questionMatch.length - 1; i++) {
                Collection<Integer> list = new HashSet<>(duckMatches.get(questionMatch[i + 1]));

                similar.retainAll(list);
            }

            //Add matching ducks to list
            for (Integer duckID : similar) {
                if (duckIDs.size() < MainActivity.TOP_RESULT_NUM_DUCKS && !duckIDs.contains(duckID)) {
                    duckIDs.add(duckID);
                } else {
                    break;
                }
            }
            Log.d("DatabaseHelper", "Similar Ducks: " + similar);
        }

        return duckIDs;
    }

    /**
     * Returns a list containing a list of all duckIDs matching the answer of one of the questions
     *
     * @param questions List of Questions to Match Against
     * @param answers List of Answers to the Questions
     * @param wrongDuckID ID of the Duck which has been Identified as Incorrect by the User
     */
    private List<List<Integer>> getMatchingDuckIDs(List<Question> questions, List<Integer> answers, Integer wrongDuckID) {
        List<List<Integer>> duckMatches = new ArrayList<>();
        openDatabase();

        //For each question and answer
        for (int questionNo = 0; questionNo < questions.size(); questionNo++) {
            Question question = questions.get(questionNo);
            Integer answer = answers.get(questionNo);

            //Stores all matching duckIDs
            List<Integer> match = new ArrayList<>();

            //EG. SELECT duckID FROM Colour WHERE Colour == 24
            Cursor cursor = mDatabase.rawQuery("SELECT duckID FROM " + question.getTable() +
                    " WHERE " + question.getFeature() + " == " + answer, null);

            //Add all matching duckIDs
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                match.add((Integer)cursor.getInt(0));

                cursor.moveToNext();
            }
            cursor.close();

            //Remove wrong duck and add list to duck matches
            match.remove(wrongDuckID);
            duckMatches.add(match);

            Log.d("DatabaseHelper", "Matches: " + match);
        }

        closeDatabase();
        return duckMatches;
    }

    /**
     * Returns the List of duckIDs for use in the SQL statement VALUES
     *
     * @param duckIDs List of DuckIDs
     * @return String containing all the duckID's formatted for the SQL VALUES statement
     */
    private String listIDs(List<Integer> duckIDs) {
        StringBuilder query = new StringBuilder();

        for (Integer duckID : duckIDs) {
            query.append("(");
            query.append(duckID.toString());
            query.append("),");
        }

        query.deleteCharAt(query.length() - 1);
        query.append(")");

        return query.toString();
    }

    /**
     * Returns true if the feature is an unknown feature
     *
     * @param feature Index of feature in FeatureOption
     * @return True if the feature is an unknown feature
     */
    private boolean isUnknown(int feature) {
        return feature == 0;
    }
}
