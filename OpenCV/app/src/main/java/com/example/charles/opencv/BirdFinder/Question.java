package com.example.charles.opencv.BirdFinder;

/**
 * This DataType contains all information contained in the Question Table
 */
public class Question {
    private String table;
    private String question;

    /**
     * Create Question Data Type
     *
     * @param table Table Question Belongs to
     * @param question Question to be Asked
     */
    public Question(String table, String question) {
        this.table = table;
        this.question = question;
    }

    /**
     * Get SQL Table Question belongs to
     *
     * @return String SQL Table
     */
    public String getTable() {
        return table;
    }

    /**
     * Get Feature Name of the Question inside the SQL Table
     *
     * @return Feature Name inside SQL Table
     */
    public String getFeature() {return getTable(); }

    /**
     * Get the Question to be Asked
     *
     * @return Question Attached to Feature
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Display Question when toString() is queried, used for debugging purposes
     *
     * @return Question Attached to Feature
     */
    @Override
    public String toString() {
        return table;
    }
}
