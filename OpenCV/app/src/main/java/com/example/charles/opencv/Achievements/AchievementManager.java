package com.example.charles.opencv.Achievements;

import android.content.Context;

import com.example.charles.opencv.Database.BirdBankDatabase;
import com.example.charles.opencv.Database.BirdFinderDatabase;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    private List<Achievement> achievements = new ArrayList<>();

    public AchievementManager(Context context) {
        BirdBankDatabase bankDB = new BirdBankDatabase(context);
        BirdFinderDatabase findDB = new BirdFinderDatabase(context);

        achievements.add(firstSteps(bankDB));
        achievements.add(rookieHunter(bankDB));
        achievements.add(experiencedHunter(bankDB));
    }

    /**
     * Returns a list of achievements
     * @return List of achievements
     */
    public List<Achievement> getAchievements() {
        return achievements;
    }

    /**
     * Activates when the user has identified one bird
     * @param bankDB Bird Bank Database
     * @return First Steps Achievement
     */
    private Achievement firstSteps(BirdBankDatabase bankDB) {
        return new Achievement("First Steps", "Find your first bird", bankDB.getSeenBirdList().size() >= 1);
    }

    /**
     * Activates when the user has identified ten birds
     * @param bankDB Bird Bank Database
     * @return Rookie Hunter Achievement
     */
    private Achievement rookieHunter(BirdBankDatabase bankDB) {
        return new MultiStepAchievement("Rookie Hunter", "Find ten different birds", 10, bankDB.getSeenBirdList().size());
    }

    /**
     * Activates when the user has identified fifty birds
     * @param bankDB Bird Bank Database
     * @return Experienced Hunter Achievement
     */
    private Achievement experiencedHunter(BirdBankDatabase bankDB) {
        return new MultiStepAchievement("Experienced Hunter", "Find fifty different birds", 50, bankDB.getSeenBirdList().size());
    }
}
