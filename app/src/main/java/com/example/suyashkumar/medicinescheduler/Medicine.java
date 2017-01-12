package com.example.suyashkumar.medicinescheduler;

/**
 * Created by SUYASH KUMAR on 1/10/2017.
 */

public class Medicine {
    private String name;
    private String start_date;
    private TimeOfDay timeOfDay;
    private int frequency;
    private String directions;

    public Medicine(String name, String start_date, TimeOfDay timeOfDay, int frequency, String directions) {
        this.name = name;
        this.start_date = start_date;
        this.timeOfDay = timeOfDay;
        this.frequency = frequency;
        this.directions = directions;
    }

    public Medicine(String name, String directions) {
        this.name = name;
        this.directions = directions;
    }

    public String getName() {
        return name;
    }

    public String getStart_date() {
        return start_date;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getDirections() {
        return directions;
    }
}
