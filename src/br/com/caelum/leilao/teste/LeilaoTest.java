package br.com.caelum.leilao.teste;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;

public class LeilaoTest {
	
	private Leilao leilao;
	private Usuario steve;
	private Usuario bill;
	
	@Before
	public void setUp() {
		this.steve = new Usuario("Steve Trabalhos");
		this.bill = new Usuario("Tio bill");
	}
	
	@Test(expected=IllegalArgumentException.class)
	@Ignore
	public void naoDeveReceberLanceCasoSejaMenorOuIgualAZero() {
		leilao = new CriadorDeLeilao().para("MacBook Pro 15")
				.lance(steve, -123.00)
				.lance(bill, 0)
				.build();
	}
	
	@Test
	public void deveReceberUmLance() {
		Leilao leilao = new Leilao("MacbookPro 15");
		assertEquals(0, leilao.getLances().size());
		
		leilao.propoe(new Lance(new Usuario("Steve Trabalhos"), 2000));
		
		assertEquals(1, leilao.getLances().size());
		assertEquals(2000, leilao.getLances().get(0).getValor(), 0.00001);
	}
	
	@Test
	public void deveReceberVariosLances() {
		Leilao leilao = new Leilao("MacbookPro 15");
		leilao.propoe(new Lance(new Usuario("Steve Trabalhos"), 2000));
		leilao.propoe(new Lance(new Usuario("Steve Wozniak"), 3000));
		assertEquals(2, leilao.getLances().size());
		assertEquals(2000, leilao.getLances().get(0).getValor(), 0.00001);
		assertEquals(3000, leilao.getLances().get(1).getValor(), 0.00001);
	}
	
	@Test
	public void naoDeveAceitar2LancesSeguidosDoMesmoUsuario() {
		Leilao leilao = new Leilao("MacbookPro 15");
		Usuario steve = new Usuario("Steve Trabalhos");
		leilao.propoe(new Lance(steve, 2000));
		leilao.propoe(new Lance(steve, 3000));
		
		assertEquals(1, leilao.getLances().size());
		assertEquals(2000, leilao.getLances().get(0).getValor(), 0.00001);
	}
	
	@Test
	public void naoDeveAceitarMaisDoQue5LancesDoMesmoUsuario() {
		Leilao leilao = new Leilao("MacbookPro 15");
		Usuario steve = new Usuario("Steve Trabalhos");
		Usuario bill = new Usuario("Tio Bill");
		
		leilao.propoe(new Lance(steve, 2000));
		leilao.propoe(new Lance(bill, 3000));
		
		leilao.propoe(new Lance(steve, 4000));
		leilao.propoe(new Lance(bill, 6000));
		
		leilao.propoe(new Lance(steve, 8000));
		leilao.propoe(new Lance(bill, 12000));
		
		leilao.propoe(new Lance(steve, 16000));
		leilao.propoe(new Lance(bill, 24000));
		
		leilao.propoe(new Lance(steve, 32000));
		leilao.propoe(new Lance(bill, 48000));
		
		//deve ser ignorado
		leilao.propoe(new Lance(steve, 34000));
		
		assertEquals(10, leilao.getLances().size());
		assertEquals(48000, leilao.getLances().get(leilao.getLances().size()-1).getValor(), 0.00001);
	}
	
    @Test
    public void deveDobrarOUltimoLanceDado() {
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve Jobs");
        Usuario billGates = new Usuario("Bill Gates");

        leilao.propoe(new Lance(steveJobs, 2000));
        leilao.propoe(new Lance(billGates, 3000));
        leilao.dobraLance(steveJobs);

        assertEquals(4000, leilao.getLances().get(2).getValor(), 0.00001);
    }
    
    @Test
    public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
    	Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve Jobs");

        leilao.dobraLance(steveJobs);

        assertEquals(0, leilao.getLances().size());
    }
}
