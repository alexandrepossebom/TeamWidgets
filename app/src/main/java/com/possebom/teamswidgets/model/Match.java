package com.possebom.teamswidgets.model;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.possebom.teamswidgets.R;

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

    public long getTimestamp() {
        return timestamp;
    }

    public String getDateFormatted() {
        if(date == null) {
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

    public String getTimeRemaining(final Context context) {
        final Resources res = context.getResources();
        final String and = context.getString(R.string.and);

        long diffInSeconds = (timestamp - System.currentTimeMillis()) / 1000;

        if (diffInSeconds < 0) {
            return context.getString(R.string.played);
        }

        final int minutes = (int) ((diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds);
        final int hours = (int) ((diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds);
        final int days = (int) (diffInSeconds / 24);

        final String text;
        if (days == 0) {
            if (hours == 0) {
                text = res.getQuantityString(R.plurals.minutes, minutes, minutes);
            } else if (minutes == 0) {
                text = res.getQuantityString(R.plurals.hours, hours, hours);
            } else {
                text = res.getQuantityString(R.plurals.hours, hours, hours) + and + res.getQuantityString(R.plurals.minutes, minutes, minutes);
            }
        } else {
            if (hours == 0 && minutes == 0) {
                text = res.getQuantityString(R.plurals.days, days, days);
            } else if (hours == 0) {
                text = res.getQuantityString(R.plurals.days, days, days) + and + res.getQuantityString(R.plurals.minutes, minutes, minutes);
            } else {
                text = res.getQuantityString(R.plurals.days, days, days) + and + res.getQuantityString(R.plurals.hours, hours, hours);
            }
        }
        return text;
    }

    @Override
    public int compareTo(Match another) {
        if (this.timestamp < another.getTimestamp()) {
            return -1;
        }
        if (this.timestamp > another.getTimestamp()) {
            return 1;
        }
        return 0;
    }
}
