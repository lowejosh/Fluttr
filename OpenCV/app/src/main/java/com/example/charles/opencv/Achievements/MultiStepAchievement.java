package com.example.charles.opencv.Achievements;

public class MultiStepAchievement extends Achievement {
    private int maxValue;
    private int currentValue;

    /**
     * Achievements with multiple completion conditions.
     * @param name Name of the achievement
     * @param description Description of the achievement
     * @param maxValue Max value for achievement to hit
     * @param currentValue Current progress through the achievement
     */
    public MultiStepAchievement(String name, String description, int maxValue, int currentValue) {
        super(name, description, currentValue >= maxValue);
        this.maxValue = maxValue;
        this.currentValue = currentValue;
    }

    /**
     * Returns true if the achievement is a singular step
     * @return True if the achievement is a singular step
     */
    @Override
    public boolean isSingular() {
        return false;
    }

    /**
     * Max value to hit for the achievement to activate
     * @return Int value of the max value to hit for the achievement to activate
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Current progress through the achievement
     * @return Int value of the current progress through the achievement
     */
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * Returns true if the achievement is only partially complete
     * @return True if the achievement is only partially complete
     */
    public boolean partialComplete() { return currentValue > 0 && currentValue < maxValue; }
}
