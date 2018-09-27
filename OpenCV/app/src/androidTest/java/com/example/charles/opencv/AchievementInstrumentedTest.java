package com.example.charles.opencv;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.example.charles.opencv.Achievements.Achievement;
import com.example.charles.opencv.Achievements.AchievementManager;
import com.example.charles.opencv.Achievements.MultiStepAchievement;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Unit tests for the Achievement Class, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
public class AchievementInstrumentedTest extends ActivityTestCase {
    @Test
    public void singleStepAchievementTrue() {
        Achievement ach = new Achievement("Test", "Testing", true);
        assertTrue(ach.isComplete());
    }

    @Test
    public void singleStepAchievementFalse() {
        Achievement ach = new Achievement("Test", "Testing", false);
        assertTrue(!ach.isComplete());
    }

    @Test
    public void singleStepAchievementSingular() {
        Achievement ach = new Achievement("Test", "Testing", true);
        assertTrue(ach.isSingular());
    }

    @Test
    public void singleStepAchievementGetName() {
        Achievement ach = new Achievement("Test", "Testing", true);
        assertEquals("Test", ach.getName());
    }

    @Test
    public void singleStepAchievementGetDescription() {
        Achievement ach = new Achievement("Test", "Testing", true);
        assertEquals("Testing", ach.getDescription());
    }

    @Test
    public void multiStepStepAchievementComplete() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", 10, 10);
        assertTrue(!ach.partialComplete());
        assertTrue(ach.isComplete());
    }

    @Test
    public void multiStepStepAchievementOverComplete() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", 10, 11);
        assertTrue(!ach.partialComplete());
        assertTrue(ach.isComplete());
    }

    @Test
    public void multiStepStepAchievementNotComplete() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", 10, 0);
        assertTrue(!ach.partialComplete());
        assertTrue(!ach.isComplete());
    }

    @Test
    public void multiStepStepAchievementPartialComplete() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", 10, 5);
        assertTrue(ach.partialComplete());
        assertTrue(!ach.isComplete());
    }

    @Test
    public void multiStepStepAchievementNotSingular() {
        Achievement ach = new MultiStepAchievement("Test", "Testing", 10, 10);
        assertTrue(!ach.isSingular());
    }

    @Test
    public void multiStepStepAchievementGetName() {
        Achievement ach = new MultiStepAchievement("Test", "Testing", 10, 10);
        assertEquals("Test", ach.getName());
    }

    @Test
    public void multiStepAchievementGetDescription() {
        Achievement ach = new MultiStepAchievement("Test", "Testing", 10, 10);
        assertEquals("Testing", ach.getDescription());
    }

    @Test
    public void multiStepAchievementGetMaxValue() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", 7, 3);
        assertEquals(7, ach.getMaxValue());
    }

    @Test
    public void multiStepAchievementGetCurrentValue() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", 10, 3);
        assertEquals(3, ach.getCurrentValue());
    }

    @Test
    public void multiStepAchievementZeroComplete() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", 0, 0);
        assertTrue(ach.isComplete());
    }

    @Test
    public void multiStepAchievementNegativeComplete() {
        MultiStepAchievement ach = new MultiStepAchievement("Test", "Testing", -10, -10);
        assertTrue(ach.isComplete());
    }

    @Test
    public void achievementManagerGetAchievements() {
        AchievementManager manager = new AchievementManager(InstrumentationRegistry.getTargetContext());
        List<Achievement> list = manager.getAchievements();
        list.size();
        assertTrue(true);
    }

    @Test
    public void achievementManagerGetCompletion() {
        AchievementManager manager = new AchievementManager(InstrumentationRegistry.getTargetContext());
        int completion = manager.getCompletion();
        assertTrue(completion >= 0 && completion <= 100);
    }
}
