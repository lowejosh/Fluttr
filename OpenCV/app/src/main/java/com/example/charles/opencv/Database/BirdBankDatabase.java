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
