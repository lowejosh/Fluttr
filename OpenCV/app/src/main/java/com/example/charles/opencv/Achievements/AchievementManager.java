package com.example.charles.opencv.Achievements;

import android.content.Context;

import com.example.charles.opencv.Database.AchievementsDatabase;
import com.example.charles.opencv.Database.BirdBankDatabase;
import com.example.charles.opencv.Database.BirdFinderDatabase;
import com.example.charles.opencv.Tables.Achievements;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    //List of player achievements
    private List<Achievement> achievements = new ArrayList<>();

    /**
     * Generates a list of achievements and the users progress through them
     * @param context Application context
     */
    public AchievementManager(Context context) {
        BirdBankDatabase bankDB = new BirdBankDatabase(context);
        BirdFinderDatabase findDB = new BirdFinderDatabase(context);
        AchievementsDatabase achDB = new AchievementsDatabase(context);

        //List of database achievements
        List<Achievements> achievementsList = achDB.getAchievements();

        achievements.add(firstSteps(bankDB));
        achievements.add(rookieHunter(bankDB));
        achievements.add(experiencedHunter(bankDB));
        achievements.add(huntingArtist(bankDB, findDB));
        achievements.add(makingFriends(achievementsList));
        achievements.add(sharingMaster(achievementsList));
        achievements.add(socialWarrior(achievementsList));
        achievements.add(birdStudent(achievementsList));
        achievements.add(birdScholar(achievementsList));
        achievements.add(firstRecording(achievementsList));
        achievements.add(recordingAddicts(achievementsList));
        achievements.add(recordingKing(achievementsList));
    }

    /**
     * Get percentage of achievements completed. Integer value between 0 and 100.
     * @return Percentage of achievements completed
     */
    public int getCompletion() {
        int percentage = 0;

        for (Achievement ach : achievements) {
            if (ach.isComplete()) {
                percentage++;
            }
        }

        percentage *= 100;
        percentage /= achievements.size();

        return percentage;
    }

    /**
     * Returns a list of achievements
     * @return List of achievements
     */
    public List<Achievement> getAchievements() {
        return achievements;
    }

    /**
     * Activates when the user has identified one bird_finder
     * @param bankDB Bird Bank Database
     * @return First Steps Achievements
     */
    private Achievement firstSteps(BirdBankDatabase bankDB) {
        return new Achievement("First Steps", "Find your first bird_finder", bankDB.getSeenBirdList().size() >= 1);
    }

    /**
     * Activates when the user has identified ten birds
     * @param bankDB Bird Bank Database
     * @return Rookie Hunter Achievements
     */
    private Achievement rookieHunter(BirdBankDatabase bankDB) {
        return new MultiStepAchievement("Rookie Hunter", "Find ten different birds", 10, bankDB.getSeenBirdList().size());
    }

    /**
     * Activates when the user has identified fifty birds
     * @param bankDB Bird Bank Database
     * @return Experienced Hunter Achievements
     */
    private Achievement experiencedHunter(BirdBankDatabase bankDB) {
        return new MultiStepAchievement("Experienced Hunter", "Find fifty different birds", 50, bankDB.getSeenBirdList().size());
    }

    /**
     * Activates when the user has identified all birds in the app
     * @param bankDB Bird Bank Database
     * @param finderDB Bird Finder Database
     * @return Hunting Artist Achievements
     */
    private Achievement huntingArtist(BirdBankDatabase bankDB, BirdFinderDatabase finderDB) {
        return new MultiStepAchievement("Hunting Artist", "Discover all birds", finderDB.getBirdIDs().size(), bankDB.getSeenBirdList().size());
    }

    /**
     * Activates when the user has shared 1 bird from the bird bank
     * @return Making Friends Achievements
     */
    private Achievement makingFriends(List<Achievements> achievementsList) {
        return new Achievement("Making Friends", "Share 1 bird from the bird bank", achievementsList.get(AchievementsDatabase.SHARE - 1).getProgress() >= 1);
    }

    /**
     * Activates when the user has shared 10 birds from the bird bank
     * @return Sharing Master Achievements
     */
    private Achievement sharingMaster(List<Achievements> achievementsList) {
        return new MultiStepAchievement("Sharing Master", "Share 10 different birds from the bird bank", 10, achievementsList.get(AchievementsDatabase.SHARE - 1).getProgress());
    }

    /**
     * Activates when the user has shared 25 birds from the bird bank
     * @return Social Warrior Achievements
     */
    private Achievement socialWarrior(List<Achievements> achievementsList) {
        return new MultiStepAchievement("Social Warrior", "Share 25 different birds from the bird bank", 25, achievementsList.get(AchievementsDatabase.SHARE - 1).getProgress());
    }

    /**
     * Activates when the user has opened a news article
     * @return Bird Student Achievements
     */
    private Achievement birdStudent(List<Achievements> achievementsList) {
        return new Achievement("Bird Student", "Open 1 newspaper article", achievementsList.get(AchievementsDatabase.NEWS - 1).getProgress() >= 1);
    }

    /**
     * Activates when the user has opened 5 news article
     * @return Bird Scholar Achievements
     */
    private Achievement birdScholar(List<Achievements> achievementsList) {
        return new MultiStepAchievement("Bird Scholar", "Open 5 newspaper articles", 5, achievementsList.get(AchievementsDatabase.NEWS - 1).getProgress());
    }

    /**
     * Activates when the user makes their first recording
     * @return First Recording Achievements
     */
    private Achievement firstRecording(List<Achievements> achievementsList) {
        return new Achievement("First Recording", "Record your first bird song", achievementsList.get(AchievementsDatabase.RECORD - 1).getProgress() >= 1);
    }

    /**
     * Activates when the user makes their first 10 recordings
     * @return Recording Addicts Achievements
     */
    private Achievement recordingAddicts(List<Achievements> achievementsList) {
        return new MultiStepAchievement("Recording Addicts", "Record ten bird songs", 10, achievementsList.get(AchievementsDatabase.RECORD - 1).getProgress());
    }

    /**
     * Activates when the user makes their first 50 recordings
     * @return Recording King Achievements
     */
    private Achievement recordingKing(List<Achievements> achievementsList) {
        return new MultiStepAchievement("Recording King", "Record fifty bird songs", 50, achievementsList.get(AchievementsDatabase.RECORD - 1).getProgress());
    }
}
