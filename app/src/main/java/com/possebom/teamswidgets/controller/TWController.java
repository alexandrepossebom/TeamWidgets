package com.possebom.teamswidgets.controller;

import com.squareup.otto.Bus;

import com.possebom.teamswidgets.dao.DAO;

/**
 * Created by alexandre on 01/11/14.
 */
public enum TWController {
    INSTANCE;
    private final Bus bus = new Bus();
    private final DAO dao = DAO.INSTANCE;
    private int defaultTeamId =0;

    private TWController() {

    }

    public Bus getBus() {
        return bus;
    }

    public DAO getDao() {
        return dao;
    }


    public int getDefaultTeamId() {
        if (defaultTeamId == 0) {
            defaultTeamId = dao.getDefaultTeamName();
        }
        return defaultTeamId;
    }

    public void setDefaultTeam(final int teamId) {
        if (this.defaultTeamId == 0 || teamId != defaultTeamId) {
            dao.setDefaultTeamName(defaultTeamId);
            this.defaultTeamId = defaultTeamId;
        }
    }
}
