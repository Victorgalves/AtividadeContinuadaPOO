package br.edu.cesarschool.cc.poo.ac.passagem;

import br.edu.cesarschool.cc.poo.ac.utils.StringUtils;

import java.util.Arrays;

public class VooMediator {
    private static VooMediator instancia = new VooMediator();
    private VooDAO vooDAO = new VooDAO();

    private VooMediator() {
    }

    public static VooMediator obterInstancia() {
        return instancia;
    }

    public Voo buscar(String IdVoo) {

        return vooDAO.buscar(IdVoo);
    }

    public String validarCiaNumero(String companhiaAerea, int numeroVoo) {
        if (StringUtils.isVaziaOuNula(companhiaAerea) || companhiaAerea.length() != 2) {
            return "CIA aerea errada";
        }

        if (numeroVoo <= 0 || numeroVoo < 1000 || numeroVoo > 9999) {
            return "Numero voo errado";
        }
        return null;
    }

    public String validar(Voo voo) {
        final String[] aeroportosValidos = {"GRU", "CGH", "GIG", "SDU", "REC", "CWB", "POA", "BSB", "SSA", "FOR", "MAO", "SLZ", "CNF", "BEL", "JPA", "PNZ",
                "CAU", "FEN", "SET", "NAT", "PVH", "BVB", "FLN", "AJU", "PMW", "MCZ", "MCP", "VIX", "GYN", "CGB", "CGR", "THE", "RBR", "VCP", "RAO"};

        if (StringUtils.isVaziaOuNula(voo.getAeroportoOrigem()) || !Arrays.asList(aeroportosValidos).contains(voo.getAeroportoOrigem())) {
            return "Aeroporto origem errado";
        }

        if (StringUtils.isVaziaOuNula(voo.getAeroportoDestino()) || !Arrays.asList(aeroportosValidos).contains(voo.getAeroportoDestino())) {
            return "Aeroporto destino errado";
        }

        if (voo.getAeroportoOrigem().equals(voo.getAeroportoDestino())) {
            return "Aeroporto origem igual a aeroporto destino";
        }
        return validarCiaNumero(voo.getCompanhiaAerea(), voo.getNumeroVoo());

    }

    public String incluir(Voo voo){
        String validar=validar(voo);
        if (validar!=null) {
            return validar;
        } else{
            if (vooDAO.incluir(voo)==false){
                return "Voo ja existente";
            } return null;
        }
    }

    public String alterar(Voo voo){
        String validar=validar(voo);
        if (validar!=null){
            return validar;
        }
        boolean alterarSucesso = vooDAO.alterar(voo);
            if(!alterarSucesso){
                return "Voo inexistente";
            } return null;

    }

    public String excluir(String idVoo){
        if(StringUtils.isVaziaOuNula(idVoo)){
            return "Id voo errado";
        } else{
            if(vooDAO.excluir(idVoo)==false){
                return "Voo inexistente";
            } return null;
        }
    }
}