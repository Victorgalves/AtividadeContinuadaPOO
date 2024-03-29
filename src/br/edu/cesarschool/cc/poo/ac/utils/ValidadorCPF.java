package br.edu.cesarschool.cc.poo.ac.utils;

public class ValidadorCPF {
	private ValidadorCPF() {}
	
	 public static boolean isCpfValido(String cpf) {
        if (cpf != null && cpf.length() == 11) {
        	cpf = cpf.replaceAll("[^0-9]", "");
        	if (cpf.matches("\\d+")) {
                int soma = 0;
                for (int i = 0; i < 9; i++) {
                    soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
                }
                int resto = soma % 11;
                int digitoVerificador1 = resto < 2 ? 0 : 11 - resto;

                soma = 0;
                for (int i = 0; i < 10; i++) {
                    soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
                }
                resto = soma % 11;
                int digitoVerificador2 = resto < 2 ? 0 : 11 - resto;

                if (digitoVerificador1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digitoVerificador2 == Character.getNumericValue(cpf.charAt(10))) {
                    return true; 
                }
        	}
        }
		return false;
	 }
}
