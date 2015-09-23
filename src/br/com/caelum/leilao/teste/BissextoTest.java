package br.com.caelum.leilao.teste;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.leilao.dominio.Bissexto;

public class BissextoTest {

	@Test
	public void deveSerBissexto() {
		assertEquals(true, new Bissexto().ehBissexto(1992));
	}
}
