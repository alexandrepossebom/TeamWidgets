package com.possebom.parser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class ParserUOLResults {

	public static void parse(final TimesEnum time, final Team team) throws IOException {
		final boolean brasileirao = team.getMatches().isEmpty();

		Utils.log("Parsing UOL Results... ");
		// final Document doc = Jsoup.parse(new File("/tmp/index.html"), null);
		final Document doc = Jsoup.connect(time.getUrlUolResults()).get();

		final Elements els = doc.select("tr.vevent");

		for (final Element el : els) {
			final Element datetime = el.select("time.dtstart").first();
			Date dt;
			try {
				dt = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("pt", "BR")).parse(datetime.attr("datetime"));
			} catch (final ParseException e2) {
				try {
					dt = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR")).parse(datetime.attr("datetime"));
				} catch (final ParseException e) {
					dt = new Date();
				}				
			}

			final Calendar date = Calendar.getInstance();
			date.setTime(dt);


			final Element league = el.select("td.league").first();
			final Element location = el.select("td.location").first();
			String stadium = ((TextNode) location.childNode(0)).text().trim();
			final String city = ((TextNode) location.childNode(2)).text().trim();

			stadium = Utils.fixStadiumName(stadium);
			
			final String local;
			if (city.isEmpty() || city.trim().length() < 2) {
				local = stadium;
			} else {
				local = String.format("%s, %s", stadium, city);
			}

			final Element team_a = el.select("abbr").first();
			final Element team_b = el.select("abbr").last();

			final String team_a_goals = el.select("span.gols ").first().text();
			final String team_b_goals = el.select("span.gols ").last().text();

			final String result = String.format("%s x %s", team_a_goals, team_b_goals);

			final String timea = team_a.attr("title");
			final String timeb = team_b.attr("title");
			final String campeonato = league.text().trim();
			
			if(timea.equals(timeb)){
				continue;
			}

			if (brasileirao == false) {
				if (campeonato.contains("Brasileirão") || campeonato.contains("Série B")) {
					continue;
				}
			}

			boolean inHome = false;
			final String adversario;
			if (timea.equals(time.getNome())) {
				adversario = timeb;
				inHome = true;
			} else {
				adversario = timea;
			}
			

			final Match match = new Match();

			match.setHome(inHome);
			match.setResult(result);
			match.setHomeTeam(time.getNome());
			match.setLeague(campeonato);
			match.setPlace(local);
			match.setVisitingTeam(adversario);
			match.setTransmission("");
			match.setTimestamp(date.getTimeInMillis());
			match.setSource("c");

			team.getMatches().add(match);
		}

		Utils.logln("Done");
	}

}
