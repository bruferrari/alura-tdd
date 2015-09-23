package br.com.caelum.leilao.teste;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.leilao.palindromo.Palindromo;

public class PalindromoTest {

	@Test
	public void deveTestarSeFraseEhPalindromo() {
		Assert.assertEquals(true, new Palindromo().ehPalindromo("Anotaram a data da maratona"));
	}
}
