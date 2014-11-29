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
import android.support.v4.app.NotificationCompat;

import com.possebom.teamswidgets.MainActivity;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.dao.DAO;
import com.possebom.teamswidgets.model.Match;
import com.possebom.teamswidgets.model.Team;

import hugo.weaving.DebugLog;
import timber.log.Timber;

public class NotificationService extends IntentService {
    private static final String ACTION_NOTIFY = "com.possebom.teamswidgets.service.action.NOTIFY";
    private static final String EXTRA_TEAM = "com.possebom.teamswidgets.service.extra.TEAM";
    private static int notification_index = 0;

    public NotificationService() {
        super("NotificationService");
    }

    @DebugLog
    public static void scheduleNotification(final Context context, final Team team, final Match match) {
        Timber.d("scheduleNotification");

        final SharedPreferences prefs = context.getSharedPreferences("PREF", 0);
        long offset = prefs.getLong("OFFSET", 60 * 60 * 1000);

        final PendingIntent pendingIntent = getPendingIntent(context, team);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        final long alarmTime = match.getTimestamp() - offset;
        if (alarmTime > System.currentTimeMillis()) {
            Timber.d("scheduleNotification : scheduled!");
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }

    private static PendingIntent getPendingIntent(final Context context, final Team team) {
        final Intent intent = new Intent();
        intent.setClass(context, NotificationService.class);
        intent.setAction(ACTION_NOTIFY);
        intent.putExtra(EXTRA_TEAM, team.getId());

        return PendingIntent.getService(context, team.getId(), intent, 0);
    }

    public static void cancelAlarmsByTeam(final Context context, final Team team) {
        if (team != null) {
            final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(getPendingIntent(context, team));
        }
    }

    public static void cancelAlarmsByTeamId(final Context context, final int teamId) {
        cancelAlarmsByTeam(context, DAO.INSTANCE.getTeamById(teamId));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFY.equals(action)) {
                final int teamId = intent.getIntExtra(EXTRA_TEAM, 0);
                handleActionNotify(teamId);
            }
        }
    }

    private void handleActionNotify(final int id) {
        Timber.d("Notification time !!!");

        final Context context = getApplicationContext();

        final Team team = TWController.INSTANCE.getDao().getTeamById(id);
        if (team == null) {
            return;
        }
        final Match match = team.getNextMatch();

        if (match == null) {
            return;
        }

        final long[] vibrate = {0, 500, 200, 500, 200, 500};
        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        final String title = getString(R.string.notification_alert, match.getHomeTeam(), match.getVisitingTeam());

        final Intent mIntent = new Intent(context, MainActivity.class);
        final PendingIntent pIntent = PendingIntent.getActivity(context, 0, mIntent, 0);

        final Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(match.getDateFormatted())
                .setVibrate(vibrate)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_stat_icon_ball)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();

        notificationManager.notify(notification_index, notification);
        notification_index++;
    }

}
