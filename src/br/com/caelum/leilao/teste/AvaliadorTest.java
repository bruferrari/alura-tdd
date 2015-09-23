package br.com.caelum.leilao.teste;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;

public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario maria;
	private Leilao leilao;
	private Leilao leilaoOtimizado;
	

	@Before
	public void criaAvaliador() {
		leiloeiro = new Avaliador();
		this.joao = new Usuario("João");
		this.jose = new Usuario("José"); 
		this.maria = new Usuario("Maria"); 
		this.leilao = new Leilao("PS4");
	}
	
	@Test(expected=RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLance() {

		Leilao leilao = new CriadorDeLeilao().para("PS4").build();
		leiloeiro.avalia(leilao);
		
	}
	
	@Test
	public void deveEntenderLancesEmOrdemCrescenteComOutrosValores() {
		
		// Código antigo
		
//		leilao.propoe(new Lance(joao, 250.00));
//		leilao.propoe(new Lance(jose, 300.00));
//		leilao.propoe(new Lance(maria, 400.00));
		
		// Exemplo de como efetuar melhorias no código de testes para que fique
		// sempre legível para o programador
		// Test Data Builders - Cria scenarios de testes
		
		leilaoOtimizado = new CriadorDeLeilao().para("PS4")
				.lance(joao, 250.00)
				.lance(jose, 300.00)
				.lance(maria, 400.00)
				.build();
		
		leiloeiro.avalia(leilaoOtimizado);
		
		double maiorEsperado = 400;
		double menorEsperado = 250;
		
		assertThat(leiloeiro.getMaiorLance(), equalTo(400.00));
		assertThat(leiloeiro.getMenorLance(), equalTo(250.00));
	}
	
	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		
		leilao.propoe(new Lance(joao, 1000.00));
		
		leiloeiro.avalia(leilao);
		assertEquals(1000.00, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(1000.00, leiloeiro.getMenorLance(), 0.00001);
	}
	
	@Test
	public void deveCalcularMedia() {
		
		leilao.propoe(new Lance(joao, 250.00));
		leilao.propoe(new Lance(jose, 300.00));
		leilao.propoe(new Lance(maria, 400.00));
		
		leiloeiro.avalia(leilao);
		
		double mediaEsperada = 316.666666;
		assertEquals(mediaEsperada, leiloeiro.getMedia(), 0.00001);
		
	}
	
	@Test
	public void deveEncontrarOsTresMaioresLances() {
		
		leilao.propoe(new Lance(joao, 100.00));
		leilao.propoe(new Lance(maria, 200.00));
		leilao.propoe(new Lance(joao, 300.00));
		leilao.propoe(new Lance(maria, 400.00));
		
		leiloeiro.avalia(leilao);
		
		// Outra maneira de melhorar a legibilidade do código usando Hamcrest,
		// ao invés de ter vários asserts, temos apenas um assert com tres
		// lances embutidos, melhorando a legibilidade do código
		
		List<Lance> tresMaiores = leiloeiro.getTresMaiores();
		assertEquals(3, tresMaiores.size());
		assertThat(tresMaiores, hasItems(
				new Lance(maria, 400),
				new Lance(joao, 300),
				new Lance(maria, 200)
				));
	}
	
	@Test
	public void testaLancesEmOrdemAleatoria() {
		
		leilao.propoe(new Lance(joao, 200.00));
		leilao.propoe(new Lance(maria, 450.00));
		leilao.propoe(new Lance(joao, 120.00));
		leilao.propoe(new Lance(maria, 700.00));
		leilao.propoe(new Lance(joao, 630.00));
		leilao.propoe(new Lance(maria, 230.00));
		
		leiloeiro.avalia(leilao);
		
		assertEquals(120.00, leiloeiro.getMenorLance(), 0.00001);
		assertEquals(700.00, leiloeiro.getMaiorLance(), 0.00001);
	}
	
	@Test
	public void testaLancesEmOrdemDecrescente() {
		
		leilao.propoe(new Lance(joao, 400.00));
		leilao.propoe(new Lance(maria, 300.00));
		leilao.propoe(new Lance(joao, 200.00));
		leilao.propoe(new Lance(maria, 100.00));
		
		leiloeiro.avalia(leilao);
		
		assertEquals(400.00, leilao.getLances().get(0).getValor(), 0.00001);
		assertEquals(300.00, leilao.getLances().get(1).getValor(), 0.00001);
		assertEquals(200.00, leilao.getLances().get(2).getValor(), 0.00001);
		assertEquals(100.00, leilao.getLances().get(3).getValor(), 0.00001);
		
		assertEquals(400.00, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(100.00, leiloeiro.getMenorLance(), 0.00001);
	}
	
	@Test
	public void deveEncontrarOsTresMaioresDentreCinco() {
		
		leilao.propoe(new Lance(joao, 400.00));
		leilao.propoe(new Lance(maria, 300.00));
		leilao.propoe(new Lance(joao, 200.00));
		leilao.propoe(new Lance(maria, 100.00));
		leilao.propoe(new Lance(joao, 50.00));
		
		leiloeiro.avalia(leilao);
		
		assertEquals(400.00, leiloeiro.getTresMaiores().get(0).getValor(), 0.00001);
		assertEquals(300.00, leiloeiro.getTresMaiores().get(1).getValor(), 0.00001);
		assertEquals(200.00, leiloeiro.getTresMaiores().get(2).getValor(), 0.00001);
	}
	
	@Test
	public void deveDevolverApenasDoisLances() {
		
		leilao.propoe(new Lance(joao, 400.00));
		leilao.propoe(new Lance(maria, 300.00));
		
		leiloeiro.avalia(leilao);
		
		assertEquals(2, leilao.getLances().size());
	}
	
	@Test
	@Ignore
	public void deveDevolverListaVazia() {
		
		leiloeiro.avalia(leilao);
		
		assertEquals(0, leilao.getLances().size());
	}
}
