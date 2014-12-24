package com.possebom.teamswidgets.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.mikpenz.iconics.IconicsDrawable;
import com.mikpenz.iconics.typeface.FontAwesome;
import com.possebom.teamswidgets.BaseApplication;
import com.possebom.teamswidgets.MainActivity;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.model.Match;
import com.possebom.teamswidgets.model.Team;
import com.possebom.teamswidgets.service.NotificationService;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetLargeConfigureActivity WidgetLargeConfigureActivity}
 */
public class WidgetLarge extends AppWidgetProvider {

    private static final String EMPTY = "";

    @DebugLog
    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        Timber.d("updateAppWidget widget id : " + appWidgetId);
        final int teamId = WidgetLargeConfigureActivity.loadTitlePref(context, appWidgetId);

        final Team team = TWController.INSTANCE.getDao().getTeamById(teamId);
        if (team == null) {
            Timber.d("Team is null id is : " + teamId);
            return;
        }

        Timber.d("updateAppWidget team : " + team.getName());

        final Match match = team.getNextMatch();

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_large);

        if (match == null) {
            views.setTextViewText(R.id.textViewOpponent, context.getString(R.string.no_data));
            views.setTextViewText(R.id.textViewLeague, EMPTY);
            views.setTextViewText(R.id.textViewPlace, EMPTY);
            views.setTextViewText(R.id.textViewDate, EMPTY);
            views.setImageViewBitmap(R.id.imageViewHomeOut, null);
        } else {
            NotificationService.scheduleNotification(context, team, match);

            views.setTextViewText(R.id.textViewOpponent, match.getVisitingTeam());
            views.setTextViewText(R.id.textViewLeague, match.getLeague());
            views.setTextViewText(R.id.textViewPlace, match.getPlace());
            views.setTextViewText(R.id.textViewDate, match.getDateFormatted());

            FontAwesome.Icon icon = FontAwesome.Icon.faw_plane;
            if (match.getHome()) {
                icon = FontAwesome.Icon.faw_home;
            }

            final IconicsDrawable drawable = new IconicsDrawable(context, icon).color(Color.WHITE).sizeRes(R.dimen.widgetImageSizeLarge);
            views.setImageViewBitmap(R.id.imageViewHomeOut, drawable.toBitmap());
        }

        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("teamId", team.getId());
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, ++WidgetSmall.pendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        views.setOnClickPendingIntent(R.id.layout_widget_large, pendingIntent);

        BaseApplication.getPicasso()
                .load(team.getImgUrl())
                .resizeDimen(R.dimen.widgetImageSizeLarge, R.dimen.widgetImageSizeLarge)
                .centerInside()
                .into(views, R.id.imageViewTeam, new int[]{appWidgetId});

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            final int teamId = WidgetLargeConfigureActivity.loadTitlePref(context, appWidgetId);
            NotificationService.cancelAlarmsByTeamId(context, teamId);
            WidgetLargeConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }
}


