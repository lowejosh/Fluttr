package com.example.charles.opencv;

import android.support.test.runner.AndroidJUnit4;
import android.util.SparseArray;

import com.example.charles.opencv.Tables.Feature;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for the Feature Class, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
public class FeatureInstrumentedTest {
    private static SparseArray<String> goodArray = new SparseArray<>();
    private static SparseArray<String> negativeArray = new SparseArray<>();
    private static SparseArray<String> zeroArray = new SparseArray<>();
    private static SparseArray<String> gapArray = new SparseArray<>();
    private static SparseArray<String> emptyArray = new SparseArray<>();
    private static SparseArray<String> mixedArray = new SparseArray<>();
    private static SparseArray<String> notEnoughElementsArray = new SparseArray<>();
    private static SparseArray<String> minimumElementsArray = new SparseArray<>();
    private static SparseArray<String> noUnknownArray = new SparseArray<>();
    private static SparseArray<String> noOtherArray = new SparseArray<>();
    private static SparseArray<String> noUnknownAndOtherArray = new SparseArray<>();

    @Before
    public void resetSystem() {
        Feature.reset();

        goodArray.clear();
        negativeArray.clear();
        zeroArray.clear();
        goodArray.clear();
        emptyArray.clear();
        mixedArray.clear();
        notEnoughElementsArray.clear();
        minimumElementsArray.clear();
        noUnknownArray.clear();
        noOtherArray.clear();
        noUnknownAndOtherArray.clear();

        goodArray.put(1, "Unknown");
        goodArray.put(2, "Other");
        goodArray.put(3, "Prominent");
        goodArray.put(4, "Curved");
        goodArray.put(5, "Long");
        goodArray.put(6, "Obvious Coloured Eye Ring");
        goodArray.put(7, "Obvious White Patch");
        goodArray.put(8, "Obvious Coloured Patch");
        goodArray.put(9, "Brightly Coloured");
        goodArray.put(10, "Shield");
        goodArray.put(11, "Distinctively Masked");
        goodArray.put(12, "Crest");
        goodArray.put(13, "Topknot");
        goodArray.put(14, "Knob");
        goodArray.put(15, "Lobe");
        goodArray.put(16, "Obvious Streaks");

        negativeArray.put(-1, "Unknown");
        negativeArray.put(-2, "Other");
        negativeArray.put(-3, "Prominent");
        negativeArray.put(-4, "Curved");

        zeroArray.put(0, "Unknown");
        zeroArray.put(1, "Other");
        zeroArray.put(2, "Prominent");
        zeroArray.put(3, "Curved");

        gapArray.put(1, "Unknown");
        gapArray.put(5, "Other");
        gapArray.put(10, "Prominent");
        gapArray.put(15, "Curved");

        mixedArray.put(4, "Unknown");
        mixedArray.put(9, "Other");
        mixedArray.put(2, "Prominent");
        mixedArray.put(10, "Curved");
        mixedArray.put(1, "Long");
        mixedArray.put(5, "Obvious Coloured Eye Ring");
        mixedArray.put(6, "Obvious White Patch");
        mixedArray.put(3, "Obvious Coloured Patch");
        mixedArray.put(8, "Brightly Coloured");
        mixedArray.put(7, "Shield");

        notEnoughElementsArray.put(0, "Unknown");
        notEnoughElementsArray.put(1, "Other");

        minimumElementsArray.put(1, "Other");
        minimumElementsArray.put(2, "Unknown");
        minimumElementsArray.put(3, "Curved");

        noUnknownArray.put(1, "Other");
        noUnknownArray.put(2, "Prominent");
        noUnknownArray.put(3, "Curved");
        noUnknownArray.put(4, "Lobe");

        noOtherArray.put(1, "Unknown");
        noOtherArray.put(2, "Prominent");
        noOtherArray.put(3, "Curved");
        noOtherArray.put(4, "Lobe");

        noUnknownAndOtherArray.put(1, "Prominent");
        noUnknownAndOtherArray.put(2, "Curved");
        noUnknownAndOtherArray.put(3, "Long");
        noUnknownAndOtherArray.put(4, "Obvious Coloured Eye Ring");
    }

    @Test
    public void goodArray() {
        Feature.loadFeatures(goodArray, goodArray);
    }

    @Test
    public void negativeArray() {
        Feature.loadFeatures(negativeArray, negativeArray);
    }

    @Test
    public void zeroArray() {
        Feature.loadFeatures(zeroArray, zeroArray);
    }

    @Test
    public void gapArray() {
        Feature.loadFeatures(gapArray, gapArray);
    }

    @Test
    public void mixArray() {
        Feature.loadFeatures(mixedArray, mixedArray);
    }

    @Test
    public void minimumElementsArray() {
        Feature.loadFeatures(minimumElementsArray, minimumElementsArray);
    }

    @Test(expected = RuntimeException.class)
    public void tooLargeImageArray() {
        Feature.loadFeatures(minimumElementsArray, goodArray);
    }

    @Test(expected = RuntimeException.class)
    public void tooSmallImageArray() {
        Feature.loadFeatures(goodArray, minimumElementsArray);
    }

    @Test(expected = RuntimeException.class)
    public void multiLoadArray() {
        Feature.loadFeatures(goodArray, goodArray);
        Feature.loadFeatures(negativeArray, negativeArray);
    }

    @Test(expected = RuntimeException.class)
    public void noArray() {
        Feature.loadFeatures(emptyArray, emptyArray);
    }

    @Test(expected = RuntimeException.class)
    public void noOtherArray() {
        Feature.loadFeatures(noOtherArray, noOtherArray);
    }

    @Test(expected = RuntimeException.class)
    public void noUnknownArray() {
        Feature.loadFeatures(noUnknownArray, noUnknownArray);
    }

    @Test(expected = RuntimeException.class)
    public void noUnknownAndOtherArray() {
        Feature.loadFeatures(noUnknownAndOtherArray, noUnknownAndOtherArray);
    }

    @Test(expected = RuntimeException.class)
    public void notEnoughElementsArray() {
        Feature.loadFeatures(notEnoughElementsArray, notEnoughElementsArray);
    }

    @Test(expected = RuntimeException.class)
    public void noArrayValueOf() {
        Feature.valueOf(1);
    }

    @Test
    public void valueOfSuccess() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertEquals("Prominent", Feature.valueOf(goodArray.keyAt(goodArray.indexOfValue("Prominent"))));
    }

    @Test
    public void valueOfMin() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertEquals("Long", Feature.valueOf(goodArray.keyAt(goodArray.indexOfValue("Long"))));
    }

    @Test
    public void valueOfMinNegative() {
        Feature.loadFeatures(negativeArray, negativeArray);
        Assert.assertEquals("Prominent", Feature.valueOf(negativeArray.keyAt(negativeArray.indexOfValue("Prominent"))));
    }

    @Test
    public void valueOfMinMixed() {
        Feature.loadFeatures(mixedArray, mixedArray);
        Assert.assertEquals("Prominent", Feature.valueOf(mixedArray.keyAt(mixedArray.indexOfValue("Prominent"))));
    }

    @Test
    public void valueOfMinGap() {
        Feature.loadFeatures(gapArray, gapArray);
        Assert.assertEquals("Prominent", Feature.valueOf(gapArray.keyAt(gapArray.indexOfValue("Prominent"))));
    }

    @Test
    public void valueOfMinZero() {
        Feature.loadFeatures(zeroArray, zeroArray);
        Assert.assertEquals("Unknown", Feature.valueOf(zeroArray.keyAt(zeroArray.indexOfValue("Unknown"))));
    }

    @Test
    public void valueOfMax() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertEquals("Curved", Feature.valueOf(goodArray.keyAt(goodArray.indexOfValue("Curved"))));
    }

    @Test
    public void valueOfMaxNegative() {
        Feature.loadFeatures(negativeArray, negativeArray);
        Assert.assertEquals("Unknown", Feature.valueOf(negativeArray.keyAt(negativeArray.indexOfValue("Unknown"))));
    }

    @Test
    public void valueOfMaxMixed() {
        Feature.loadFeatures(mixedArray, mixedArray);
        Assert.assertEquals("Curved", Feature.valueOf(mixedArray.keyAt(mixedArray.indexOfValue("Curved"))));
    }

    @Test
    public void valueOfMaxGap() {
        Feature.loadFeatures(gapArray, gapArray);
        Assert.assertEquals("Curved", Feature.valueOf(gapArray.keyAt(gapArray.indexOfValue("Curved"))));
    }

    @Test
    public void valueOfMaxZero() {
        Feature.loadFeatures(zeroArray, zeroArray);
        Assert.assertEquals("Curved", Feature.valueOf(zeroArray.keyAt(zeroArray.indexOfValue("Curved"))));
    }

    @Test
    public void valueOfNegative() {
        Feature.loadFeatures(negativeArray, negativeArray);
        Assert.assertEquals("Prominent", Feature.valueOf(negativeArray.keyAt(negativeArray.indexOfValue("Prominent"))));
    }

    @Test
    public void valueOfZero() {
        Feature.loadFeatures(zeroArray, zeroArray);
        Assert.assertEquals("Unknown", Feature.valueOf(zeroArray.keyAt(zeroArray.indexOfValue("Unknown"))));
    }

    @Test
    public void valueOfOutOfBounds() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertEquals("Unknown", Feature.valueOf(17));
    }

    @Test(expected = RuntimeException.class)
    public void noArrayIsUnknown() {
        Feature.isUnknown(1);
    }

    @Test
    public void isUnknownSuccess() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertTrue(Feature.isUnknown(goodArray.keyAt(goodArray.indexOfValue("Unknown"))));
    }

    @Test
    public void isUnknownFailure() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertFalse(Feature.isUnknown(goodArray.keyAt(goodArray.indexOfValue("Prominent"))));
    }

    @Test
    public void isUnknownNegative() {
        Feature.loadFeatures(negativeArray, negativeArray);
        Assert.assertTrue(Feature.isUnknown(-1));
    }

    @Test
    public void isUnknownZero() {
        Feature.loadFeatures(zeroArray, zeroArray);
        Assert.assertTrue(Feature.isUnknown(0));
    }

    @Test
    public void isUnknownNonDefault() {
        Feature.loadFeatures(mixedArray, mixedArray);
        Assert.assertTrue(Feature.isUnknown(4));
    }

    @Test
    public void isUnknownOutOfBounds() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertFalse(Feature.isUnknown(17));
    }

    @Test(expected = RuntimeException.class)
    public void noArrayIsOther() {
        Feature.isOther(1);
    }

    @Test
    public void isOtherSuccess() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertTrue(Feature.isOther(goodArray.keyAt(goodArray.indexOfValue("Other"))));
    }

    @Test
    public void isOtherFailure() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertFalse(Feature.isOther(goodArray.keyAt(goodArray.indexOfValue("Prominent"))));
    }

    @Test
    public void isOtherNegative() {
        Feature.loadFeatures(negativeArray, negativeArray);
        Assert.assertTrue(Feature.isOther(-2));
    }

    @Test
    public void isOtherZero() {
        Feature.loadFeatures(zeroArray, zeroArray);
        Assert.assertTrue(Feature.isOther(1));
    }

    @Test
    public void isOtherNonDefault() {
        Feature.loadFeatures(mixedArray, mixedArray);
        Assert.assertTrue(Feature.isOther(9));
    }

    @Test
    public void isOtherOutOfBounds() {
        Feature.loadFeatures(goodArray, goodArray);
        Assert.assertFalse(Feature.isOther(17));
    }
}
