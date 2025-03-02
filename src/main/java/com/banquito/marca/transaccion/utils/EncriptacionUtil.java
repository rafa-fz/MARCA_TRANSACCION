package com.banquito.marca.transaccion.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncriptacionUtil {
    private static final String ALGORITHM = "DESede";
    private final String encryptionKey;

    public EncriptacionUtil(@Value("${app.encryption.key}") String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String encriptar3DES(String datos) {
        try {
            byte[] keyBytes = encryptionKey.getBytes();
            SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] textoEncriptado = cipher.doFinal(datos.getBytes());
            return Base64.getEncoder().encodeToString(textoEncriptado);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar datos", e);
        }
    }
} 