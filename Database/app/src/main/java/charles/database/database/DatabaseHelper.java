package charles.database.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import charles.database.model.Duck;
import charles.database.model.Feature;
import charles.database.model.FeatureOptions;
import charles.database.model.Question;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "database.sqlite";
    public static final String DBLOCATION = "/data/data/charles.database/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * Create database from assets
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    /**
     * Not implemented
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
    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();

        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }

        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * Close the sqlite database if open
     */
    public void closeDatabase() {
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
        query.append("SELECT duckID FROM ");
        query.append(feature);
        query.append(" WHERE ");

        if (!isUnknown(goalFeature)) {
            query.append(feature);
            query.append(" = ");
            query.append(goalFeature);
            query.append(" AND ");
        }

        query.append("DuckID IN (VALUES");

        for (Integer duckID : duckIDs) {
            query.append("(");
            query.append(duckID.toString());
            query.append("),");
        }

        query.deleteCharAt(query.length() - 1);
        query.append(")");

        duckIDs.clear();

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query.toString(), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (isUnknown(goalFeature)) {
                duckIDs.remove((Integer)(cursor.getInt(0)));
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

        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT(");
        query.append(feature);
        query.append(") FROM ");
        query.append(feature);
        query.append(" WHERE DuckID IN (VALUES");
        query.append(listIDs(duckIDs));
        query.append(") ORDER BY ");
        query.append(feature);

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query.toString(), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            featureList.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();

        //Add UNKNOWN feature
        featureList.add(0);

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
        Question bestOption = null;
        String whereStatement = " WHERE DuckID IN(VALUES " + listIDs(duckIDs) + ")";
        Cursor cursor = null;
        int maxNumDucks = 0;
        int count;

        if (questions.size() == 0) {
            Log.d("DatabaseHelper", "getBestOption: No Question Given");
            return bestOption;
        }

        openDatabase();
        for (Question question : questions) {
            cursor = mDatabase.rawQuery("SELECT Count(DISTINCT(" + question.getTable() + ")) FROM " + question.getTable() + whereStatement, null);
            cursor.moveToFirst();
            count = cursor.getInt(0);

            if (count > maxNumDucks) {
                bestOption = question;
                maxNumDucks = count;
            }

            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return bestOption;
    }

    public List<Duck> getListDucks() {
        Duck duck;
        List<Duck> duckList = new ArrayList<>();
        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Ducks", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            duck = new Duck(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getShort(3),
                                (byte)cursor.getInt(4),
                                (byte)cursor.getInt(5));
            duckList.add(duck);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return duckList;
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

        return query.toString();
    }

    private boolean isUnknown(int feature) {
        return feature == 0;
    }

    /*public int getDuckIDCount(List<Integer> duckIDList, String TABLE, Feature feature, FeatureOptions goalFeature) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT duckID FROM ");
        query.append(TABLE);
        query.append(" WHERE ");
        query.append(feature.getFeatureName());
        query.append(" = ");
        query.append(goalFeature.)
        query.append(TABLE);
        query.append(".DuckID IN (VALUES");

        for (Integer duckID : duckIDList) {
            query.append("(");
            query.append(duckID.toString());
            query.append("),");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(")");

        duckIDList = new ArrayList<>();

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query.toString(), null);

        while (!cursor.isAfterLast()) {
            duckIDList.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
    }*/
}
