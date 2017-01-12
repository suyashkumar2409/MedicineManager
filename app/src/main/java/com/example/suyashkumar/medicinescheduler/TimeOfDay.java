package com.example.suyashkumar.medicinescheduler;

/**
 * Created by SUYASH KUMAR on 1/10/2017.
 */

public class TimeOfDay {
    private boolean morning;
    private boolean afternoon;
    private boolean evening;
    private boolean night;

    public TimeOfDay(boolean morning, boolean afternoon, boolean evening, boolean night) {
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;
    }

    public boolean isNight() {
        return night;
    }

    public boolean isMorning() {
        return morning;
    }

    public boolean isAfternoon() {
        return afternoon;
    }

    public boolean isEvening() {
        return evening;
    }
}
