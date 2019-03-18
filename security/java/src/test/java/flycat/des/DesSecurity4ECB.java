package flycat.des;


import org.junit.Test;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * <p>Title: DesSecurity</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @Author superbody
 * @Date 3/18/19
 */

/**
 * 说明：
 * 在DES加密中cipher是没有使用随机数的，值需要secretkey这个值就行了。
 *
 */
public class DesSecurity4ECB
{
    @Test
    public void test() throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        String plainText = "Hello , world !";
        String key = "12345678";    //要求key至少长度为8个字符

        DESKeySpec keySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);//生成des加密需要的密钥

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");//算法/模式/
        // Generating IV.
        byte[] IV = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);//通过密钥加密了
        byte[] cipherData = cipher.doFinal(plainText.getBytes());
        System.out.println("cipherText : " + new BASE64Encoder().encode(cipherData));
        //PtRYi3sp7TOR69UrKEIicA==

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainData = cipher.doFinal(cipherData);
        System.out.println("plainText : " + new String(plainData));
    }
}

