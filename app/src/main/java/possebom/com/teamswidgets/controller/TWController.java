package possebom.com.teamswidgets.controller;

import com.squareup.otto.Bus;

import possebom.com.teamswidgets.dao.DAO;

/**
 * Created by alexandre on 01/11/14.
 */
public enum TWController {
    INSTANCE;
    private final Bus bus = new Bus();
    private final DAO dao = new DAO();
    private String defaultTeamName;

    private TWController() {

    }

    public Bus getBus() {
        return bus;
    }

    public DAO getDao() {
        return dao;
    }


    public String getDefaultTeamName() {
        if (defaultTeamName == null) {
            defaultTeamName = dao.getDefaultTeamName();
        }
        return defaultTeamName;
    }

    public void setDefaultTeam(final String defaultTeamName) {
        if (this.defaultTeamName == null || (defaultTeamName != null && !defaultTeamName.equals(this.defaultTeamName))) {
            dao.setDefaultTeamName(defaultTeamName);
            this.defaultTeamName = defaultTeamName;
        }
    }
}
