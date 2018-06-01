package charles.database;

import org.junit.Test;

import charles.database.model.Bird;

import static junit.framework.Assert.assertTrue;

public class ModelUnitTests {
    final int validID = 1;
    final String validName = "Bird";
    final String validPath = "";
    final short validAtlasNo = 1;
    final byte validMinSize = 10;
    final byte validMaxSize = 15;

    @Test
    public void duck_validID() {
        Bird bird = new Bird(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(bird.getDuckID() == 1);
    }

    /*@Test
    public void duck_invalidID() {
        Bird duck = new Bird(-1, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getDuckID() == 1);
    }*/

    @Test
    public void duck_validName() {
        Bird bird = new Bird(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(bird.getName().equals(validName));
    }

    @Test
    public void duck_validPath() {
        Bird bird = new Bird(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(bird.getImage().equals(validPath));
    }

    @Test
    public void duck_validAtlasNo() {
        Bird bird = new Bird(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(bird.getAtlasNo() == validAtlasNo);
    }

    @Test
    public void duck_validMinSize() {
        Bird bird = new Bird(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(bird.getMinSize() == validMinSize);
    }

    @Test
    public void duck_validMaxSize() {
        Bird bird = new Bird(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(bird.getMaxSize() == validMaxSize);
    }
}
