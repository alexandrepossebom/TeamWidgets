package com.possebom.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

	public static String fixTime(String time, final Calendar cal) {
		time = time.replaceAll(":", "h");
		if (time.equals("00h00") || time.equals("01h00")) {
			final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
			case Calendar.SUNDAY:
				time = "16h00";
				break;
			case Calendar.SATURDAY:
				time = "18h30";
				break;
			case Calendar.WEDNESDAY:
				time = "22h00";
				break;
			default:
				break;
			}
		}
		return time;
	}

	public static String fixTeamName(String team) {
		team = team.replaceFirst("BOTAFOGO", "Botafogo");
		team = team.replaceFirst("Bota-fogo", "Botafogo");
		team = team.replaceFirst("Vasco da Gama", "Vasco");
		team = team.replaceFirst("ATLETICO", "Atlético");
		team = team.replaceFirst("Atletico", "Atlético");
		team = team.replaceFirst("GOIÁS", "Goiás");
		team = team.replaceFirst("FIGUEIRENSE", "Figueirense");
		team = team.replaceFirst("SAMPAIO CORREA", "Sampaio Correa");
		team = team.replaceFirst("CORITIBA", "Coritiba");
		team = team.replaceFirst("PONTE PRETA", "Ponte Preta");
		team = team.replaceFirst("SANTOS", "Santos");
		team = team.replaceFirst("CENE", "Cene");
		team = team.replaceFirst("CRICIÚMA", "Criciúma");
		team = team.replaceFirst("MACAÉ", "Macaé");

		if (team.contains("Atlético") || team.contains("América")) {
			team = team.replaceFirst(" - ", "-");
		} else {
			team = team.replaceFirst(" - [A-Z]{2}$", "").trim();
		}

		return team;
	}

	public static String fixStadiumName(String stadium) {
		stadium = stadium.replaceFirst("Itaquerão", "Arena Corinthians");
		stadium = stadium.replaceFirst("Arena Grêmio", "Arena do Grêmio");
		return stadium;
	}

	public static void sleep() {
		log("Sleeping ... ");
		try {
			Thread.sleep(5000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		logln("Done");
	}

	public static void log(final String text) {
		log(text, false);
	}

	public static void logln(final String text) {
		log(text, true);
	}

	private static void log(final String text, final boolean newline) {
		if (newline)
			System.out.println(text);
		else
			System.out.print(text);
	}

	public static void write(final Teams teams) {

		// final File file = new File("/tmp/");
		final File file = new File("/var/www/widgets/");
		if (file.isDirectory()) {
			final Gson gson = new Gson();
			PrintWriter outJson;
			try {
				outJson = new PrintWriter("/var/www/widgets/teams.json");
				// outJson = new PrintWriter("/tmp/teams.json");
				outJson.println(gson.toJson(teams));
				outJson.close();
			} catch (final FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} else {
			final Gson gson = new GsonBuilder().setPrettyPrinting().create();
			logln("JSON: " + gson.toJson(teams).toString());
		}

	}

}
