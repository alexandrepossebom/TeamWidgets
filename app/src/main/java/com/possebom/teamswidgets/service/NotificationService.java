package com.possebom.teamswidgets.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;

import com.possebom.teamswidgets.MainActivity;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.model.Match;
import com.possebom.teamswidgets.model.Team;
import com.squareup.picasso.Picasso;


import timber.log.Timber;

public class NotificationService extends IntentService {
    private static final String ACTION_NOTIFY = "com.possebom.teamswidgets.service.action.NOTIFY";
    private static final String EXTRA_TEAM = "com.possebom.teamswidgets.service.extra.TEAM";
    private static int notification_index = 0;

    public NotificationService() {
        super("NotificationService");
    }

    public static void scheduleNotification(final Context context, final Team team, final Match match) {
        Timber.d("scheduleNotification");

        final SharedPreferences prefs = context.getSharedPreferences("PREF", 0);
        long offset = prefs.getLong("OFFSET", 60 * 60 * 1000);

        final Intent intent = new Intent();
        intent.setClass(context, NotificationService.class);
        intent.setAction(ACTION_NOTIFY);
        intent.putExtra(EXTRA_TEAM, team.getName());

        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        final long alarmTime = match.getTimestamp() - offset;
        if (alarmTime > System.currentTimeMillis()) {
            Timber.d("scheduleNotification : scheduled!");
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFY.equals(action)) {
                final String teamName = intent.getStringExtra(EXTRA_TEAM);
                handleActionNotify(teamName);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionNotify(String teamName) {
        Timber.d("Notification time !!!");

        final Context context = getApplicationContext();

        final Team team = TWController.INSTANCE.getDao().getTeamByName(teamName);
        if (team == null) {
            return;
        }
        final Match match = team.getNextMatch();

        if (match == null) {
            return;
        }

        final long[] vibrate = {0, 500, 200, 500, 200, 500};
        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        final String title = getString(R.string.notification_alert, match.getOpponent());

        final Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
        final PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, mIntent, 0);

        final Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(match.getDateFormatted())
                .setVibrate(vibrate)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.generic_team)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();
        notificationManager.notify(notification_index, notification);
        notification_index++;
    }

}
