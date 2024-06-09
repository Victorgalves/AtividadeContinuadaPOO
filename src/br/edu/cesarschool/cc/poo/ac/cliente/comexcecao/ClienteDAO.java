package br.edu.cesarschool.cc.poo.ac.cliente.comexcecao;

import br.edu.cesarschool.cc.poo.ac.cliente.Cliente;
import br.edu.cesarschool.cc.poo.ac.utils.Registro;
import br.edu.cesarschool.cc.poo.ac.utils.SuperDAO;

public class ClienteDAO extends SuperDAO<Cliente> {

	@Override
	public Class<Cliente> obterTipo() {
		return Cliente.class;
	}

	public Cliente buscar(String cpf) throws ExcecaoRegistroInexistente {
		Cliente cliente = (Cliente) daoGenerico.buscar(cpf);
		if (cliente == null) {
			throw new ExcecaoRegistroInexistente("Cliente não encontrado.");
		}
		return cliente;
	}

	public void incluir(Cliente cliente) throws ExcecaoRegistroJaExistente {
        try {
            if (buscar(cliente.getIdUnico()) == null) {
                if (!daoGenerico.incluir(cliente)) {
                    throw new ExcecaoRegistroJaExistente("Falha ao incluir cliente.");
                }
            } else {
                throw new ExcecaoRegistroJaExistente("Cliente já existe.");
            }
        } catch (ExcecaoRegistroInexistente e) {
            throw new RuntimeException(e);
        }
    }


	public void alterar(Cliente cliente) throws ExcecaoRegistroInexistente {
		if (buscar(cliente.getIdUnico()) != null) {
			if(!daoGenerico.alterar(cliente)){
				throw new ExcecaoRegistroInexistente("Falha ao alterar. Registro inexistente");
			}else {
				throw new ExcecaoRegistroInexistente("Cliente não encontrado");
			}
		}
	}

	public void excluir(String cpf) throws  ExcecaoRegistroInexistente{
		if (buscar(cpf) != null) {
			if(!daoGenerico.excluir(cpf)){
				throw new ExcecaoRegistroInexistente("Falha ao tentar exlcuir. Cliente inexistente");
			}else{
				throw new ExcecaoRegistroInexistente("Cliente não encontrado");
			}
		}
	}

	public Cliente[] buscarTodos() {
		Registro[] registros = daoGenerico.buscarTodos();
		Cliente[] clientes = new Cliente[registros.length];
		for (int i = 0; i < registros.length; i++) {
			clientes[i] = (Cliente) registros[i];
		}
		return clientes;
	}
}