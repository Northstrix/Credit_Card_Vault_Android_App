package com.example.unlock;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class tdes_in_cbc {
    protected static byte[] key_for_tdes = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18
    };

    public static String encrypt_3des_in_cbc(String input) {
        try {
            // Create a key specification from the provided key bytes
            KeySpec keySpec = new DESedeKeySpec(key_for_tdes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey key = keyFactory.generateSecret(keySpec);

            // Create an initialization vector (IV) for added security
            byte[] ivBytes = new byte[8]; // You may use a more secure way to generate IV
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            // Create a Cipher instance for encryption
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

            // Encrypt the input string
            byte[] encryptedBytes = cipher.doFinal(input.getBytes("UTF-8"));

            // Encode the encrypted bytes to a base64 string for easy storage and transmission
            return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt_3des_in_cbc(String encryptedInput) {
        try {
            // Create a key specification from the provided key bytes
            KeySpec keySpec = new DESedeKeySpec(key_for_tdes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey key = keyFactory.generateSecret(keySpec);

            // Create an initialization vector (IV) for decryption
            byte[] ivBytes = new byte[8]; // You must use the same IV as used during encryption
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            // Create a Cipher instance for decryption
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            // Decode the base64 string into bytes and then decrypt
            byte[] encryptedBytes = android.util.Base64.decode(encryptedInput, android.util.Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Convert the decrypted bytes back to a string
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void set_key(byte[] hashedBytes){
        for (int i = 0; i < 24; i++){
            key_for_tdes[i] = hashedBytes[i];
        }
    }
}
