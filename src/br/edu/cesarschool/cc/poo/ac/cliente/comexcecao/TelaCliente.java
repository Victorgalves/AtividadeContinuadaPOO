package br.edu.cesarschool.cc.poo.ac.cliente.comexcecao;

import br.edu.cesarschool.cc.poo.ac.cliente.Cliente;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TelaCliente {
    private ClienteMediator clienteMediator = ClienteMediator.obterInstancia();
    private static final String DIGITE_O_CPF = "Digite o cpf: ";
    private static final String CPF_DESCONHECIDO = String.valueOf(-1);
    private static final Scanner ENTRADA;
    private static final BufferedReader ENTRADA_STR;

    static {
        ENTRADA = new Scanner(System.in);
        ENTRADA_STR = new BufferedReader(new InputStreamReader(System.in));
    }

    public TelaCliente() {
    }

    public void inicializaTelasCadastroProduto() {
        while (true) {
            String cpf = CPF_DESCONHECIDO;
            this.imprimeMenuPrincipal();
            int opcao = ENTRADA.nextInt();
            switch (opcao) {
                case 1:
                    this.processaInclusao();
                    break;
                case 2:
                    cpf = this.processaBusca();
                    if (!cpf.equals(CPF_DESCONHECIDO)) {
                        this.processaAlteracao(cpf);
                    }
                    break;
                case 3:
                    cpf = this.processaBusca();
                    if (!cpf.equals(CPF_DESCONHECIDO)) {
                        this.processaExclusao(cpf);
                    }
                    break;
                case 4:
                    this.processaBusca();
                    break;
                case 5:
                    System.out.println("Saindo do cadastro de produtos");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida!!");
                    break;
            }
        }
    }

    private void imprimeMenuPrincipal() {
        System.out.println("1- Incluir");
        System.out.println("2- Alterar");
        System.out.println("3- Excluir");
        System.out.println("4- Buscar");
        System.out.println("5- Sair");
        System.out.print("Digite a opção: ");
    }

    private void processaInclusao() {
        Cliente cliente = this.capturaCliente(CPF_DESCONHECIDO);
        try {
            clienteMediator.incluir(cliente);
            System.out.println("Cliente incluído com sucesso!");
        } catch (ExcecaoRegistroJaExistente e) {
            System.out.println(e.getMessage());
        } catch (ExcecaoValidacao e) {
            for (String mensagem : e.getMensagens()) {
                System.out.println(mensagem);
            }
        }
    }

    private void processaAlteracao(String cpf) {
        Cliente cliente = this.capturaCliente(cpf);
        try {
            clienteMediator.alterar(cliente);
            System.out.println("Cliente alterado com sucesso!");
        } catch (ExcecaoRegistroInexistente e) {
            System.out.println(e.getMessage());
        } catch (ExcecaoValidacao e) {
            for (String mensagem : e.getMensagens()) {
                System.out.println(mensagem);
            }
        }
    }

    private String processaBusca() {
        System.out.print(DIGITE_O_CPF);
        String cpf = ENTRADA.next();
        try {
            Cliente cliente = clienteMediator.buscar(cpf);
            System.out.println("Cpf: " + cliente.getCpf());
            System.out.println("Nome: " + cliente.getNome());
            return cpf;
        } catch (ExcecaoRegistroInexistente e) {
            System.out.println(e.getMessage());
            return CPF_DESCONHECIDO;
        }
    }

    private void processaExclusao(String cpf) {
        try {
            clienteMediator.excluir(cpf);
            System.out.println("Cliente excluído com sucesso!");
        } catch (ExcecaoRegistroInexistente e) {
            System.out.println(e.getMessage());
        } catch (ExcecaoValidacao e) {
            for (String mensagem : e.getMensagens()) {
                System.out.println(mensagem);
            }
        }
    }

    private Cliente capturaCliente(String codigoDaAlteracao) {
        String cpf;
        if (codigoDaAlteracao.equals(CPF_DESCONHECIDO)) {
            System.out.print(DIGITE_O_CPF);
            cpf = ENTRADA.next();
        } else {
            cpf = codigoDaAlteracao;
        }

        System.out.print("Digite o nome: ");
        String nome = lerString();
        return new Cliente(cpf, nome, 0.0);
    }

    private String lerString() {
        try {
            return ENTRADA_STR.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
