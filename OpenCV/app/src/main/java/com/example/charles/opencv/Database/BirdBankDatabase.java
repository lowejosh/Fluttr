package com.example.charles.opencv.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.charles.opencv.Tables.Bird;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls Bird Bank specific database functionality
 */
public class BirdBankDatabase extends Database {
    public BirdBankDatabase(Context context) {
        super(context);
    }

    /**
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
     * Get the seen_bird table cursor
     * @return Cursor of bird IDs
     */
    private Cursor getSeenBirdCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BS_TABLE_NAME;
        return db.rawQuery(query, null);
    }

    /**
     * Get a list of seen birds
     * @return List of seen birds
     */
    public List<Bird> getSeenBirdList() {
        Cursor data = getSeenBirdCursor();
        List<Bird> mList = new ArrayList<>();
        while(data.moveToNext()) {
            mList.add(this.getBird(data.getInt(0)));
        }
        return mList;
    }

    /**
     * Get a list of dates for seen birds
     * @return List of dates for seen birds
     */
    public List<String> getSeenBirdDateList() {
        Cursor data = getSeenBirdCursor();
        List<String> dateList = new ArrayList<>();
        while(data.moveToNext()) {
            dateList.add(data.getString(1));
        }
        return dateList;
    }

    /**
     * Clears the birds seen table
     */
    public void clearBirdsSeen() {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }
}
