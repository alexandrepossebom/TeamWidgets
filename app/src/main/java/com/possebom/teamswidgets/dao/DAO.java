package com.possebom.teamswidgets.dao;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.possebom.teamswidgets.BaseApplication;
import com.possebom.teamswidgets.controller.TWController;
import com.possebom.teamswidgets.event.ErrorOnUpdateEvent;
import com.possebom.teamswidgets.event.UpdateEvent;
import com.possebom.teamswidgets.model.Team;
import com.possebom.teamswidgets.widgets.WidgetSmall;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by alexandre on 01/11/14.
 */
public enum DAO {
    INSTANCE;
    private static final long INTERVAL = 24 * 60 * 60 * 1000;
    private static final String PREFS_NAME = "TeamPref";
    private static final String PREFS_KEY_JSON = "json";
    private static final String PREFS_KEY_LASTUPDATE = "lastUpdate";
    private final Map<String, String> mapUrls = new HashMap<>();
    private List<Team> teamList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private DAO() {
        String json = getSharedPreferences().getString(PREFS_KEY_JSON, null);
        if (json != null) {
            updateResults(json, false);
        }
    }

    private SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            final Context context = BaseApplication.getContext();
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public int getDefaultTeamName() {
        return getSharedPreferences().getInt("defaultTeamId", 0);
    }

    public void setDefaultTeamName(final int teamId) {
        getSharedPreferences().edit().putInt("defaultTeamId", teamId).apply();
    }

    public Team getTeamByName(final String name) {
        Team teamResult = null;

        for (Team team : teamList) {
            if (team.getName().equals(name)) {
                teamResult = team;
                break;
            }
        }

        return teamResult;
    }

    @DebugLog
    public Team getTeamById(final int id) {
        Team teamResult = null;

        for (Team team : teamList) {
            if (team.getId() == id) {
                teamResult = team;
                break;
            }
        }

        return teamResult;
    }

    public String getTeamLogoUrlByName(final String teamName) {
        return mapUrls.get(teamName);
    }

    @DebugLog
    public boolean isNeedUpdate() {
        long lastUpdate = getSharedPreferences().getLong(PREFS_KEY_LASTUPDATE, 0);
        long now = System.currentTimeMillis();
        long diff = now - lastUpdate;

        final CharSequence strDiff = DateUtils.getRelativeTimeSpanString(lastUpdate);

        if (diff < INTERVAL) {
            Timber.d("Don't need update diff is : " + strDiff);
            final String json = sharedPreferences.getString(PREFS_KEY_JSON, "");
            updateResults(json, true);
            return false;
        }

        Timber.d("Updating diff is : " + strDiff);
        return true;
    }

    public void update() {
        update(false);
    }

    public void update(boolean force) {
        if (!force) {
            if (!isNeedUpdate()) {
                return;
            }
        }

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://possebom.com/widgets/teams.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Timber.d("debug :" + response.toString());
                try {
                    final String json = response.getJSONArray("teams").toString();
                    SharedPreferences.Editor editor = getSharedPreferences().edit();
                    editor.putString(PREFS_KEY_JSON, json);
                    editor.putLong(PREFS_KEY_LASTUPDATE, System.currentTimeMillis());
                    editor.apply();
                    updateResults(json, true);
                    updateWidgets(BaseApplication.getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                    TWController.INSTANCE.getBus().post(new ErrorOnUpdateEvent(e.getCause()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Timber.e("Error :" + statusCode);
                TWController.INSTANCE.getBus().post(new ErrorOnUpdateEvent(throwable));
            }
        });

    }

    public void updateWidgets(final Context context) {
        updateWidget(context, WidgetSmall.class);
        //TODO
//        updateWidget(context, WidgetProviderLarge.class);
    }

    private void updateWidget(final Context context, final Class widget) {
        final int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, widget));
        if (ids != null && ids.length > 0) {
            final Intent intent = new Intent(context, widget);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }
    }


    private void updateResults(final String json, boolean sendBus) {
        final Type collectionType = new TypeToken<Collection<Team>>() {
        }.getType();
        teamList = new Gson().fromJson(json, collectionType);

        for (Team team : teamList) {
            mapUrls.put(team.getName(), team.getImgUrl());
        }

        Timber.i("Team list size: " + teamList.size());
        if (sendBus) {
            TWController.INSTANCE.getBus().post(new UpdateEvent());
        }
    }

    public List<Team> getTeamList() {
        return teamList;
    }
}
