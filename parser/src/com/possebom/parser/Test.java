package com.possebom.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test {

	public static String getText(String url) throws Exception {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		return response.toString();
	}

	public static String run(TimesEnum team) {
		int uol = 0;
		int cbf = 0;
		int validGames = 0;
		int games = 0;
		String result = "";
		try {
			String source = getText("http://possebom.com/widgets/" + team.getNomeUrl() + ".json");

			JSONObject jsonObject = new JSONObject(source);

			JSONArray ja = jsonObject.getJSONArray("jogos");
			games = ja.length();

			for (int i = 0; i < ja.length(); ++i) {
				JSONObject json = ja.getJSONObject(i);
				long timestamp = json.getLong("timestamp");
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis(timestamp);
				if (Calendar.getInstance().compareTo(date) != -1) {
					continue;
				}
				validGames++;
				
				String fonte = json.getString("fonte");
				if(fonte.contains("c")){
					cbf++;
				}else{
					uol++;
				}
				
			}
			
			result = String.format("Team : %15s, Games : %02d, valid : %02d, cbf: %02d, uol: %02d",team.getNomeUrl(), games, validGames, cbf, uol);
			if(games != validGames){
				result = result + " ERROR";
			}
			
			if(games == 0){
				result = result + " ERROR";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getLocalizedMessage();
		}		
		return result;
	}

}
