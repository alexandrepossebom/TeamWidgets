package possebom.com.teamswidgets.controller;

import com.squareup.otto.Bus;

import possebom.com.teamswidgets.dao.DAO;
import possebom.com.teamswidgets.model.Team;

/**
 * Created by alexandre on 01/11/14.
 */
public enum TWController {
    INSTANCE;
    private final Bus bus = new Bus();
    private final DAO dao = new DAO();
    private Team defaultTeam;

    private TWController() {

    }

    public Bus getBus() {
        return bus;
    }

    public DAO getDao() {
        return dao;
    }


    public Team getDefaultTeam() {
        return defaultTeam;
    }

    public void setDefaultTeam(final Team defaultTeam) {
        this.defaultTeam = defaultTeam;
    }
}
