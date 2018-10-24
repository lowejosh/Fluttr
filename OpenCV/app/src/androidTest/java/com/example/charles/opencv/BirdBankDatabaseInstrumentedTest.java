package com.example.charles.opencv;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.example.charles.opencv.Tables.Bird;
import com.example.charles.opencv.Database.BirdBankDatabase;
import com.example.charles.opencv.FeatureActivity.BirdFinderActivity;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Unit tests for the Database Class, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BirdBankDatabaseInstrumentedTest extends ActivityTestCase {
    private BirdBankDatabase db;

    @Before
    public void setup() {
        db = new BirdBankDatabase(InstrumentationRegistry.getTargetContext());

        // clear bird_finder-bird_bank
        db.clearBirdsSeen();
    }

    //aaa So that this test occurs first
    @Test
    public void aaaDatabaseCreation() {
        //Checks if database was made in startup
        assertTrue(true);
    }

    @Test
    public void getBirdSuccess() {
        Bird bird = db.getBird(1);
        assertEquals(1, bird.getBirdID());
    }

    @Test
    public void getBirdInvalid() {
        Bird bird = db.getBird(0);
        assertNull(bird);
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
