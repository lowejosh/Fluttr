package com.example.charles.opencv.Gallery.items;


import android.net.Uri;

/**
 * a photo item.
 */
public class PhotoItem {

    // Ivars.
    private Uri thumbnailUri;
    private Uri fullImageUri;

    public PhotoItem(Uri thumbnailUri,Uri fullImageUri) {
        this.thumbnailUri = thumbnailUri;
        this.fullImageUri = fullImageUri;
    }

    /**
     * getters/setters
     */
    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public Uri getFullImageUri() {
        return fullImageUri;
    }

    public void setFullImageUri(Uri fullImageUri) {
        this.fullImageUri = fullImageUri;
    }
}
