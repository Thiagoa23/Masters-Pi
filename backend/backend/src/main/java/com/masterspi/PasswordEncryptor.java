package com.masterspi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe para gerar o hash SHA-256 de uma senha.
 * Utilize este código para obter o hash que deverá ser armazenado no banco.
 */
public class PasswordEncryptor {

    public static void main(String[] args) {
        String rawPassword = "admin123";
        String encodedPassword = encodeSHA256(rawPassword);
        System.out.println("Senha encriptada: " + encodedPassword);
    }

    public static String encodeSHA256(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Erro ao encriptar a senha", ex);
        }
    }
}
