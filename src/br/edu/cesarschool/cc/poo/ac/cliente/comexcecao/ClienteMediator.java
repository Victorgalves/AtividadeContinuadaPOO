package br.edu.cesarschool.cc.poo.ac.cliente.comexcecao;

import br.edu.cesarschool.cc.poo.ac.cliente.Cliente;
import br.edu.cesarschool.cc.poo.ac.cliente.ClienteDAO;
import br.edu.cesarschool.cc.poo.ac.utils.StringUtils;
import br.edu.cesarschool.cc.poo.ac.utils.ValidadorCPF;
import br.edu.cesarschool.cc.poo.ac.utils.ordenacao.Ordenadora;

public class ClienteMediator {
	private static final String CLIENTE_INEXISTENTE = "Cliente inexistente";
	private static final String CPF_ERRADO = "CPF errado";
	private static final int TAM_MIN_NOME = 2;
	private static final String NOME_ERRADO = "nome errado";
	private static ClienteMediator instancia;
	private br.edu.cesarschool.cc.poo.ac.cliente.ClienteDAO clienteDao = new ClienteDAO();

	public static ClienteMediator obterInstancia() {
		if (instancia == null) {
			instancia = new ClienteMediator();
		}
		return instancia;
	}
	private ClienteMediator() {
		
	}
	public void validar(Cliente cliente) throws ExcecaoValidacao {
		ExcecaoValidacao excecaoValidacao = new ExcecaoValidacao();
		if (cliente == null) {
			excecaoValidacao.adicionarMensagem("Cliente não informado");
		} else {
			if (!ValidadorCPF.isCpfValido(cliente.getCpf())) {
				excecaoValidacao.adicionarMensagem(CPF_ERRADO);
			}
			if (StringUtils.isVaziaOuNula(cliente.getNome()) || cliente.getNome().length() < TAM_MIN_NOME) {
				excecaoValidacao.adicionarMensagem(NOME_ERRADO);
			}
			if (cliente.getSaldoPontos() < 0) {
				excecaoValidacao.adicionarMensagem("saldo errado");
			}
		}
		if (!excecaoValidacao.getMensagens().isEmpty()) {
			throw excecaoValidacao;
		}
	}


	public void incluir(Cliente cliente) throws ExcecaoRegistroJaExistente, ExcecaoValidacao {
		validar(cliente);
		if (!clienteDao.incluir(cliente)) {
			throw new ExcecaoRegistroJaExistente("Cliente existente");
		}
	}

	public void alterar(Cliente cliente) throws ExcecaoValidacao, ExcecaoRegistroInexistente {
		validar(cliente);
		if (!clienteDao.alterar(cliente)) {
			throw new ExcecaoRegistroInexistente(CLIENTE_INEXISTENTE);
		}
	}	
	public Cliente buscar(String cpf) throws  ExcecaoRegistroInexistente {
		Cliente cliente = clienteDao.buscar(cpf);
		if(cliente == null){
			throw new ExcecaoRegistroInexistente(CLIENTE_INEXISTENTE);
		}
		return cliente;
	}

	public void excluir(String cpf) throws ExcecaoRegistroInexistente, ExcecaoValidacao {
		if (!ValidadorCPF.isCpfValido(cpf)) {
			ExcecaoValidacao excecaoValidacao = new ExcecaoValidacao();
			excecaoValidacao.adicionarMensagem(CPF_ERRADO);
			throw excecaoValidacao;
		}
		if (!clienteDao.excluir(cpf)) {
			throw new ExcecaoRegistroInexistente(CLIENTE_INEXISTENTE);
		}
	}

	public Cliente[] obterClientesPorNome(){
		Cliente[] clientes = clienteDao.buscarTodos();
		Ordenadora.ordenar(clientes);
		return clientes;
	}
}
