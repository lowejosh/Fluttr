package charles.database.model;

public class Duck {
    private int duckID;
    private String name;
    private String image;
    private short atlasNo;
    private byte minSize;
    private byte maxSize;

    /**
     * Duck object which contains all information stored inside the ducks table.
     *
     * @param duckID DuckID
     * @param name Duck Name
     * @param image Duck Image Path inside res/drawable
     * @param atlasNo Duck Atlas Number
     * @param minSize Duck Minimum Size (cm)
     * @param maxSize Duck Maximum Size (cm)
     */
    public Duck(int duckID, String name, String image, short atlasNo, byte minSize, byte maxSize) {
        this.duckID = duckID;
        this.name = name;
        this.image = image;
        this.atlasNo = atlasNo;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    /**
     * Returns the DuckID of this Duck
     * @return DuckID of this Duck
     */
    public int getDuckID() {
        return duckID;
    }

    /**
     * Returns the Name of the this Duck
     * @return Name of this Duck
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Image path of this duck inside res/drawable
     * @return Image Path of this Duck
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns the Atlas Number of this Duck
     * @return Atlas Number of this Duck
     */
    public short getAtlasNo() {
        return atlasNo;
    }

    /**
     * Returns the Minimum Size of this Duck in cm
     * @return Minimum Size of this Duck (cm)
     */
    public int getMinSize() {
        //Java stores bytes as signed, convert to int for 0-254 range
        return minSize & 0xFF;
    }

    /**
     * Returns the Maximum Size of this Duck in cm
     * @return Maximum Size of this Duck (cm)
     */
    public int getMaxSize() {
        //Java stores bytes as signed, convert to int for 0-254 range
        return maxSize & 0xFF;
    }
}
