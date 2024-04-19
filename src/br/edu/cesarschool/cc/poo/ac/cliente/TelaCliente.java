package br.edu.cesarschool.cc.poo.ac.cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TelaCliente {
    private ClienteMediator clienteMediator= ClienteMediator.obterInstancia();
    private static final String DIGITE_O_CPF = "Digite o cpf: ";
    private static final String CPF_NAO_ENCONTRADO = "Cliente não encontrado!";
    private static final String CPF_DESCONHECIDO = String.valueOf(-1);
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final BufferedReader ENTRADA_STR = new BufferedReader(new InputStreamReader(System.in));

    public void inicializaTelasCadastroProduto() {
        while(true) {
            String cpf = CPF_DESCONHECIDO;
            imprimeMenuPrincipal();
            int opcao = ENTRADA.nextInt();
            if (opcao == 1) {
                processaInclusao();
            } else if (opcao == 2) {
                cpf = processaBusca();
                if (cpf != CPF_DESCONHECIDO) {
                    processaAlteracao(cpf);
                }
            } else if (opcao == 3) {
                cpf = processaBusca();
                if (cpf != CPF_DESCONHECIDO) {
                    processaExclusao(cpf);
                }
            } else if (opcao == 4) {
                processaBusca();
            } else if (opcao == 5) {
                System.out.println("Saindo do cadastro de produtos");
                System.exit(0);
            } else {
                System.out.println("Opção inválida!!");
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
        Cliente cliente = capturaCliente(CPF_DESCONHECIDO);
        String retorno = clienteMediator.incluir(cliente);
        if (retorno == null) {
            System.out.println("Cliente incluído com sucesso!");
        } else {
            System.out.println(retorno);
        }
    }
    private void processaAlteracao(String cpf) {
        Cliente cliente = capturaCliente(cpf);
        String retorno = clienteMediator.alterar(cliente);
        if (retorno == null) {
            System.out.println("Cliente alterado com sucesso!");
        } else {
            System.out.println(retorno);
        }
    }

    private String processaBusca() {
        System.out.print(DIGITE_O_CPF);
        String cpf = ENTRADA.next();
        Cliente cliente = clienteMediator.buscar(cpf);
        if (cliente == null) {
            System.out.println(CPF_NAO_ENCONTRADO);
            return CPF_DESCONHECIDO;
        } else {
            // Mostrar todos os atributos do Produto!!
            System.out.println("Cpf: " + cliente.getCpf());
            System.out.println("Nome: " + cliente.getNome());
            return cpf;
        }
    }
        private void processaExclusao(String cpf){
            String retorno = clienteMediator.excluir(cpf);
            if (retorno == null) {
                System.out.println("Cliente excluído com sucesso!");
            } else {
                System.out.println(retorno);
            }
        }
            private Cliente capturaCliente(String codigoDaAlteracao) {
                Cliente cliente = null;
                String cpf;
                double saldoPontos = 0;
                if (codigoDaAlteracao == CPF_DESCONHECIDO) {
                    System.out.print(DIGITE_O_CPF);
                    cpf = ENTRADA.next();
                } else {
                    cpf = codigoDaAlteracao;
                }
                // Lê todos os atributos do Produto
                System.out.print("Digite o nome: ");
                String nome = lerString();
                // Cria instância de Produto com todos os atributos
                cliente = new Cliente(cpf, nome,saldoPontos);
                return cliente;
            }
    private String lerString() {
        try {
            return ENTRADA_STR.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        }

