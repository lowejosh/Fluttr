package com.example.charles.opencv;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.example.charles.opencv.Activity.TwentyQuestionActivity;
import com.example.charles.opencv.BirdBank.ListBirdAdapter;
import com.example.charles.opencv.Activity.BirdBankActivity;
import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.TwentyQuestion.Bird;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


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
        TwentyQuestionActivity.AddBird("-1", db, InstrumentationRegistry.getTargetContext());
        int actualSeenBirdListSize = db.getSeenBirdList().size();
        assertEquals(expectedSeenBirdListSize, actualSeenBirdListSize);
    }

    @Test
    public void addSeenBird() {
        int expectedSeenBirdListSize = db.getSeenBirdList().size() + 1;
        TwentyQuestionActivity.AddBird("1", db, InstrumentationRegistry.getTargetContext());
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
        TwentyQuestionActivity test = new TwentyQuestionActivity();
        int expectedSeenBirdSize = db.getSeenBirdList().size();
        test.AddBird("0");
        int actualSeenBirdSize = db.getSeenBirdList().size();
        assertEquals(expectedSeenBirdSize, actualSeenBirdSize);
    }
}

*/