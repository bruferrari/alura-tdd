package br.com.caelum.leilao.dominio;

public class Bissexto {
	
	public boolean ehBissexto(int ano) {
		if((ano % 4 == 0) && (ano % 100 != 0) || (ano % 400 == 0))
			return true;
		else
			return false;
	}

}
