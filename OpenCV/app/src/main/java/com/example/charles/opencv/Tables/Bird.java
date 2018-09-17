package com.example.charles.opencv.Tables;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
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
    private String description;
    private String sound;


    /**
     * Bird object which contains all information stored inside the birds table.
     *
     * @param birdID BirdID
     * @param name Bird Name
     * @param image Bird Image Path inside assets/images
     * @param atlasNo Bird Atlas Number
     * @param minSize Bird Minimum Size (cm)
     * @param maxSize Bird Maximum Size (cm)
     * @param description Bird Description
     * @param sound Bird sound file inside assets/sound
     */
    public Bird(int birdID, String name, String image, short atlasNo, byte minSize, byte maxSize, String description, String sound) {
        this.birdID = birdID;
        this.name = name;
        this.image = "images/" + image;
        this.atlasNo = atlasNo;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.description = description;
        this.sound = sound;

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
     * Returns the description of the bird
     * @return Description of the bird
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns AssetFileDescriptor sound file of the bird
     * @param context
     * @return
     */
    public AssetFileDescriptor getSound(Context context) {
        AssetFileDescriptor descriptor = null;
        try {
            descriptor = context.getAssets().openFd("sound/" + sound);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return descriptor;
    }

    /**
     * Get the Bitmap Image of the Bird from the assets folder
     *
     * @param context App Context
     * @return Image from file path in Bitmap form
     */
    public Bitmap getImage(Context context) {
        //Update image for ImageView
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(image));
        } catch (IOException | IllegalArgumentException unused) {
            Log.e("AIActivity", "Failed to load image: " + image);

            //If bird image does not exist, display noImage file
            try {
                return BitmapFactory.decodeStream(context.getAssets().open("images/noImage.jpg"));
            } catch (IOException | IllegalArgumentException ex){
                Log.e("AIActivity", "noImage Failed to Load");
                Log.e("AIActivity", ex.getMessage());
            }
        }

        return null;
    }

}
