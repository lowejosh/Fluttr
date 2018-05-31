package charles.database.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        Log.d("DatabaseHelper", "getDuck");
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
        Log.d("DatabaseHelper", "getDuckIDs");
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
        Log.d("DatabaseHelper", "updateDuckIDs");
        Log.d("DatabaseHelper", "updateDuckIDs: goalFeature: " + FeatureOptions.getValue(goalFeature));
        Log.d("DatabaseHelper", "updateDuckIDs: feature: " + feature);
        Log.d("DatabaseHelper", "updateDuckIDs: duckIDs: " + duckIDs);
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

        Log.d("DatabaseHelper", "updateDuckIDs: Query: " + query.toString());

        if (!isUnknown(goalFeature)) {
            duckIDs.clear();
        }

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query.toString(), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Log.d("DatabaseHelper", "updateDuckIDs: Duck Found");
            if (isUnknown(goalFeature)) {
                Log.d("DatabaseHelper", "updateDuckIDs: Duck Removed: " + cursor.getInt(0));
                duckIDs.remove((Integer)cursor.getInt(0));
            } else {
                Log.d("DatabaseHelper", "updateDuckIDs: Duck Added: " + cursor.getInt(0));
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
        Log.d("DatabaseHelper", "getListFeatures");
        Log.d("DatabaseHelper", "getListFeatures: feature: " + feature);
        Log.d("DatabaseHelper", "getListFeatures: duckIDs: " + duckIDs);
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
        Log.d("DatabaseHelper", "getListQuestions");
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
        Log.d("DatabaseHelper", "getBestOption");
        Log.d("DatabaseHelper", "getBestOption: duckIDs: " + duckIDs);
        Log.d("DatabaseHelper", "getBestOption: questions Size: " + questions.size());

        //Check if question exists
        if (questions.size() == 0) {
            Log.d("DatabaseHelper", "getBestOption: No Question Given");
            return null;
        }

        //Create SQL statement, track best table option, and number of ducks in the best table
        String whereStatement = " WHERE DuckID IN(VALUES " + listIDs(duckIDs);
        Question bestOption = null;
        int maxNumDucks = 0;

        openDatabase();
        for (Question question : questions) {
            Cursor cursor = mDatabase.rawQuery("SELECT Count(DISTINCT(" + question.getTable() +
                    ")) FROM " + question.getTable() + whereStatement, null);

            cursor.moveToFirst();
            int count = cursor.getInt(0);

            if (count > maxNumDucks) {
                bestOption = question;
                maxNumDucks = count;
            }

            cursor.close();
        }

        closeDatabase();
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
    public List<Integer> getClosestDucks(List<Question> questions, List<Integer> answers, Integer wrongDuckID)
    {
        List<Integer> duckIDs = new ArrayList<>();



        return duckIDs;
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
