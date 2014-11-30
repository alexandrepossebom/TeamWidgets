package com.possebom.teamswidgets.util;

import android.content.Context;

import com.possebom.teamswidgets.R;

/**
 * Created by alexandre on 30/11/14.
 */
public final class RelativeTime {


    public static String getTime(final Context context, final long millis) {
        return time(context, millis - System.currentTimeMillis());
    }

    private static String time(final Context context, long distanceMillis) {
        final String prefix;
        final String suffix;

        if (distanceMillis < 0) {
            distanceMillis = Math.abs(distanceMillis);
            prefix = "";
            suffix = context.getString(R.string.relativetime_ago);
        } else {
            prefix = context.getString(R.string.relativetime_prefix_togo);
            suffix = "";
        }

        final double seconds = distanceMillis / 1000;
        final double minutes = seconds / 60;
        final double hours = minutes / 60;
        final double days = hours / 24;
        final double years = days / 365;

        final String time;
        if (seconds < 45)
            time = context.getString(R.string.relativetime_seconds);
        else if (seconds < 90)
            time = context.getString(R.string.relativetime_minute);
        else if (minutes < 45)
            time = context.getString(R.string.relativetime_minutes, Math.round(minutes));
        else if (minutes < 90)
            time = context.getString(R.string.relativetime_hour);
        else if (hours < 24)
            time = context.getString(R.string.relativetime_hours, Math.round(hours));
        else if (hours < 48)
            time = context.getString(R.string.relativetime_day);
        else if (days < 30)
            time = context.getString(R.string.relativetime_days, Math.round(days));
        else if (days < 60)
            time = context.getString(R.string.relativetime_month);
        else if (days < 365)
            time = context.getString(R.string.relativetime_months, Math.round(days / 30));
        else if (years < 2)
            time = context.getString(R.string.relativetime_year);
        else
            time = context.getString(R.string.relativetime_years, Math.round(years));

        return join(prefix, time, suffix);
    }

    private static String join(final String prefix, final String time, final String suffix) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (prefix != null && prefix.length() > 0) {
            stringBuilder.append(prefix).append(' ');
        }
        stringBuilder.append(time);
        if (suffix != null && suffix.length() > 0) {
            stringBuilder.append(' ').append(suffix);
        }
        return stringBuilder.toString();
    }


}