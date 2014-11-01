package possebom.com.teamswidgets.dao;

import android.content.Context;

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
import possebom.com.teamswidgets.util.Log;

/**
 * Created by alexandre on 01/11/14.
 */
public class DAO {

    private List<Team> teamList = new ArrayList<Team>();

    public Team getTeamByName(final String name){
        Team teamResult = null;

        for (Team team: teamList){
            if(team.getName().equals(name)){
                teamResult = team;
                break;
            }
        }

        return teamResult;
    }

    public void update(Context context) {
        Ion.with(context)
                .load("http://possebom.com/widgets/teams.json")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String message = null;
                        if (e != null) {
                            Log.d(e.getMessage());
                            return;
                        } else {
                            final Gson gson = new Gson();
                            final Type collectionType = new TypeToken<Collection<Team>>() {
                            }.getType();
                            teamList = gson.fromJson(result.getAsJsonArray("Teams").toString(), collectionType);
                            Log.i(result.toString());
                            Log.i("Team list size: " + teamList.size());
                        }
                        TWController.INSTANCE.getBus().post(new UpdateEvent(message));
                    }
                });
    }

    public List<Team> getTeamList() {
        return teamList;
    }
}
