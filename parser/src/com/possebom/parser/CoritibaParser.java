package com.possebom.parser;


public class CoritibaParser {

	// private static void populateJson(final TimesEnum time, final Document
	// doc) throws JSONException {
	// final Calendar today = Calendar.getInstance();
	// Element eli = doc.select("div#info_jogos").first();
	//
	// eli = eli.select("p").first();
	//
	// final String times = ((TextNode) eli.childNode(0)).text().trim();
	//
	// eli = eli.select("span").first public static void parse(final TimesEnum
	// time, final Jogos jogos) throws
	// // IOException, JSONException {
	// //
	// // Utils.log("Parsing Coritiba.com ... ");
	// // Document doc = null;
	// //
	// // doc =
	// //
	// Jsoup.connect("http://www.coritiba.com.br/portal/elementos/jogostopo/").get();
	// //
	// // final Element el = doc.select("img#jogos_seta_dir").first();
	// // final int id =
	// // Integer.parseInt(el.attr("onclick").replaceFirst("mudarJogo.",
	// // "").replaceFirst(",1.;", ""));
	// //
	// // // Jogo jogo = populateJson(time, doc);
	// // // if (jogo != null) {
	// // // jogos.getJogos().add(jogo);
	// // // }
	// // // while (id > 0) {
	// // // doc =
	// // //
	// //
	// Jsoup.connect("http://www.coritiba.com.br/portal/elementos/jogostopo/").data("jogoAtualId",
	// // // String.valueOf(id), "acima",
	// // // "1").method(Method.POST).execute().parse();
	// // // el = doc.select("img#jogos_seta_dir").first();
	// // //
	// // // int newId =
	// // // Integer.parseInt(el.attr("onclick").replaceFirst("mudarJogo.",
	// // // "").replaceFirst(",1.;", ""));
	// // // if (id == newId) {
	// // // break;
	// // // }
	// // // id = newId;
	// // //
	// // // jogo = populateJson(time, doc);
	// // // if (jogo != null) {
	// // // jogos.getJogos().add(jogo);
	// // // }
	// // // }
	// //
	// // Utils.logln("Done");
	// //
	// // }();
	//
	// final String campeonato = ((TextNode) eli.childNode(0)).text().trim();
	//
	// final String local = ((TextNode)
	// eli.childNode(2)).text().trim().replaceAll("^.*\\- ", "").trim();
	//
	// String data = ((TextNode) eli.childNode(4)).text().trim();
	// data = data.substring(data.indexOf(" ") + 1);
	//
	// final Elements imgs = doc.select("ul").select("img");
	//
	// String transmissao = "";
	// for (final Element element : imgs) {
	// if(element.hasAttr("title") && element.attr("src").contains("on")){
	// if(!transmissao.isEmpty()){
	// transmissao = transmissao.concat(", ");
	// }
	// transmissao = transmissao.concat(element.attr("title"));
	// }
	// }
	//
	// final String[] timeArray = times.split(" x ");
	//
	// final String timea = timeArray[0];
	// final String timeb = timeArray[1];
	//
	// boolean inHome = false;
	// final String adversario;
	// if (timea.equals(time.getNome())) {
	// adversario = timeb;
	// inHome = true;
	// } else {
	// adversario = timea;
	// }
	//
	// Date dt;
	// try {
	// dt = new SimpleDateFormat("dd/MM/yyyy - HH:mm'h'", new Locale("pt",
	// "BR")).parse(data);
	// } catch (final ParseException e2) {
	// dt = new Date();
	// e2.printStackTrace();
	// }
	// final Calendar date = Calendar.getInstance();
	// date.setTime(dt);
	//
	// // if (today.getTimeInMillis() > date.getTimeInMillis()) {
	// // return null;
	// // }
	//
	// final SimpleDateFormat sdfdia = new SimpleDateFormat("dd/MM", new
	// Locale("pt", "BR"));
	// final String dia = sdfdia.format(date.getTime());
	//
	// final SimpleDateFormat sdfhora = new SimpleDateFormat("HH'h'mm", new
	// Locale("pt", "BR"));
	// final String hora = sdfhora.format(date.getTime());
	//
	// // final Jogo jogo = new Jogo();
	// //
	// // jogo.setTimea(timea);
	// // jogo.setTimeb(timeb);
	// // jogo.setCampeonato(campeonato);
	// // jogo.setLocal(local);
	// // jogo.setDia(dia);
	// // jogo.setHora(hora);
	// // jogo.setHome(inHome);
	// // jogo.setAdversario(adversario);
	// // jogo.setTransmissao(transmissao);
	// // jogo.setTimestamp(date.getTimeInMillis());
	// // jogo.setFonte("coritiba");
	//
	// // System.out.println(jogo);
	// // return jogo;
	// }

	// public static void parse(final TimesEnum time, final Jogos jogos) throws
	// IOException, JSONException {
	//
	// Utils.log("Parsing Coritiba.com ... ");
	// Document doc = null;
	//
	// doc =
	// Jsoup.connect("http://www.coritiba.com.br/portal/elementos/jogostopo/").get();
	//
	// final Element el = doc.select("img#jogos_seta_dir").first();
	// final int id =
	// Integer.parseInt(el.attr("onclick").replaceFirst("mudarJogo.",
	// "").replaceFirst(",1.;", ""));
	//
	// // Jogo jogo = populateJson(time, doc);
	// // if (jogo != null) {
	// // jogos.getJogos().add(jogo);
	// // }
	// // while (id > 0) {
	// // doc =
	// //
	// Jsoup.connect("http://www.coritiba.com.br/portal/elementos/jogostopo/").data("jogoAtualId",
	// // String.valueOf(id), "acima",
	// // "1").method(Method.POST).execute().parse();
	// // el = doc.select("img#jogos_seta_dir").first();
	// //
	// // int newId =
	// // Integer.parseInt(el.attr("onclick").replaceFirst("mudarJogo.",
	// // "").replaceFirst(",1.;", ""));
	// // if (id == newId) {
	// // break;
	// // }
	// // id = newId;
	// //
	// // jogo = populateJson(time, doc);
	// // if (jogo != null) {
	// // jogos.getJogos().add(jogo);
	// // }
	// // }
	//
	// Utils.logln("Done");
	//
	// }
}
