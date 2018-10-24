package com.example.charles.opencv.Database;

import android.content.Context;
import android.database.Cursor;

import com.example.charles.opencv.Tables.Achievements;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls Bird Bank specific database functionality
 */
public class AchievementsDatabase extends Database {
    public AchievementsDatabase(Context context) {
        super(context);
    }

    static public int SHARE = 1;
    static public int NEWS = 2;
    static public int RECORD = 3;

    /**
     * Retrieve an achievement from the database
     * @param ID Achievements ID
     * @return Achievements
     */
    public Achievements getAchievement(int ID) {
        Achievements ach;

        openDatabase();
        Cursor cursor = rawQuery("SELECT * FROM Achievements WHERE AchievementID = " + ID);
        cursor.moveToFirst();

        ach = new Achievements(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));

        cursor.close();
        closeDatabase();

        return ach;
    }

    /**
     * Retrieve all achievements from the database
     * @return List of Achievements
     */
    public List<Achievements> getAchievements() {
        List<Achievements> achievements = new ArrayList<>();

        openDatabase();
        Cursor cursor = rawQuery("SELECT * FROM Achievements");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Achievements ach = new Achievements(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            achievements.add(ach);

            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();

        return achievements;
    }

    /**
     * Update an achievement with a new progress value. If incrementing use incrementAchievement instead.
     * @param ID Achievements ID
     * @param progress Achievements Progress
     */
    public void updateAchievement(int ID, int progress) {
        String query = "UPDATE Achievements SET Progress = " + progress + " WHERE AchievementID = " + ID;

        openDatabase();
        execSQL(query);
        closeDatabase();
    }

    /**
     * Increment and achievement with a new progress value
     * @param ID Achievements ID
     */
    public void incrementAchievement(int ID) {
        Achievements ach = getAchievement(ID);
        updateAchievement(ID, ach.getProgress() + 1);
    }
}
