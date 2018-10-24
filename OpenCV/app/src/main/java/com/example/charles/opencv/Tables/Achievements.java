package com.example.charles.opencv.Tables;

/**
 * This DataType contains all information contained in the Birds Table
 */
public class Achievements {
    private int ID;
    private String name;
    private int progress;

    public Achievements(int ID, String name, int progress) {
        this.ID = ID;
        this.name = name;
        this.progress = progress;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getProgress() {
        return progress;
    }
}
