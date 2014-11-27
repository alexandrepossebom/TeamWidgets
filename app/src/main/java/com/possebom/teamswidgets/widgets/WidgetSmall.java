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
import com.possebom.teamswidgets.MainActivity;
import com.possebom.teamswidgets.R;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.model.Match;
import com.possebom.teamswidgets.model.Team;
import com.possebom.teamswidgets.service.NotificationService;
import com.squareup.picasso.Picasso;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetSmallConfigureActivity WidgetSmallConfigureActivity}
 */
public class WidgetSmall extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        final int teamId = WidgetSmallConfigureActivity.loadTitlePref(context, appWidgetId);

        final Team team = TWController.INSTANCE.getDao().getTeamById(teamId);
        if (team == null) {
            return;
        }
        final Match match = team.getNextMatch();

        if (match == null) {
            return;
        }

        NotificationService.scheduleNotification(context, team, match);

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_small);
        views.setTextViewText(R.id.textViewOpponent, match.getVisitingTeam());
        views.setTextViewText(R.id.textViewLeague, match.getLeague());
        views.setTextViewText(R.id.textViewPlace, match.getPlace());
        views.setTextViewText(R.id.textViewDate, match.getDateFormatted());

        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("teamId",team.getId());
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, team.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        views.setOnClickPendingIntent(R.id.layout_widget_small, pendingIntent);

        Picasso.with(context)
                .load(team.getImgUrl())
                .resizeDimen(R.dimen.widgetImageSize, R.dimen.widgetImageSize)
                .centerInside()
                .into(views, R.id.imageViewTeam, new int[]{appWidgetId});

        FontAwesome.Icon icon = FontAwesome.Icon.faw_plane;

        if (match.getHome()) {
            icon = FontAwesome.Icon.faw_home;
        }

        final IconicsDrawable drawable = new IconicsDrawable(context, icon).color(Color.WHITE).sizeRes(R.dimen.widgetImageSize);
        views.setImageViewBitmap(R.id.imageViewHomeOut, drawable.toBitmap());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            WidgetSmallConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


