package br.edu.cesarschool.cc.poo.ac.passagem;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VooDAO {
    private CadastroObjetos cadastro= new CadastroObjetos(Voo.class);
    public VooDAO() {

    }
    private List<Voo> voos = new ArrayList<>();
    public String obterIdUnico(Voo voo){
        return (voo.getCompanhiaAerea()+ voo.getNumeroVoo());
    }

    public Voo buscar(String idVoo){
        return (Voo)cadastro.buscar(idVoo);
    }

    public boolean incluir(Voo voo){
        String idUnico = obterIdUnico(voo);
        Voo v = buscar(idUnico);
        if (v == null) {
            cadastro.incluir(voo, idUnico);
            return true;
        }
        return false;
    }
    public boolean alterar(Voo voo){
        String idUnico = obterIdUnico(voo);
        Voo v = buscar(idUnico);
        if (v!= null) {
            cadastro.alterar(voo, idUnico);
            return true;
        }
        return false;
    }

    public boolean excluir(String idVoo){
        Voo v = buscar(idVoo);
        if (v != null) {
            cadastro.excluir(idVoo);

            return true;
        }
        return false;
    }
    public Voo[] buscarTodos() {
        Serializable[] res = voos.toArray(new Voo[0]);
        if (res == null) {
            return null;
        } else {
            Voo[] voos = new Voo[res.length];
            int i = 0;
            for (Serializable reg : res) {
                voos[i] = (Voo)reg;
                i++;
            }
            return voos;
        }
    }
}


