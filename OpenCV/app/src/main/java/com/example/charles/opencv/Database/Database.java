package com.example.charles.opencv.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.charles.opencv.Tables.Bird;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    static final String BS_TABLE_NAME = "birds_seen";
    static final String BS_COL1 = "ID";
    static final String BS_COL2 = "DATE";

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

        //if (database.exists()) {
        //    database.delete();
        //}

        //If database does not exist, create it
        if (!database.exists()) {
            getReadableDatabase();
            copyDatabase();
        }
    }

    /**
     * <b>Not Implemented</b>
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + BS_TABLE_NAME);
        String createTable = "CREATE TABLE " + BS_TABLE_NAME + " (" + BS_COL1 + " INTEGER PRIMARY KEY, " + BS_COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    /**
     * <b>Not Implemented</b>
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BS_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Open the sqlite database
     */
    void openDatabase() {
        String dbPath = context.getDatabasePath(DBNAME).getPath();

        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }

        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * Close the sqlite database if open
     */
    void closeDatabase() {
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
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     *
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    Cursor rawQuery(String sql) {
        return mDatabase.rawQuery(sql, null);
    }

    /**
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     *
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query,
     *     which will be replaced by the values from selectionArgs. The
     *     values will be bound as Strings.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    Cursor rawQuery(String sql, String[] selectionArgs) {
        return mDatabase.rawQuery(sql, selectionArgs);
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
                    (byte) cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7));
        } catch (CursorIndexOutOfBoundsException ex) {
            Log.e("Database","Database: Failed to Find Duck with ID: " + birdID);
            bird = null;
        }

        cursor.close();
        closeDatabase();
        return bird;
    }

    /**
     * Add bird ID to the birds_seen table
     * @param birdID ID of the Bird
     * @return True if data is added
     */
    public boolean addData(String birdID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BS_COL1, birdID);

        String pattern = "dd-MM-yyyy";
        String currentDate = new SimpleDateFormat(pattern).format(new Date());

        contentValues.put(BS_COL2, currentDate);

        Log.d("Database","addData: Adding " + birdID + " and " + currentDate + " to " + BS_TABLE_NAME);
        long result = db.insert(BS_TABLE_NAME, null, contentValues);

        return (result != -1);
    }

    /**
     * Add bird IDs to the birds_seen table
     * @param birdIDs List of Bird IDs
     * @return True if data is added
     */
    public boolean addData(List<String> birdIDs) {
        for (String ID : birdIDs) {
            if (!addData(ID)) {
                return false;
            }
        }
        return true;
    }
}
