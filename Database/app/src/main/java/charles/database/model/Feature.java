package charles.database.model;

public class Feature {
    private int duckID;
    private byte feature;
    private String table;

    public Feature(int duckID, byte feature, String table) {
        this.duckID = duckID;
        this.feature = feature;
        this.table = table;
    }

    public int getDuckID() {
        return duckID;
    }

    public byte getFeature() {
        return feature;
    }

    public String getFeatureRepresentation() {
        return FeatureOptions.getValue((int)(feature & 0xFF));
    }

    public String getTable() {
        return table;
    }
}
