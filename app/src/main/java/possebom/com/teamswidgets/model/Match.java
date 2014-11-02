package possebom.com.teamswidgets.model;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;

import java.util.Calendar;

import possebom.com.teamswidgets.R;

/**
 * Created by alexandre on 01/11/14.
 */
public class Match {
    private long timestamp;
    private Boolean home;
    private String transmission;
    private String opponent;
    private String league;
    private String place;
    private Calendar date = Calendar.getInstance();

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
        date.setTimeInMillis(timestamp);
    }

    public String getDateFormatted() {
        String strDate = DateFormat.format("dd/MM (E) kk:mm", date).toString();
        if (opponent.isEmpty()) {
            strDate = "";
        }
        return strDate.replaceFirst(" 00:00","");
    }

    public Boolean getHome() {
        return home;
    }

    public void setHome(final Boolean home) {
        this.home = home;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(final String transmission) {
        this.transmission = transmission;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(final String opponent) {
        this.opponent = opponent;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(final String league) {
        this.league = league;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(final String place) {
        this.place = place;
    }

    public String getTimeRemaining(final Context context) {
        final Resources res = context.getResources();
        final String and = context.getString(R.string.and);

        long diffInSeconds = (timestamp - System.currentTimeMillis()) / 1000;

        if (diffInSeconds < 0){
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
}
