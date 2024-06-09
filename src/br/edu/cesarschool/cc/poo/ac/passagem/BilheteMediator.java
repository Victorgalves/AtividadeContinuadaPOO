package br.edu.cesarschool.cc.poo.ac.passagem;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.cesarschool.cc.poo.ac.cliente.Cliente;
import br.edu.cesarschool.cc.poo.ac.cliente.ClienteMediator;
import br.edu.cesarschool.cc.poo.ac.negocio.comparadores.ComparadorBilheteDataHora;
import br.edu.cesarschool.cc.poo.ac.negocio.comparadores.ComparadorBilhetePreco;
import br.edu.cesarschool.cc.poo.ac.utils.DiaDaSemana;
import br.edu.cesarschool.cc.poo.ac.utils.ValidadorCPF;
import br.edu.cesarschool.cc.poo.ac.utils.ordenacao.Ordenadora;

public class BilheteMediator {
	private static BilheteMediator instancia;
	private static final int FATOR_CONV_PONTOS = 20;
	private BilheteDAO bilheteDao = new BilheteDAO();
	private BilheteVipDAO bilheteVipDao = new BilheteVipDAO();
	private ClienteMediator clienteMediator = ClienteMediator.obterInstancia();
	private VooMediator vooMediator = VooMediator.obterInstancia();

	public static BilheteMediator obterInstancia() {
		if (instancia == null) {
			instancia = new BilheteMediator();
		}
		return instancia;
	}

	private BilheteMediator() {}

	public Bilhete buscar(String numeroBilhete) {
		return bilheteDao.buscar(numeroBilhete);
	}

	public BilheteVip buscarVip(String numeroBilhete) {
		return bilheteVipDao.buscar(numeroBilhete);
	}

	public String validar(String cpf, String ciaAerea, int numeroVoo,
						  double preco, double pagamentoEmPontos, LocalDateTime dataHora) {
		if (!ValidadorCPF.isCpfValido(cpf)) {
			return "CPF errado";
		} else {
			String msg = vooMediator.validarCiaNumero(ciaAerea, numeroVoo);
			if (msg != null) {
				return msg;
			}
		}
		if (preco <= 0) {
			return "Preco errado";
		}
		if (pagamentoEmPontos < 0) {
			return "Pagamento pontos errado";
		}
		if (preco < pagamentoEmPontos) {
			return "Preco menor que pagamento em pontos";
		}
		if (dataHora == null || LocalDateTime.now().plusHours(1).isAfter(dataHora)) {
			return "data hora invalida";
		}

		// Buscar o voo para validação adicional
		Voo vooAux = new Voo(null, null, ciaAerea, numeroVoo);
		String idVoo = vooAux.obterIdVoo();
		Voo vooCad = vooMediator.buscar(idVoo);
		if (vooCad == null) {
			return "Voo nao encontrado";
		}

		// Validação do dia da semana
		DayOfWeek diaSemanaDataHora = dataHora.getDayOfWeek();
		DiaDaSemana[] diasDaSemanaVoo = vooCad.getDiaDaSemana();
		if (diasDaSemanaVoo != null && diasDaSemanaVoo.length > 0) {
			boolean diaSemanaValido = false;
			for (DiaDaSemana dia : diasDaSemanaVoo) {
				if (dia.ordinal() + 1 == diaSemanaDataHora.getValue()) { // Ajustar índice
					diaSemanaValido = true;
					break;
				}
			}
			if (!diaSemanaValido) {
				return "Voo nao disponivel na data";
			}
		}

		// Validação da hora
		LocalTime horaVoo = vooCad.getHora();
		if (horaVoo != null) {
			LocalTime horaDataHora = dataHora.toLocalTime();
			if (!horaDataHora.equals(horaVoo)) {
				return "Hora diferente da especificada no voo";
			}
		}

		return null;
	}

	private ResultadoAuxiliar gerarBilheteAux(String cpf, String ciaAerea, int numeroVoo,
											  double preco, double pagamentoEmPontos, LocalDateTime dataHora) {
		String msg = validar(cpf, ciaAerea, numeroVoo, preco, pagamentoEmPontos, dataHora);
		if (msg != null) {
			return new ResultadoAuxiliar(null, null, new ResultadoGeracaoBilhete(null, null, msg), 0);
		}
		Voo vooAux = new Voo(null, null, ciaAerea, numeroVoo);
		String idVoo = vooAux.obterIdVoo();
		Voo vooCad = vooMediator.buscar(idVoo);
		if (vooCad == null) {
			return new ResultadoAuxiliar(null, null, new ResultadoGeracaoBilhete(null, null, "Voo nao encontrado"), 0);
		}
		Cliente cli = clienteMediator.buscar(cpf);
		if (cli == null) {
			return new ResultadoAuxiliar(null, null, new ResultadoGeracaoBilhete(null, null, "Cliente nao encontrado"), 0);
		}
		double valorNecessarioPontos = pagamentoEmPontos * FATOR_CONV_PONTOS;
		if (cli.getSaldoPontos() < valorNecessarioPontos) {
			return new ResultadoAuxiliar(null, null, new ResultadoGeracaoBilhete(null, null, "Pontos insuficientes"), 0);
		}
		return new ResultadoAuxiliar(vooCad, cli, null, valorNecessarioPontos);
	}

	public ResultadoGeracaoBilhete gerarBilhete(String cpf, String ciaAerea, int numeroVoo,
												double preco, double pagamentoEmPontos, LocalDateTime dataHora) {
		ResultadoAuxiliar res = gerarBilheteAux(cpf, ciaAerea, numeroVoo,
				preco, pagamentoEmPontos, dataHora);
		if (res.getRes() != null) {
			return res.getRes();
		}
		Cliente cli = res.getCliente();
		Bilhete bilhete = new Bilhete(cli, res.getVoo(), preco, pagamentoEmPontos, dataHora);
		cli.debitarPontos(res.getValorNecessarioPontos());
		cli.creditarPontos(bilhete.obterValorPontuacao());
		if (!bilheteDao.incluir(bilhete)) {
			return new ResultadoGeracaoBilhete(null, null, "Bilhete ja existente");
		}
		String msg = clienteMediator.alterar(cli);
		if (msg != null) {
			return new ResultadoGeracaoBilhete(null, null, msg);
		}
		return new ResultadoGeracaoBilhete(bilhete, null, null);
	}

	public ResultadoGeracaoBilhete gerarBilheteVip(String cpf, String ciaAerea, int numeroVoo,
												   double preco, double pagamentoEmPontos, LocalDateTime dataHora, double bonusPontuacao) {
		ResultadoAuxiliar res = gerarBilheteAux(cpf, ciaAerea, numeroVoo,
				preco, pagamentoEmPontos, dataHora);
		if (res.getRes() != null) {
			return res.getRes();
		}
		Cliente cli = res.getCliente();
		BilheteVip bilhete = new BilheteVip(cli, res.getVoo(), preco, pagamentoEmPontos,
				dataHora, bonusPontuacao);
		cli.debitarPontos(res.getValorNecessarioPontos());
		cli.creditarPontos(bilhete.obterValorPontuacaoVip());
		if (!bilheteVipDao.incluir(bilhete)) {
			return new ResultadoGeracaoBilhete(null, null, "Bilhete ja existente");
		}
		String msg = clienteMediator.alterar(cli);
		if (msg != null) {
			return new ResultadoGeracaoBilhete(null, null, msg);
		}
		return new ResultadoGeracaoBilhete(null, bilhete, null);
	}

	public Bilhete[] obterBilhetesPorPreco(){
		Bilhete[] bilhetes = bilheteDao.buscarTodos();
		Ordenadora.ordenar(bilhetes, new ComparadorBilhetePreco());
		return bilhetes;
	}

	public Bilhete[] obterBilhetesPorDataHora(double precoMin){
		Bilhete[] bilhetes = bilheteDao.buscarTodos();
		List<Bilhete> filtrar = Arrays.stream(bilhetes).filter(bilhete -> bilhete.getPreco() <= precoMin).collect(Collectors.toList());
		Bilhete[] ArrayFiltrados = filtrar.toArray(new Bilhete[0]);
		Ordenadora.ordenar(ArrayFiltrados, new ComparadorBilheteDataHora());
		return ArrayFiltrados;
	}

}
