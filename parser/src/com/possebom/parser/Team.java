package com.possebom.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Team {
	@Expose
	private String name;
	@Expose
	private String imgUrl;
	@Expose
	private String completeName;
	@Expose
	private final List<Match> matches = new ArrayList<Match>();
	@Expose
	private int id;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(final String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(final String completeName) {
		this.completeName = completeName;
	}

}
