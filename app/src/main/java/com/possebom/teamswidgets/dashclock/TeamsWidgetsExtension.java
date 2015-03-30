package com.possebom.teamswidgets.dashclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;
import com.possebom.teamswidgets.MainActivity;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.model.Match;
import com.possebom.teamswidgets.model.Team;

import timber.log.Timber;

/**
 * Created by alexandre on 15/02/15.
 */
public class TeamsWidgetsExtension extends DashClockExtension {


    public static final String PREF_NAME = "DashClockExtensionTeam";
    private static final String NEWLINE = "\n";

    @Override
    protected void onUpdateData(final int reason) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String teamName = sharedPreferences.getString(PREF_NAME, "");

        final boolean alwaysUpdate = sharedPreferences.getBoolean("DashClockExtensionUpdate",false);
        setUpdateWhenScreenOn(alwaysUpdate);

        final Team team = TWController.INSTANCE.getDao().getTeamByName(teamName);

        if (team == null){
            Timber.e("Dashclock: team is null");
            publishUpdate(new ExtensionData());
            return;
        }

        Timber.e("Dashclock team : " + teamName);

        final Match match = team.getNextMatch();

        if (match == null){
            Timber.e("Dashclock: Match is null");
            publishUpdate(new ExtensionData());
            return;
        }


        final String text = daysBetween(match.getTimestamp());
        final String and = " " + getString(R.string.and) + " ";

        final String expanded = String.format("%s - %s%n%s%n%s", match.getVisitingTeam(), match.getLeague(), match.getPlace(), text.replace(NEWLINE, and));

        // Publish the extension data update.
        publishUpdate(new ExtensionData()
                .visible(true)
                .icon(R.drawable.ic_stat_icon_ball)
                .status(text)
                .expandedTitle(match.getDateFormatted())
                .expandedBody(expanded)
                .clickIntent(new Intent(getApplicationContext(), MainActivity.class)));

    }

    public String daysBetween(final long endDate) {
        long diffInSeconds = (endDate - System.currentTimeMillis()) / 1000;

        final int minutes = (int) ((diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds);
        final int hours = (int) ((diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds);
        final int days = (int) (diffInSeconds / 24);

        final Resources res = getResources();

        final String text;
        if (days == 0) {
            if (hours == 0) {
                text = res.getQuantityString(R.plurals.minutes, minutes, minutes);
            } else if (minutes == 0) {
                text = res.getQuantityString(R.plurals.hours, hours, hours);
            } else {
                text = res.getQuantityString(R.plurals.hours, hours, hours) + NEWLINE + res.getQuantityString(R.plurals.minutes, minutes, minutes);
            }
        } else {
            if (hours == 0 && minutes == 0) {
                text = res.getQuantityString(R.plurals.days, days, days);
            } else if (hours == 0) {
                text = res.getQuantityString(R.plurals.days, days, days) + NEWLINE + res.getQuantityString(R.plurals.minutes, minutes, minutes);
            } else {
                text = res.getQuantityString(R.plurals.days, days, days) + NEWLINE + res.getQuantityString(R.plurals.hours, hours, hours);
            }
        }
        return text;
    }

}
