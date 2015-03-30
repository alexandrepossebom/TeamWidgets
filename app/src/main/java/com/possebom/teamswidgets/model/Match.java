package com.possebom.teamswidgets.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by alexandre on 01/11/14.
 */
public class Match implements Comparable<Match> {
    private DateTime date;
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
        if (date == null) fixDate();
        return date.getMillis();
    }

    private void fixDate() {
        synchronized (this) {
            final DateTime dt = new DateTime(timestamp, DateTimeZone.forID("Brazil/East"));
            date = dt.withZone(DateTimeZone.getDefault());
        }
    }

    public String getDateFormatted() {
        if (date == null) fixDate();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM (E) kk:mm").withLocale(Locale.getDefault());
        String strDate = date.toString(fmt);
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
        return date.isAfterNow();
    }

    @Override
    public int compareTo(@NonNull Match another) {
        if (this.getTimestamp() < another.getTimestamp()) {
            return -1;
        }
        if (this.getTimestamp() > another.getTimestamp()) {
            return 1;
        }
        return 0;
    }
}
