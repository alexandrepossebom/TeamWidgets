package com.possebom.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Teams {

	@Expose
	private String updated;

	@Expose
	private final List<Team> teams = new ArrayList<Team>();

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(final String updated) {
		this.updated = updated;
	}

	public List<Team> getTeams() {
		return teams;
	}


}
