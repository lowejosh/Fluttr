package com.example.charles.opencv.Achievements;

import android.content.Context;

import com.example.charles.opencv.Database.BirdBankDatabase;
import com.example.charles.opencv.Database.BirdFinderDatabase;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    private List<Achievement> achievements = new ArrayList<>();

    /**
     * Generates a list of achievements and the users progress through them
     * @param context Application context
     */
    public AchievementManager(Context context) {
        BirdBankDatabase bankDB = new BirdBankDatabase(context);
        BirdFinderDatabase findDB = new BirdFinderDatabase(context);

        achievements.add(firstSteps(bankDB));
        achievements.add(rookieHunter(bankDB));
        achievements.add(experiencedHunter(bankDB));
        achievements.add(huntingArtist(bankDB, findDB));
        achievements.add(makingFriends());
        achievements.add(sharingMaster());
        achievements.add(socialWarrior());
        achievements.add(birdStudent());
        achievements.add(birdScholar());
        achievements.add(firstRecording());
        achievements.add(recordingAddicts());
        achievements.add(recordingKing());
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
     * @return First Steps Achievement
     */
    private Achievement firstSteps(BirdBankDatabase bankDB) {
        return new Achievement("First Steps", "Find your first bird_finder", bankDB.getSeenBirdList().size() >= 1);
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

    /**
     * Activates when the user has identified all birds in the app
     * @param bankDB Bird Bank Database
     * @param finderDB Bird Finder Database
     * @return Hunting Artist Achievement
     */
    private Achievement huntingArtist(BirdBankDatabase bankDB, BirdFinderDatabase finderDB) {
        return new MultiStepAchievement("Hunting Artist", "Discover all birds", finderDB.getBirdIDs().size(), bankDB.getSeenBirdList().size());
    }

    /**
     * Activates when the user has shared 1 bird_finder from the bird_finder bird_bank
     * @return Making Friends Achievement
     */
    private Achievement makingFriends() {
        return new Achievement("Making Friends", "Share 1 bird_finder from the bird_finder bird_bank", false);
    }

    /**
     * Activates when the user has shared 10 birds from the bird_finder bird_bank
     * @return Sharing Master Achievement
     */
    private Achievement sharingMaster() {
        return new MultiStepAchievement("Sharing Master", "Share 10 different birds from the bird_finder bird_bank", 10, 0);
    }

    /**
     * Activates when the user has shared 25 birds from the bird_finder bird_bank
     * @return Social Warrior Achievement
     */
    private Achievement socialWarrior() {
        return new MultiStepAchievement("Social Warrior", "Share 25 different birds from the bird_finder bird_bank", 25, 0);
    }

    /**
     * Activates when the user has opened a news article
     * @return Bird Student Achievement
     */
    private Achievement birdStudent() {
        return new Achievement("Bird Student", "Open 1 newspaper article", false);
    }

    /**
     * Activates when the user has opened 5 news article
     * @return Bird Scholar Achievement
     */
    private Achievement birdScholar() {
        return new MultiStepAchievement("Bird Scholar", "Open 5 newspaper articles", 5, 0);
    }

    /**
     * Activates when the user makes their first recording
     * @return First Recording Achievement
     */
    private Achievement firstRecording() {
        return new Achievement("First Recording", "Record your first bird_finder song", false);
    }

    /**
     * Activates when the user makes their first 10 recordings
     * @return Recording Addicts Achievement
     */
    private Achievement recordingAddicts() {
        return new MultiStepAchievement("Recording Addicts", "Record ten bird_finder songs", 10, 0);
    }

    /**
     * Activates when the user makes their first 50 recordings
     * @return Recording Addicts Achievement
     */
    private Achievement recordingKing() {
        return new MultiStepAchievement("Recording Addicts", "Record fifty bird_finder songs", 50, 0);
    }
}
