package br.edu.cesarschool.cc.poo.ac.passagem;

import br.edu.cesarschool.cc.poo.ac.utils.DiaDaSemana;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Scanner;

public class TelaVoo {
    private static final String DIGITE_O_ID_VOO = "Digite o ID do voo (Companhia + Número): ";
    private static final String VOO_NAO_ENCONTRADO = "Voo não encontrado!";
    private static final String ID_VOO_DESCONHECIDO = null;
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final BufferedReader ENTRADA_STR = new BufferedReader(new InputStreamReader(System.in));
    private VooMediator vooMediator = VooMediator.obterInstancia();

    public void inicializaTelasCadastroVoo() {
        while (true) {
            String idVoo = ID_VOO_DESCONHECIDO;
            imprimeMenuPrincipal();
            int opcao = ENTRADA.nextInt();
            ENTRADA.nextLine(); // Consume newline
            if (opcao == 1) {
                processaInclusao();
            } else if (opcao == 2) {
                idVoo = processaBusca();
                if (idVoo != ID_VOO_DESCONHECIDO) {
                    processaAlteracao(idVoo);
                }
            } else if (opcao == 3) {
                idVoo = processaBusca();
                if (idVoo != ID_VOO_DESCONHECIDO) {
                    processaExclusao(idVoo);
                }
            } else if (opcao == 4) {
                idVoo = processaBusca();
            } else if (opcao == 5) {
                System.out.println("Saindo do cadastro de voos");
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
        Voo voo = capturaVoo(ID_VOO_DESCONHECIDO);
        String retorno = vooMediator.incluir(voo);
        if (retorno == null) {
            System.out.println("Voo incluído com sucesso!");
        } else {
            System.out.println(retorno);
        }
    }

    private void processaAlteracao(String idVoo) {
        Voo voo = capturaVoo(idVoo);
        String retorno = vooMediator.alterar(voo);
        if (retorno == null) {
            System.out.println("Voo alterado com sucesso!");
        } else {
            System.out.println(retorno);
        }
    }

    private String processaBusca() {
        System.out.print(DIGITE_O_ID_VOO);
        String idVoo = lerString();
        Voo voo = vooMediator.buscar(idVoo);
        if (voo == null) {
            System.out.println(VOO_NAO_ENCONTRADO);
            return ID_VOO_DESCONHECIDO;
        } else {
            // Mostrar todos os atributos do Voo!!
            System.out.println("Número do Voo: " + voo.getNumeroVoo());
            System.out.println("Aeroporto de Origem: " + voo.getAeroportoOrigem());
            System.out.println("Aeroporto de Destino: " + voo.getAeroportoDestino());
            System.out.println("Companhia Aérea: " + voo.getCompanhiaAerea());
            System.out.println("Dias da Semana: " + Arrays.toString(voo.getDiaDaSemana()));
            System.out.println("Hora: " + voo.getHora().toString().substring(0, 5));
            return idVoo;
        }
    }

    private void processaExclusao(String idVoo) {
        String retorno = vooMediator.excluir(idVoo);
        if (retorno == null) {
            System.out.println("Voo excluído com sucesso!");
        } else {
            System.out.println(retorno);
        }
    }

    private Voo capturaVoo(String idVooDaAlteracao) {
        String companhiaAerea;
        int numeroVoo;
        if (idVooDaAlteracao == ID_VOO_DESCONHECIDO) {
            System.out.print(DIGITE_O_ID_VOO);
            String idVoo = lerString();
            companhiaAerea = idVoo.substring(0, 2);
            numeroVoo = Integer.parseInt(idVoo.substring(2));
        } else {
            companhiaAerea = idVooDaAlteracao.substring(0, 2);
            numeroVoo = Integer.parseInt(idVooDaAlteracao.substring(2));
        }
        System.out.print("Digite o aeroporto de origem: ");
        String aeroportoOrigem = lerString();
        System.out.print("Digite o aeroporto de destino: ");
        String aeroportoDestino = lerString();
        System.out.print("Digite a companhia aérea: ");
        String ciaAerea = lerString();
        if (!ciaAerea.equals(companhiaAerea)) {
            companhiaAerea = ciaAerea;
        }
        System.out.print("Digite os dias da semana (separados por vírgula, ex: segunda-feira,terça-feira,quarta-feira): ");
        String[] diasStr = lerString().split(",");
        DiaDaSemana[] diaDaSemana = new DiaDaSemana[diasStr.length];

        for (int i = 0; i < diasStr.length; i++) {
            switch (diasStr[i].trim().toLowerCase()) {
                case "segunda-feira":
                    diaDaSemana[i] = DiaDaSemana.SEGUNDA_FEIRA;
                    break;
                case "terça-feira":
                    diaDaSemana[i] = DiaDaSemana.TERCA_FEIRA;
                    break;
                case "quarta-feira":
                    diaDaSemana[i] = DiaDaSemana.QUARTA_FEIRA;
                    break;
                case "quinta-feira":
                    diaDaSemana[i] = DiaDaSemana.QUINTA_FEIRA;
                    break;
                case "sexta-feira":
                    diaDaSemana[i] = DiaDaSemana.SEXTA_FEIRA;
                    break;
                case "sábado":
                    diaDaSemana[i] = DiaDaSemana.SABADO;
                    break;
                case "domingo":
                    diaDaSemana[i] = DiaDaSemana.DOMINGO;
                    break;
                default:
                    throw new IllegalArgumentException("Dia da semana inválido: " + diasStr[i]);
            }
        }


        System.out.print("Digite a hora (HH:MM): ");
        String horaStr = lerString() + ":00";
        LocalTime hora = LocalTime.parse(horaStr);

        return new Voo(aeroportoOrigem, aeroportoDestino, companhiaAerea, numeroVoo, diaDaSemana, hora);
    }

    private String lerString() {
        try {
            return ENTRADA_STR.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
