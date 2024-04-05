package br.edu.cesarschool.cc.poo.ac.passagem;

import br.edu.cesarschool.cc.poo.ac.cliente.Cliente;
import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;

import java.awt.font.ShapeGraphicAttribute;

public class BilheteDAO {
    private CadastroObjetos cadastro = new CadastroObjetos(CadastroObjetos.class);

    public BilheteDAO(CadastroObjetos cadastro) {
        this.cadastro = cadastro;
    }

    private String obterIdUnico(Bilhete bilhete) {
        return bilhete.gerarNumero();
    }

    public Voo buscar(String numeroBilhete) {
        return (Voo) cadastro.buscar(numeroBilhete);

    }

    public boolean incluir(Bilhete bilhete) {
        String idUnico = obterIdUnico(bilhete);
        Voo b = buscar(idUnico);
        if (b == null) {
            cadastro.incluir(bilhete, idUnico);
            return true;
        }
        return false;
    }

    public boolean alterar(Bilhete bilhete){
        String idUnico = obterIdUnico(bilhete);
        Voo b = buscar(idUnico);
        if (b!= null) {
            cadastro.alterar(bilhete, idUnico);
            return true;
        }
        return false;
    }

    public boolean excluir(String numeroBilhete){
        Voo b = buscar(numeroBilhete);
        if (b != null) {
            cadastro.excluir(numeroBilhete);
            return true;
        }
        return false;
    }

}
