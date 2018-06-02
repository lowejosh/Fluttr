package com.example.charles.opencv;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.charles.opencv.Database.Database;
import com.example.charles.opencv.TwentyQuestion.Bird;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseInstrumentedTest {
    private Context context;
    private Database db;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
        db = new Database(context);
    }

    //aaa So that this test occurs first
    @Test
    public void aaaDatabaseCreation() {
        Assert.assertTrue(true);
    }

    @Test
    public void getBirdSuccess() {
        Bird bird = db.getBird(1);
        Assert.assertEquals(bird.getBirdID(), 1);
    }

    @Test
    public void getBirdInvalid() {
        Bird bird = db.getBird(0);

    }
}
