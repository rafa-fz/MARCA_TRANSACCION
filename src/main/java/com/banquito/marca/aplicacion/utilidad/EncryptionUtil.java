package com.banquito.marca.aplicacion.utilidad;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "DESede";
    private static final String SECRET_KEY = "marcaTransaccion2025"; // Debe ser de 24 caracteres

    public String encrypt(String data) throws Exception {
        byte[] keyBytes = SECRET_KEY.getBytes();
        SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}