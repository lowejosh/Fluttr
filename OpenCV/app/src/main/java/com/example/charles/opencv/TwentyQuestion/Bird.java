package com.example.charles.opencv.TwentyQuestion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;

/**
 * This DataType contains all information contained in the Birds Table
 */
public class Bird {
    private int birdID;
    private String name;
    private String image;
    private short atlasNo;
    private byte minSize;
    private byte maxSize;


    /**
     * Bird object which contains all information stored inside the birds table.
     *
     * @param birdID BirdID
     * @param name Bird Name
     * @param image Bird Image Path inside res/drawable
     * @param atlasNo Bird Atlas Number
     * @param minSize Bird Minimum Size (cm)
     * @param maxSize Bird Maximum Size (cm)
     */
    public Bird(int birdID, String name, String image, short atlasNo, byte minSize, byte maxSize) {
        this.birdID = birdID;
        this.name = name;
        this.image = "images/" + image;
        this.atlasNo = atlasNo;
        this.minSize = minSize;
        this.maxSize = maxSize;

        if (birdID < 1) {
            Log.e("Bird", "Bad ID Given: " + birdID);
        }

        if (atlasNo < 1) {
            Log.e("Bird", "Bad Atlas Number Given: " + atlasNo);
        }
    }

    /**
     * Returns the BirdID of this Bird
     * @return BirdID of this Bird
     */
    public int getBirdID() {
        return birdID;
    }

    /**
     * Returns the Name of the this Bird
     * @return Name of this Bird
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Atlas Number of this Bird
     * @return Atlas Number of this Bird
     */
    public short getAtlasNo() {
        return atlasNo;
    }

    /**
     * Returns the Minimum Size of this Bird in cm
     * @return Minimum Size of this Bird (cm)
     */
    public int getMinSize() {
        //Java stores bytes as signed, convert to int for 0-254 range
        return minSize & 0xFF;
    }

    /**
     * Returns the Maximum Size of this Bird in cm
     * @return Maximum Size of this Bird (cm)
     */
    public int getMaxSize() {
        //Java stores bytes as signed, convert to int for 0-254 range
        return maxSize & 0xFF;
    }

    /**
     * Get the Bitmap Image of the Bird from the assets folder
     *
     * @param context App Context
     * @return Image from file path in Bitmap form
     */
    public Bitmap getBirdImage(Context context) {
        //Update image for ImageView
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(image));
        } catch (IOException unused) {
            //If bird image does not exist, display noImage file
            try {
                return BitmapFactory.decodeStream(context.getAssets().open("images/noImage.jpg"));
            } catch (IOException ex){
                Log.e("MainActivity", "noImage Failed to Load");
                Log.e("MainActivity", ex.getMessage());
            }
            Log.e("MainActivity", "Failed to load image: " + image);
        }

        return null;
    }

}
