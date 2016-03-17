package com.mobica.womi.pushnots.model;

import java.util.Calendar;

/**
 * Created by womi on 2016-03-17.
 */
public class DelayModel {
    private Calendar startTime;
    private boolean isRepeatable;
    private int repetitionIntervalInMinutes;

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setIsRepeatable(boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public int getRepetitionIntervalInMinutes() {
        return repetitionIntervalInMinutes;
    }

    public void setRepetitionIntervalInMinutes(int repetitionIntervalInMinutes) {
        this.repetitionIntervalInMinutes = repetitionIntervalInMinutes;
    }

    public Calendar getStartTime() {

        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }
}
