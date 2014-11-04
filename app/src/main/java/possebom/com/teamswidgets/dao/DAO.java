package possebom.com.teamswidgets.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import possebom.com.teamswidgets.controller.TWController;
import possebom.com.teamswidgets.event.UpdateEvent;
import possebom.com.teamswidgets.model.Team;
import timber.log.Timber;

/**
 * Created by alexandre on 01/11/14.
 */
public class DAO {

    private static final String PREFS_NAME = "TeamPref";
    private static final String PREFS_KEY_JSON = "json";
    private static final String PREFS_KEY_LASTUPDATE = "lastUpdate";


    private List<Team> teamList = new ArrayList<Team>();



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

    public void update(final Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastUpdate = sharedPreferences.getLong(PREFS_KEY_LASTUPDATE,0);
        long now = System.currentTimeMillis();
        long diff = now - lastUpdate;

        final Type collectionType = new TypeToken<Collection<Team>>() { }.getType();

        if(diff < 60 * 1000 ){
            Timber.d("Dont need update diff is : "+ diff/1000);
            final String json = sharedPreferences.getString(PREFS_KEY_JSON,"");
            teamList = new Gson().fromJson(json, collectionType);
            Timber.i("Team list size: " + teamList.size());
            TWController.INSTANCE.getBus().post(new UpdateEvent(null));
            return;
        }

        Ion.with(context)
                .load("http://possebom.com/widgets/teams.json")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String message = null;
                        if (e != null) {
                            Timber.e(e.getMessage());
                            message = e.getMessage();
                        } else {
                            final String json = result.getAsJsonArray("Teams").toString();
                            teamList = new Gson().fromJson(json, collectionType);
                            Timber.i("Team list size: " + teamList.size());

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(PREFS_KEY_JSON, json);
                            editor.putLong(PREFS_KEY_LASTUPDATE, System.currentTimeMillis());
                            editor.commit();
                        }
                        TWController.INSTANCE.getBus().post(new UpdateEvent(message));
                    }
                });
    }

    public List<Team> getTeamList() {
        return teamList;
    }
}
