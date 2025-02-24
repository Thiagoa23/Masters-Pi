package com.masterspi.util;

public class CPFValidator {
     public static boolean isValidCPF(String cpf) {
        if (cpf == null) {
            return false;
        }
        
        // Remove pontos, traços e outros caracteres não numéricos
        cpf = cpf.replaceAll("[^\\d]", "");
        
        // Verifica se possui 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Descarta CPFs com todos os dígitos iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        try {
            // Cálculo do primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                int digit = Character.getNumericValue(cpf.charAt(i));
                sum += digit * (10 - i);
            }
            int firstVerifier = (sum * 10) % 11;
            if (firstVerifier == 10 || firstVerifier == 11) {
                firstVerifier = 0;
            }
            if (firstVerifier != Character.getNumericValue(cpf.charAt(9))) {
                return false;
            }

            // Cálculo do segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                int digit = Character.getNumericValue(cpf.charAt(i));
                sum += digit * (11 - i);
            }
            int secondVerifier = (sum * 10) % 11;
            if (secondVerifier == 10 || secondVerifier == 11) {
                secondVerifier = 0;
            }
            if (secondVerifier != Character.getNumericValue(cpf.charAt(10))) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
