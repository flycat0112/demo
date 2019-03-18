package flycat.aes;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * <p>Title: AesSecurity</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @Author superbody
 * @Date 3/18/19
 */
public class AesSecurity4ECB {
    @Test
    public void test() throws Exception {
        String plainText = "This is a plain text which need to be encrypted by Java AES 256 Algorithm in CBC Mode";
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        // Generate Key
        SecretKey key = keyGenerator.generateKey();

        System.out.println("Original Text  : "+plainText);

        byte[] cipherText = encrypt(plainText.getBytes(),key);
        System.out.println("Encrypted Text : "+Base64.getEncoder().encodeToString(cipherText));

        String decryptedText = decrypt(cipherText,key);
        System.out.println("DeCrypted Text : "+decryptedText);
    }

    public static byte[] encrypt (byte[] plaintext,SecretKey key) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //Cipher cipher = Cipher.getInstance("AES");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }


    public static String decrypt (byte[] cipherText, SecretKey key) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }
}

