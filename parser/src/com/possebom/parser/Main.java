package com.possebom.parser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Main {

	public static void main(final String[] args) {

		if (args.length == 0) {
			parse();
		}

		// test();
	}

	private static void test() {

		final StringBuffer stringBuffer = new StringBuffer();

		for (final TimesEnum team : TimesEnum.values()) {
			stringBuffer.append(Test.run(team));
			stringBuffer.append("\n");
		}

		System.out.println(stringBuffer);

		String subject = "[WIDGETS] Ok";
		if (stringBuffer.toString().contains("ERROR")) {
			subject = "[WIDGETS] ERROR";
		}

		Email.send(subject, stringBuffer.toString());

	}

	private static void parse() {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", new Locale("pt", "BR"));

		final Teams teams = new Teams();
		teams.setUpdated(sdf.format(new Date()));

		for (final TimesEnum time : TimesEnum.values()) {

			final Team team = new Team();
			team.setName(time.getNome());
			team.setImgUrl(time.getImgUrl());
			team.setCompleteName(time.getNomeCompleto());
			team.setId(time.getId());

			Utils.logln("Parsing data for " + time.getNome() + " ...");

			try {
				ParserCBF.parse(time, team);
			} catch (final IOException e) {
				e.printStackTrace();
			}

			try {
				ParserUOL.parse(time, team);
			} catch (final IOException e) {
				e.printStackTrace();
			}

			try {
				ParserUOLResults.parse(time, team);
			} catch (final IOException e) {
				e.printStackTrace();
			}

			Collections.sort(team.getMatches());

			teams.getTeams().add(team);
			Utils.sleep();
		}

		Utils.write(teams);
	}
}
