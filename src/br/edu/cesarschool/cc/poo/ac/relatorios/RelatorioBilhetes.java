package br.edu.cesarschool.cc.poo.ac.relatorios;

import br.edu.cesarschool.cc.poo.ac.passagem.Bilhete;
import br.edu.cesarschool.cc.poo.ac.passagem.BilheteMediator;

public class RelatorioBilhetes {

    public static void gerarRelatorioBilhetesPorPreco(){
        BilheteMediator mediator = BilheteMediator.obterInstancia();
        Bilhete[] bilhetes = mediator.obterBilhetesPorPreco();
        for(Bilhete bilhete : bilhetes){
            System.out.println(bilhete.toString());
        }
    }

    public static void gerarRelatorioBilhetesPorDH(double precoMax) {
        BilheteMediator mediator = BilheteMediator.obterInstancia();
        Bilhete[] bilhetes = mediator.obterBilhetesPorDataHora(precoMax);
        for (Bilhete bilhete : bilhetes) {
            System.out.println(bilhete.toString());
        }
    }
}