package flycat.rsa;

import org.junit.Test;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * <p>Title: RsaSecurity</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @Author superbody
 * @Date 3/18/19
 */
public class RsaSecurity {

    @Test
    public void test() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, SignatureException {
        String plainText = "Hello , world !";

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("rsa");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        Cipher cipher = Cipher.getInstance("rsa");
        SecureRandom random = new SecureRandom();

        cipher.init(Cipher.ENCRYPT_MODE, privateKey, random);
        byte[] cipherData = cipher.doFinal(plainText.getBytes());
        System.out.println("cipherText : " + new BASE64Encoder().encode(cipherData));


        cipher.init(Cipher.DECRYPT_MODE, publicKey, random);
        byte[] plainData = cipher.doFinal(cipherData);
        System.out.println("plainText : " + new String(plainData));
        //Hello , world !

        Signature signature  = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(cipherData);
        byte[] signData = signature.sign();
        System.out.println("signature : " + new BASE64Encoder().encode(signData));


        signature.initVerify(publicKey);
        signature.update(cipherData);
        boolean status = signature.verify(signData);
        System.out.println("status : " + status);
    }
}

