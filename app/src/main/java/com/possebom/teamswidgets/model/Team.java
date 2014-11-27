package com.possebom.teamswidgets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandre on 01/11/14.
 */
public class Team {

    private String name;
    private String imgUrl;
    private List<Match> matches = new ArrayList<Match>();
    private Match nextMatch;
    private int id;

     public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(final List<Match> matches) {
        this.matches = matches;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(final String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Match getNextMatch() {
        long now = System.currentTimeMillis();
        if (nextMatch != null) {
            if (nextMatch.getTimestamp() < now) {
                nextMatch = null;
            }
        }

        if (nextMatch == null) {
            for (Match match : matches) {
                if (match.getTimestamp() > now) {
                    nextMatch = match;
                    break;
                }
            }
        }

        return nextMatch;
    }

    public int getFirstPlayedPosition() {
        int position = -1;
        final long now = System.currentTimeMillis();
        for (int i = 0; i < matches.size(); i++) {
            final Match match = matches.get(i);
            if (match.getTimestamp() < now) {
                position = i;
                break;
            }
        }
        return position;
    }

    public int getFirstNotPlayedPosition() {
        int position = -1;
        final long now = System.currentTimeMillis();
        for (int i = 0; i < matches.size(); i++) {
            final Match match = matches.get(i);
            if (match.getTimestamp() > now) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
