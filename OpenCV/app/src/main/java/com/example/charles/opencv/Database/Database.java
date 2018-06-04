package com.example.charles.opencv.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.charles.opencv.Activity.TwentyQuestionActivity;
import com.example.charles.opencv.TwentyQuestion.Bird;
import com.example.charles.opencv.TwentyQuestion.Feature;
import com.example.charles.opencv.TwentyQuestion.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * This class is a helper for interfacing with the sql database.
 */
public class Database extends SQLiteOpenHelper {
    private static final String DBNAME = "database.sqlite";
    private static String DBLOCATION;
    private Context context;
    private static SQLiteDatabase mDatabase;

    // Bird seen stuff
    private static final String BS_TABLE_NAME = "birds_seen";
    private static final String BS_COL1 = "ID";
    private static final String BS_COL2 = "DATE";

    /**
     * Create database from assets
     *
     * @param context Context
     */
    public Database(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
        DBLOCATION = context.getDatabasePath(DBNAME).getParentFile().getPath() + "/";

        //Check database exists
        File database = context.getDatabasePath(DBNAME);

        //If database does not exist, create it
        if (!database.exists()) {
            getReadableDatabase();
            copyDatabase();
        }
    }

    /**
     * Not implemented
     *
     * @param db Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("CREATING TABLE","CREATING TABLE");
        System.out.println("CREATing TABLE");
        db.execSQL("DROP TABLE IF EXISTS " + BS_TABLE_NAME);
        String createTable = "CREATE TABLE " + BS_TABLE_NAME + " (" + BS_COL1 + " INTEGER PRIMARY KEY, " + BS_COL2 + " TEXT)";
        db.execSQL(createTable);
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
        db.execSQL("DROP TABLE IF EXISTS " + BS_TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String birdID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BS_COL1, birdID);

        String pattern = "dd-MM-yyyy";
        String currentDate = new SimpleDateFormat(pattern).format(new Date());

        contentValues.put(BS_COL2, currentDate);

        Log.d("Database","addData: Adding " + birdID + " and " + currentDate + " to " + BS_TABLE_NAME);
        long result = db.insert(BS_TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Open the sqlite database
     */
    private void openDatabase() {
        String dbPath = context.getDatabasePath(DBNAME).getPath();

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
     * On initialisation of the app, copy the database into system files
     */
    private void copyDatabase() {
        try {
            InputStream inputStream = context.getAssets().open(Database.DBNAME);
            String outFileName = DBLOCATION + DBNAME;
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

    /**
     * Retrieve the bird attached to the BirdID. If bird does not exists, returns null
     *
     * @param birdID BirdID of the selected bird
     * @return Bird matching the BirdID
     */
    public Bird getBird(int birdID) {
        Bird bird;

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Birds WHERE BirdID = ?", new String[] { String.valueOf(birdID) });
        cursor.moveToFirst();

        try {
            bird = new Bird(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getShort(3),
                    (byte) cursor.getInt(4),
                    (byte) cursor.getInt(5));
        } catch (CursorIndexOutOfBoundsException ex) {
            Log.e("Database","Database: Failed to Find Duck with ID: " + birdID);
            bird = null;
        }

        cursor.close();
        closeDatabase();
        return bird;
    }

    /**
     * Get the seen_bird table cursor
     * @return Cursor of bird IDs
     */
    public Cursor getSeenBirdIDList() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BS_TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Retrieve a list of initialised Bird objects from the Birds table
     * @return List of birds
     */
    public List<Bird> getBirdList() {
        List<Bird> birdList = new ArrayList<>();
        Bird bird;

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Birds", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            try {
                bird = new Bird(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getShort(3),
                        (byte) cursor.getInt(4),
                        (byte) cursor.getInt(5));
            } catch (CursorIndexOutOfBoundsException ex) {
                Log.e("Database", "Database: Failed to Find Bird with ID: " + cursor.getInt(0));
                bird = null;
            }
            birdList.add(bird);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return birdList;

    }

    /**
     * Returns a list containing all BirdIDs from the Birds table
     *
     * @return List of BirdIDs
     */
    public List<Integer> getBirdIDs() {
        List<Integer> birdIDs = new ArrayList<>();

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT BirdID FROM Birds", null);
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
            query.append(feature);
            query.append(" = ");
            query.append(goalFeature);
            query.append(" AND ");
        }

        query.append("BirdID IN (VALUES");
        query.append(listIDs(birdIDs));

        if (!Feature.isOther(goalFeature)) {
            birdIDs.clear();
        }

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query.toString(), null);
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
     * Get the list of possible features from a table with the list of BirdIDs
     *
     * @param feature Table
     * @param birdIDs List of BirdIDs
     * @return List of Features
     */
    public List<Integer> getListFeatures(String feature, List<Integer> birdIDs) {
        List<Integer> featureList = new ArrayList<>();

        //If birdID list is empty, return empty feature list
        if (birdIDs.size() == 0) {
            return featureList;
        }

        //Add Unknown to Feature List
        featureList.add(Feature.UNKNOWN);

        //Create query to find all features that any bird in the list of birdIDs has
        String query = "SELECT DISTINCT(" + feature + ") FROM " + feature +
                " WHERE BirdID IN (VALUES" + listIDs(birdIDs) + " ORDER BY " + feature;

        //Open Database and create cursor
        openDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery(query, null);
            cursor.moveToFirst();

            //Add each feature available
            while (!cursor.isAfterLast()) {
                featureList.add(cursor.getInt(0));
                cursor.moveToNext();
            }

            cursor.close();
        } catch (SQLiteException ex) {
            //If SQL table does not exist, log error and return empty feature list
            Log.e("Database", "Invalid Table Selected: " + feature);
            return new ArrayList<>();
        }

        //Determine if a bird has none of the listed features
        cursor = mDatabase.rawQuery("SELECT birdID FROM Birds WHERE birdID NOT IN (SELECT birdID FROM " + feature + ")", null);
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


        openDatabase();
        for (Question question : questions) {
            try {
                Cursor cursor = mDatabase.rawQuery("SELECT Count(DISTINCT(" + question.getTable() +
                        ")) FROM " + question.getTable() + whereStatement, null);

                cursor.moveToFirst();
                int count = cursor.getInt(0);

                //If no birds exist in the table
                if (count == 0) {
                    Log.d("TwentyQuestionActivity", "Bad Question");
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
            }

            //Compare list of Questions
            Collection<Integer> similar = new HashSet<>(birdMatches.get(questionMatch[0]));
            for (int i = 0; i < questionMatch.length - 1; i++) {
                Collection<Integer> list = new HashSet<>(birdMatches.get(questionMatch[i + 1]));

                similar.retainAll(list);
            }

            //Add matching birds to list
            for (Integer birdID : similar) {
                if (birdIDs.size() < TwentyQuestionActivity.TOP_RESULT_NUM_BIRDS && !birdIDs.contains(birdID)) {
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
                Cursor cursor = mDatabase.rawQuery("SELECT birdID FROM " + question.getTable() +
                        " WHERE " + question.getFeature() + " == " + answer, null);

                //Add all matching birdIDs
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    match.add((Integer) cursor.getInt(0));

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

    /**
     * Gets the DBNAME
     * @return the name of the database
     */
    public static String getDBNAME() {
        return DBNAME;
    }

    /**
     * Clears the birds seen table
     */
    public void clearBirdsSeen() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DROP TABLE "+BS_TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

}
