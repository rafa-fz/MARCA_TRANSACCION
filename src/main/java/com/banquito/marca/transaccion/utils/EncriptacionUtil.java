package com.banquito.marca.transaccion.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncriptacionUtil {

    private static final Logger log = LoggerFactory.getLogger(EncriptacionUtil.class);
    private static final String ALGORITHM = "DESede";

    @Value("${app.encryption.key}")
    private String key;

    public String encriptar3DES(String texto) {
        try {
            String keyAjustada = ajustarLongitudClave(key);

            byte[] keyBytes = keyAjustada.getBytes("UTF-8");
            SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] textoEncriptado = cipher.doFinal(texto.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(textoEncriptado);

        } catch (Exception e) {
            log.error("Error al encriptar: {}", e.getMessage());
            throw new RuntimeException("Error al encriptar datos", e);
        }
    }

    public String desencriptar3DES(String textoEncriptado) {
        try {
            String keyAjustada = ajustarLongitudClave(key);

            byte[] keyBytes = keyAjustada.getBytes("UTF-8");
            SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] textoDesencriptado = cipher.doFinal(Base64.getDecoder().decode(textoEncriptado));
            return new String(textoDesencriptado, "UTF-8");

        } catch (Exception e) {
            log.error("Error al desencriptar: {}", e.getMessage());
            throw new RuntimeException("Error al desencriptar datos", e);
        }
    }

    private String ajustarLongitudClave(String originalKey) {
        if (originalKey == null) {
            throw new IllegalArgumentException("La clave de encriptación no puede ser nula");
        }

        StringBuilder keyBuilder = new StringBuilder(originalKey);
        while (keyBuilder.length() < 24) {
            keyBuilder.append(originalKey);
        }
        return keyBuilder.substring(0, 24);
    }
}