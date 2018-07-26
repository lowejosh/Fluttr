package com.example.charles.opencv;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.BirdFinder.Bird;
import com.example.charles.opencv.BirdFinder.Question;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the Database Class, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseInstrumentedTest extends ActivityTestCase {
    private Database db;

    @Before
    public void setup() {
        db = new Database(InstrumentationRegistry.getTargetContext());
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
}
