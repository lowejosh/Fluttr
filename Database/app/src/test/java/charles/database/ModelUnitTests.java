package charles.database;

import org.junit.Test;

import charles.database.model.Duck;

import static junit.framework.Assert.assertTrue;

public class ModelUnitTests {
    final int validID = 1;
    final String validName = "Duck";
    final String validPath = "";
    final short validAtlasNo = 1;
    final byte validMinSize = 10;
    final byte validMaxSize = 15;

    @Test
    public void duck_validID() {
        Duck duck = new Duck(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getDuckID() == 1);
    }

    /*@Test
    public void duck_invalidID() {
        Duck duck = new Duck(-1, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getDuckID() == 1);
    }*/

    @Test
    public void duck_validName() {
        Duck duck = new Duck(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getName().equals(validName));
    }

    @Test
    public void duck_validPath() {
        Duck duck = new Duck(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getImage().equals(validPath));
    }

    @Test
    public void duck_validAtlasNo() {
        Duck duck = new Duck(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getAtlasNo() == validAtlasNo);
    }

    @Test
    public void duck_validMinSize() {
        Duck duck = new Duck(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getMinSize() == validMinSize);
    }

    @Test
    public void duck_validMaxSize() {
        Duck duck = new Duck(validID, validName, validPath, validAtlasNo, validMinSize, validMaxSize);
        assertTrue(duck.getMaxSize() == validMaxSize);
    }
}
