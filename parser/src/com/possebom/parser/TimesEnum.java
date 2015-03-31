package com.possebom.parser;

public enum TimesEnum {
	// CORITIBA(1, "Coritiba", "Coritiba Foot Ball Club", "coritiba", "20025");
	ATLETICOMG(9, "Atlético-MG", "Clube Atlético Mineiro", "atletico-mg", "20003"), ATLETICOPR(8, "Atlético-PR", "Clube Atlético Paranaense", "atletico-pr", "20052"), AVAI(7, "Avaí",
			"Avaí Futebol Clube", "avai", "20058"), BAHIA(6, "Bahia", "Esporte Clube Bahia", "bahia", "20006", "b"), BOTAFOGO(5, "Botafogo", "Botafogo de Futebol e Regatas", "botafogo", "20004", "b"), CEARA(
			4, "Ceará", "Ceará Sporting Club", "ceara", "20031", "b"), CHAPECOENSE(3, "Chapecoense", "Associação Chapecoense de Futebol", "chapecoense", "20086"), CORINTHIANS(2, "Corinthians",
			"Sport Club Corinthians Paulista", "corinthians", "20001"), CORITIBA(1, "Coritiba", "Coritiba Foot Ball Club", "coritiba", "20025"), CRICIUMA(10, "Criciuma", "Criciúma Esporte Clube",
			"criciuma", "20019", "b"), CRUZEIRO(11, "Cruzeiro", "Cruzeiro Esporte Clube", "cruzeiro", "20021"), FIGUEIRENSE(12, "Figueirense", "Figueirense Futebol Clube", "figueirense", "20040"), FLAMENGO(
			13, "Flamengo", "Clube de Regatas do Flamengo", "flamengo", "20016"), FLUMINENSE(14, "Fluminense", "Fluminense Football Club", "fluminense", "20014"), GOIAS(15, "Goiás",
			"Goiás Esporte Clube", "goias", "20028"), GREMIO(16, "Grêmio", "Grêmio Foot-Ball Porto Alegrense", "gremio", "20013"), INTERNACIONAL(17, "Internacional", "Sport Club Internacional",
			"internacional", "20011"), PALMEIRAS(18, "Palmeiras", "Sociedade Esportiva Palmeiras", "palmeiras", "20002"), PARANA(19, "Paraná Clube", "Paraná Clube", "parana", "20015", "b"), SANTOS(
			20, "Santos", "Santos Futebol Clube", "santos", "20008"), SAOPAULO(21, "São Paulo", "São Paulo Futebol Clube", "sao-paulo", "20005"), SPORT(22, "Sport", "Sport Club do Recife", "sport",
			"20010"), VASCO(23, "Vasco", "Club de Regatas Vasco da Gama", "vasco", "20012"), VITORIA(24, "Vitória", "Esporte Clube Vitória", "vitoria", "20018", "b");

	private String nome;
	private String nomeCompleto;
	private String nomeUrl;
	private String urlCbf;
	private String serie;
	private int id;

	private TimesEnum(final int id, final String nome, final String nomeCompleto, final String nomeUrl, final String urlCbf, final String serie) {
		this.nome = nome;
		this.nomeCompleto = nomeCompleto;
		this.urlCbf = urlCbf;
		this.nomeUrl = nomeUrl;
		this.serie = serie;
		this.id = id;
	}

	private TimesEnum(final int id, final String nome, final String nomeCompleto, final String nomeUrl, final String urlCbf) {
		this.nome = nome;
		this.nomeCompleto = nomeCompleto;
		this.urlCbf = urlCbf;
		this.nomeUrl = nomeUrl;
		this.serie = "a";
		this.id = id;
	}

	public String getCampeonato() {
		if (serie.equals("a")) {
			return "Brasileirão";
		} else {
			return "Série B";
		}
	}

	public int getId() {
		return id;
	}

	private TimesEnum(final String nome, final String nomeUrl) {
		this.nome = nome;
		this.nomeUrl = nomeUrl;
	}

	public String getNome() {
		return nome;
	}

	public String getImgUrl() {
		return "http://possebom.com/widgets/images/" + nomeUrl + ".png";
	}

	public String getUrlUol() {
		return "http://esporte.uol.com.br/futebol/times/" + nomeUrl + "/proximos-jogos/";
	}

	public String getUrlUolResults() {
		return "http://esporte.uol.com.br/futebol/times/" + nomeUrl + "/resultados/";
	}

	public String getUrlCbf() {
		return "http://www.cbf.com.br/competicoes/brasileiro-serie-" + serie + "/equipes/2015?c=" + urlCbf;
	}

	public String getNomeUrl() {
		return nomeUrl;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

}
