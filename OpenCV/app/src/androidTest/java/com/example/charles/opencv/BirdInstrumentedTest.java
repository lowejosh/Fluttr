package com.example.charles.opencv;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.example.charles.opencv.Database.BirdFinderDatabase;
import com.example.charles.opencv.Tables.Bird;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Unit tests for the Duck DataType, requires the physical or emulated machine to run.
 */
@RunWith(AndroidJUnit4.class)
public class BirdInstrumentedTest extends ActivityTestCase {
    private Context context;
    private Bird bird;
    private int ID = 1;
    private String name = "Bird";
    private String image = "black_kite.jpg";
    private short atlasNo = 100;
    private byte minSize = 10;
    private byte maxSize = 20;
    private String description = "Description";
    private String sound = "Australasian Shoveler.mp3";

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        bird = new Bird(ID, name, image, atlasNo, minSize, maxSize, description, sound);
    }

    @Test
    public void birdSuccess() {
        assertTrue(true);
    }

    @Test
    public void birdID() {
        assertEquals(ID, bird.getBirdID());
    }

    @Test
    public void birdIDNegativeID() {
        bird = new Bird(-1, name, image, atlasNo, minSize, maxSize, description, sound);
        assertEquals(-1, bird.getBirdID());
    }

    @Test
    public void birdName() {
        assertEquals(name, bird.getName());
    }

    @Test
    public void birdNameEmpty() {
        bird = new Bird(ID, "", image, atlasNo, minSize, maxSize, description, sound);
        assertEquals("", bird.getName());
    }

    public void birdGetImageBitmap() throws IOException {
        assertEquals(BitmapFactory.decodeStream(context.getAssets().open("image/" + image)), bird.getImage(context));
    }

    public void birdGetImageBitmapBadImage() throws IOException {
        bird = new Bird(ID, name, "", atlasNo, minSize, maxSize, description, sound);
        assertEquals(BitmapFactory.decodeStream(context.getAssets().open("images/noImage.jpg")), bird.getImage(context));
    }

    @Test
    public void birdAtlasNo() {
        assertEquals(atlasNo, bird.getAtlasNo());
    }

    @Test
    public void birdAtlasNoNegativeMax() {
        bird = new Bird(ID, name, image, (short)-32768, minSize, maxSize, description, sound);
        assertEquals((short)-32768, bird.getAtlasNo());
    }

    @Test
    public void birdAtlasNoMax() {
        bird = new Bird(ID, name, image, (short)32767, minSize, maxSize, description, sound);
        assertEquals((short)32767, bird.getAtlasNo());
    }

    @Test
    public void birdMinSize() {
        assertEquals(minSize, bird.getMinSize());
    }

    @Test
    public void birdMinSizeMax() {
        bird = new Bird(ID, name, image, atlasNo, (byte)255, maxSize, description, sound);
        assertEquals(255, bird.getMinSize());
    }

    @Test
    public void birdMinSizeMin() {
        bird = new Bird(ID, name, image, atlasNo, (byte)0, maxSize, description, sound);
        assertEquals(0, bird.getMinSize());
    }

    @Test
    public void birdMaxSize() {
        assertEquals(maxSize, bird.getMaxSize());
    }

    @Test
    public void birdMaxSizeMax() {
        bird = new Bird(ID, name, image, atlasNo, minSize, (byte)255, description, sound);
        assertEquals(255, bird.getMaxSize());
    }

    @Test
    public void birdMaxSizeMin() {
        bird = new Bird(ID, name, image, atlasNo, minSize, (byte)0, description, sound);
        assertEquals(0, bird.getMaxSize());
    }

    @Test
    public void birdDescription() {
        assertEquals(description, bird.getDescription());
    }

    @Test
    public void birdDescriptionInvalid() {
        bird = new Bird(ID, name, image, atlasNo, minSize, maxSize, "                       ",sound);
        assertEquals("", bird.getDescription());
    }

    @Test
    public void birdDescriptionNull() {
        bird = new Bird(ID, name, image, atlasNo, minSize, maxSize, null,sound);
        assertEquals(null, bird.getDescription());
    }

    @Test
    public void birdSound() {
        AssetFileDescriptor descriptor = null;
        try {
            descriptor = context.getAssets().openFd("sound/" + sound);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(descriptor.describeContents(), bird.getSound(context).describeContents());
    }

    @Test
    public void birdSoundInvalid() {
        bird = new Bird(ID, name, image, atlasNo, minSize, maxSize, description, "blurb");
        assertEquals(null, bird.getSound(context));
    }

    @Test
    public void birdSoundNull() {
        bird = new Bird(ID, name, image, atlasNo, minSize, maxSize, description, null);
        assertEquals(null, bird.getSound(context));
    }

    @Test
    public void birdImageWorks() {
        BirdFinderDatabase db = new BirdFinderDatabase(context);
        List<Integer> birdList = db.getBirdIDs();
        for (int i: birdList) {
            Bird bird = db.getBird(i);
            assertTrue(bird.imageExists(context));
        }
    }
}
