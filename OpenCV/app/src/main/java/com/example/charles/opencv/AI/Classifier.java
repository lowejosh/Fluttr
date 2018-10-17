package com.example.charles.opencv.AI;

import android.graphics.Bitmap;
import android.graphics.RectF;

import java.util.List;

public interface Classifier {

    /**
     * An immutable result returned by a Classifier describing what was recognised
     */

    public class Recognition {
        /**
         * A unique identifier for what has been recognised. Specific to the class, not the instance
         * of the object.
         */
        private final String id;

        /**
         * Display name for the recognition.
         */
        private final String title;

        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better
         */
        private final Float confidence;

        /**
         * Optional location within the source image for the location of the recognised object
         */
        private RectF location;

        public Recognition(String id, String title, Float confidence, RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        public RectF getLocation() {
            return location;
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "]";
            }

            if (title != null) {
                resultString += title + " ";
            }

            if (id!= null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }

            if (location != null) {
                resultString += location + " ";
            }

            return resultString.trim();
        }
    }

    List<Recognition> recogniseImage(Bitmap bitmap);

    void enableStatLogging(final boolean debug);

    String getStatString();

    void close();
}
