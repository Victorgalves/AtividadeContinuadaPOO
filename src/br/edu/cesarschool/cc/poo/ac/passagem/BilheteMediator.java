package br.edu.cesarschool.cc.poo.ac.passagem;

import java.time.LocalDateTime;

import br.edu.cesarschool.cc.poo.ac.cliente.Cliente;
import br.edu.cesarschool.cc.poo.ac.cliente.ClienteMediator;
import br.edu.cesarschool.cc.poo.ac.utils.ValidadorCPF;

public class BilheteMediator {
	
	private static BilheteMediator instancia = new BilheteMediator();
	
	private BilheteDAO bilheteDao = new BilheteDAO();
	private BilheteVipDAO bilheteVipDao = new BilheteVipDAO();
	private VooMediator vooMediator = VooMediator.obterInstancia();
	private ClienteMediator clienteMediator = ClienteMediator.obterInstancia();
	
	BilheteMediator() {
	}
	public static BilheteMediator obterInstancia() {

		return instancia;
	}
	public Bilhete buscar(String numeroBilhete) {
		return bilheteDao.buscar(numeroBilhete);
		
	}
	
	public BilheteVip buscarVip(String numeroBilhete) {
		return bilheteVipDao.buscar(numeroBilhete);
	}
	
	
	public String validar(String cpf, String ciaArea, int numeroVoo, double preco, double pagamentoEmPontos, LocalDateTime dataHora) {
		if(!ValidadorCPF.isCpfValido(cpf)) {
			return "CPF errado";	
		}
		String mensagem = vooMediator.validarCiaNumero(ciaArea, numeroVoo);
		if(mensagem != null) {
			return mensagem;
		}
		if(preco < 0) {
			return "Preco errado";
		}
		if(pagamentoEmPontos < 0) {
			return "Pagamento em pontos errado";
		}
		if(preco < pagamentoEmPontos) {
			System.out.println("Preço menor que pagamento em pontos");
		}
		
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		if(dataHora.isBefore(dataHoraAtual.plusHours(1))) {
			return "data hora invalida";
		}
		
		return null;
	}
	
	public ResultadoGeracaoBilhete gerarBilhete(String cpf, String ciaAerea, int numeroVoo, double preco, double pagamentoEmPontos, LocalDateTime dataHora) {
		
		String mensagemValidacao = validar(cpf, ciaAerea, numeroVoo, preco, pagamentoEmPontos, dataHora);
		if(mensagemValidacao != null) {
			return new ResultadoGeracaoBilhete(null, null, mensagemValidacao);
		}else {
			
			Voo voo = new Voo(null , null,ciaAerea, numeroVoo);
			
			String idVoo = voo.obterIdVoo();
			
			Voo vooEncontrado = vooMediator.buscar(idVoo);
			if(vooEncontrado == null) {
				return new ResultadoGeracaoBilhete(null, null, "Voo não encontrado");
			}else {
				
				Cliente cliente = clienteMediator.buscar(cpf);		
				if(cliente == null) {
					return new ResultadoGeracaoBilhete(null, null, "Cliente não encontrado");
				}else {
					double pontosNecessarios = pagamentoEmPontos * 20;
					if(cliente.getSaldoPontos() < pontosNecessarios ) {
						return new ResultadoGeracaoBilhete(null, null, "Pontos insuficientes");
					}	else {
						Bilhete bilhete = new Bilhete(cliente, vooEncontrado, preco, pagamentoEmPontos, dataHora);
						
						cliente.debitarPontos(pontosNecessarios);
						cliente.creditarPontos(bilhete.getPagamentoEmPontos());						
						boolean sucessoIncluir =  bilheteDao.incluir(bilhete);
						if(!sucessoIncluir) {
							return new ResultadoGeracaoBilhete(null, null, "Bilhete já existente");
						}else {
							String alterarCliente = clienteMediator.alterar(cliente);
							if(alterarCliente != null) {
								return new ResultadoGeracaoBilhete(null, null, alterarCliente);
							}
							
							
							return new ResultadoGeracaoBilhete(bilhete, null, null);
							
						}
					}
				}
				
			}
		}
	}
	public ResultadoGeracaoBilhete gerarBilheteVip(String cpf, String ciaAerea, int numeroVoo, double preco, double pagamentoEmPontos, LocalDateTime dataHora, double bonusPontuacao) {
		
		String mensagemValidacao = validar(cpf, ciaAerea, numeroVoo, preco, pagamentoEmPontos, dataHora);
		if(mensagemValidacao != null) {
			return new ResultadoGeracaoBilhete(null, null, mensagemValidacao);
		}else {
			if(bonusPontuacao <0 || bonusPontuacao > 100) {
				return new ResultadoGeracaoBilhete(null, null, "bonus errado");
			}else {
				Voo voo = new Voo(null, null, ciaAerea, numeroVoo);
				String idVoo = voo.obterIdVoo();
				Voo vooEncontrado = vooMediator.buscar(idVoo);
				if(vooEncontrado == null) {
					return new ResultadoGeracaoBilhete(null, null, "Voo não encontrado");
				}else {
					Cliente cliente = clienteMediator.buscar(cpf);
					if(cliente == null) {
						return new ResultadoGeracaoBilhete(null, null, "Cliente não encontrado");
	                } else {
	                	double pontosNecessarios = pagamentoEmPontos * 20;
	                	if(cliente.getSaldoPontos() < pontosNecessarios) {
	                		return new ResultadoGeracaoBilhete(null, null, "Pontos insuficientes");
	                	}else {
	                		BilheteVip bilheteVip = new BilheteVip(cliente, vooEncontrado, preco, pagamentoEmPontos, dataHora, bonusPontuacao);
	                		cliente.debitarPontos(pontosNecessarios);
	                		cliente.creditarPontos(bilheteVip.obterValorPontuacaoVip());
	                		
	                		boolean incluirBilheteVip = bilheteDao.incluir(bilheteVip);
	                		if(!incluirBilheteVip) {
	                			 return new ResultadoGeracaoBilhete(null, null, "Bilhete VIP já existente");
	                		}else {
	                			String alteracaoCliente = clienteMediator.alterar(cliente);
	                			if(alteracaoCliente != null) {
	                				return new ResultadoGeracaoBilhete(null, null, alteracaoCliente);
	                			}else {
	                				return new ResultadoGeracaoBilhete(null, bilheteVip, null);
	                			}
	                		}
	                	}
					}
				}
			}
		}
		
	}
}