package com.example.charles.opencv;

import android.support.test.runner.AndroidJUnit4;

import com.example.charles.opencv.Tables.Feature;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for the Feature Class, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
public class FeatureInstrumentedTest {
    @Test
    public void valueOfSuccess() {
        Assert.assertEquals("Prominent", Feature.valueOf(1));
    }

    @Test
    public void valueOfUnknown() { Assert.assertEquals("Unknown", Feature.valueOf(Feature.UNKNOWN)); }

    @Test
    public void valueOfOther() {
        Assert.assertEquals("Other", Feature.valueOf(Feature.OTHER));
    }

    @Test
    public void valueOfNegative() { Assert.assertEquals("Unknown", Feature.valueOf(Feature.UNKNOWN)); }

    @Test
    public void valueOfOutOfBounds() {
        Assert.assertEquals("Unknown", Feature.valueOf(1000));
    }

    @Test
    public void isUnknownSuccess() {
        Assert.assertTrue(Feature.isUnknown(Feature.UNKNOWN));
    }

    @Test
    public void isUnknownFailure() {
        Assert.assertFalse(Feature.isUnknown(Feature.UNKNOWN));
    }

    @Test
    public void isOtherSuccess() {
        Assert.assertTrue(Feature.isOther(Feature.OTHER));
    }

    @Test
    public void isOtherFailure() {
        Assert.assertFalse(Feature.isOther(Feature.OTHER));
    }
}
