package com.possebom.teamswidgets.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by alexandre on 01/11/14.
 */
public class Match implements Comparable<Match> {
    private Calendar date;
    private long timestamp;
    private Boolean home;
    private String transmission;
    private String league;
    private String place;
    private String homeTeam;
    private String visitingTeam;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDateFormatted() {
        if (date == null) {
            date = GregorianCalendar.getInstance();
            date.setTimeInMillis(timestamp);
        }
        String strDate = DateFormat.format("dd/MM (E) kk:mm", date).toString();
        if (TextUtils.isEmpty(visitingTeam)) {
            strDate = "";
        }
        return strDate.replaceFirst(" 00:00", "");
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public Boolean getHome() {
        return home;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getLeague() {
        return league;
    }

    public String getPlace() {
        return place;
    }

    public boolean isAlreadyPlayed() {
        return timestamp <= System.currentTimeMillis();
    }

    @Override
    public int compareTo(@NonNull Match another) {
        if (this.timestamp < another.getTimestamp()) {
            return -1;
        }
        if (this.timestamp > another.getTimestamp()) {
            return 1;
        }
        return 0;
    }
}
