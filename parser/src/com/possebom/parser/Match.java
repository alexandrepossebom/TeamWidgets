package com.possebom.parser;

import com.google.gson.annotations.Expose;

public class Match implements Comparable<Match> {

	@Expose
	private long timestamp;
	@Expose
	private Boolean home;
	@Expose
	private String transmission;
	@Expose
	private String league;
	@Expose
	private String place;
	@Expose
	private String homeTeam;
	@Expose
	private String visitingTeam;
	@Expose
	private String source;
	@Expose
	private String result;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	public String getResult() {
		return result;
	}

	public void setResult(final String result) {
		this.result = result;
	}

	public Boolean getHome() {
		return home;
	}

	public void setHome(final Boolean home) {
		this.home = home;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(final String transmission) {
		this.transmission = transmission;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(final String league) {
		this.league = league;
	}

	public String getPlace() {
		return place;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(final String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getVisitingTeam() {
		return visitingTeam;
	}

	public void setVisitingTeam(final String visitingTeam) {
		this.visitingTeam = visitingTeam;
	}

	public void setPlace(final String place) {
		this.place = place;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	@Override
	public int compareTo(final Match o) {
		final Long obj1 = new Long(timestamp);
		final Long obj2 = new Long(o.timestamp);
		final int retval = obj1.compareTo(obj2);
		return retval;
	}

	@Override
	public String toString() {
		return String.format("%s  %s - %s - %s %s %s", timestamp, league, place, homeTeam, result, visitingTeam);
	}

}
