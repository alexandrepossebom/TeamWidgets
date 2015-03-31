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
import org.jsoup.select.Elements;

public class ParserCBF {

	public static void parse(final TimesEnum t, final Team team) throws IOException {
		Utils.log("Parsing CBF ... ");
//		Document doc = Jsoup.parse(new File("/tmp/cbf.html"), null);
		final Document doc = Jsoup.connect(t.getUrlCbf()).get();

		final Elements elsdata = doc.select("div.item.tabela-jogos");

		for (final Element element : elsdata) {
			final String date = element.select("h4.blue.blue2").select("b").text().replaceAll(" de ", " ").replaceFirst("^.*,", "").trim();
			String time = element.select("div.col-md-1.full-game-time.cell-center-middle").text();

			String team1 = element.select("div.col-md-4.fittext.game-team-1.cell-center-middle").text();
			String team2 = element.select("div.col-md-4.fittext.game-team-2.cell-center-middle").text();

			final String result = element.select("div.col-md-2.game-score.cell-center-middle").text();

			team1 = Utils.fixTeamName(team1);
			team2 = Utils.fixTeamName(team2);

			// Stadium
			final Elements elementsStadium = element.select("div.col-md-3.full-game-location-changes.cell-center-middle").select("span");
			elementsStadium.select("strong").remove();
			String stadium = elementsStadium.text().replaceFirst(" - ", ", ").replaceFirst(" - [A-Z]{2}$", "").trim();
			
			stadium = Utils.fixStadiumName(stadium);

			final String transmission = element.select("div.full-game-transmission").text();

			Date dt = null;
			try {
				dt = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("pt", "BR")).parse(date + " " + time);
			} catch (final ParseException e) {
				e.printStackTrace();
			}

			final Calendar calDate = Calendar.getInstance();
			calDate.setTime(dt);

			final String campeonato = t.getCampeonato();
			
			time = Utils.fixTime(time, calDate);
			
			boolean inHome = false;

			String adversario = team1;
			if (team1.equals(t.getNome())) {
				adversario = team2;
				inHome = true;
			}

			final Match match = new Match();
			
			match.setHome(inHome);
			match.setResult(result);
			match.setHomeTeam(t.getNome());
			match.setLeague(campeonato);
			match.setPlace(stadium);
			match.setVisitingTeam(adversario);
			match.setTransmission(transmission);
			match.setTimestamp(calDate.getTimeInMillis());
			match.setSource("c");
			team.getMatches().add(match);
		}

		Utils.logln("Done");
	}

}
