package possebom.com.teamswidgets.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import possebom.com.teamswidgets.BaseApplication;
import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.event.UpdateEvent;
import possebom.com.teamswidgets.model.Team;
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

    private List<Team> teamList = new ArrayList<Team>();
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

    public String getDefaultTeamName() {
        return getSharedPreferences().getString("defaultTeamName", null);
    }

    public void setDefaultTeamName(final String teamName) {
        getSharedPreferences().edit().putString("defaultTeamName", teamName).apply();
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
        if (!isNeedUpdate()) {
            return;
        }
        
        final Context context = BaseApplication.getContext();

        Ion.with(context)
                .load("http://possebom.com/widgets/teams.json")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Timber.e("Error :" + e.getMessage());
                            TWController.INSTANCE.getBus().post(new UpdateEvent("error"));
                        } else {
                            final String json = result.getAsJsonArray("Teams").toString();
                            SharedPreferences.Editor editor = getSharedPreferences().edit();
                            editor.putString(PREFS_KEY_JSON, json);
                            editor.putLong(PREFS_KEY_LASTUPDATE, System.currentTimeMillis());
                            editor.apply();
                            updateResults(json, true);
                        }
                    }
                });
    }

    private void updateResults(final String json, boolean sendBus) {
        final Type collectionType = new TypeToken<Collection<Team>>() {
        }.getType();
        teamList = new Gson().fromJson(json, collectionType);
        Timber.i("Team list size: " + teamList.size());
        if (sendBus) {
            TWController.INSTANCE.getBus().post(new UpdateEvent(null));
        }
    }

    public List<Team> getTeamList() {
        return teamList;
    }
}
