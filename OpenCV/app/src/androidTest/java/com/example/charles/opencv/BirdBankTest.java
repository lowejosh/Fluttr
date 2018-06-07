package com.example.charles.opencv;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.example.charles.opencv.FeatureActivity.BirdFinderActivity;
import com.example.charles.opencv.Database.Database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the
 */
@RunWith(AndroidJUnit4.class)
public class
BirdBankTest extends ActivityTestCase {
    private Database db;

    @Before
    public void setup() {
        // setup database
        db = new Database(InstrumentationRegistry.getTargetContext());

        // clear bird-bank
        db.clearBirdsSeen();
    }

    @Test
    public void addInvalidSeenBird() {
        // prepare looper
        Looper.prepare();

        int expectedSeenBirdListSize = db.getSeenBirdList().size();
        BirdFinderActivity.AddBird("-1", db, InstrumentationRegistry.getTargetContext());
        int actualSeenBirdListSize = db.getSeenBirdList().size();
        assertEquals(expectedSeenBirdListSize, actualSeenBirdListSize);
    }

    @Test
    public void addSeenBird() {
        int expectedSeenBirdListSize = db.getSeenBirdList().size() + 1;
        BirdFinderActivity.AddBird("1", db, InstrumentationRegistry.getTargetContext());
        int actualSeenBirdListSize = db.getSeenBirdList().size();
        assertEquals(expectedSeenBirdListSize, actualSeenBirdListSize);
    }

}
















/*
@RunWith(AndroidJUnit4.class)
public class BirdBankTest extends ActivityTestCase {
    private Database db;

    @Before
    public void setup() {
        // setup database
        db = new Database(InstrumentationRegistry.getTargetContext());

        // clear bird-bank
        db.clearBirdsSeen();
    }

    @Test
    public void addInvalidSeenBird {
        BirdFinderActivity test = new BirdFinderActivity();
        int expectedSeenBirdSize = db.getSeenBirdList().size();
        test.AddBird("0");
        int actualSeenBirdSize = db.getSeenBirdList().size();
        assertEquals(expectedSeenBirdSize, actualSeenBirdSize);
    }
}

*/