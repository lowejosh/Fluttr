package com.example.charles.opencv.Achievements;

public class Achievement {
    private String name;
    private String description;
    private boolean complete;

    /**
     * Achievements with a single completion condition.
     * @param name Name of the achievement
     * @param description Description of the achievement
     * @param complete True if the achievement is completed
     */
    public Achievement(String name, String description, boolean complete) {
        this.name = name;
        this.description = description;
        this.complete = complete;
    }

    /**
     * Returns the name of the achievement
     * @return Name of the achievement
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the achievement
     * @return Description of the achievement
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns true if the achievement has been completed
     * @return True if the achievement has been completed
     */
    public boolean isComplete() {
        return complete;
    }
}
