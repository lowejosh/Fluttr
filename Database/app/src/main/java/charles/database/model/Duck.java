package charles.database.model;

public class Duck {
    private int duckID;
    private String name;
    private String image;
    private short atlasNo;
    private byte minSize;
    private byte maxSize;

    public Duck(int duckID, String name, String image, short atlasNo, byte minSize, byte maxSize) {
        this.duckID = duckID;
        this.name = name;
        this.image = image;
        this.atlasNo = atlasNo;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public int getDuckID() {
        return duckID;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
    public short getAtlasNo() {
        return atlasNo;
    }

    public int getMinSize() {
        //Java stores bytes as signed, convert to int for 0-254 range
        return minSize & 0xFF;
    }

    public int getMaxSize() {
        //Java stores bytes as signed, convert to int for 0-254 range
        return maxSize & 0xFF;
    }
}
