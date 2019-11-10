package com.baihang.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAHelper {
    private static final String CHARSET = "UTF-8";
    public static void log(String s){
      //  System.out.println(s);
    }
    /**
     *   AES解密
     * @param encryptBytes
     * @param decryptKey 解密密钥
     * @return
     * @throws Exception
     */

    public static  String aesDecryptByBytes(byte[] encryptBytes, String decryptKey)throws  Exception{
        KeyGenerator keyGenerator= KeyGenerator.getInstance("AES");
        keyGenerator.init(128,new SecureRandom(decryptKey.getBytes()));
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(),"AES"));
        byte[] deryptBytes = cipher.doFinal(encryptBytes);
        return  new String(deryptBytes);
    }

    /**
     * 将base 64 code进行AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return
     */
    public  static String aesDecrypt(String encryptStr, String decryptKey)throws  Exception{
        log(encryptStr);
        log(decryptKey);
        return  encryptStr == null ? null:aesDecryptByBytes(Base64.decodeBase64(encryptStr),decryptKey);
    }

    /**
     * 公钥解密
     */
    public static String publicDecrypt(String content, String pubKey) throws Exception {

        //生成公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        //初始化解密
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(content));
        return new String(result,CHARSET);
    }
    /**
     * 公钥解密
     */
    public static String publicDecrypt(String content, PublicKey publicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        //初始化解密
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(content));
        return new String(result,CHARSET);
    }

    /**
     * 私钥加密
     */
    public static String privateEncrypt(String content, String priKey) throws Exception {
        //生成私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
        Cipher cipher = Cipher.getInstance("RSA");
        //初始化加密
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(content.getBytes(CHARSET));

        return Base64.encodeBase64String(result);
    }
    /**
     * 私钥加密
     */
    public static String privateEncrypt(String content, PrivateKey privateKey) throws Exception {
        //Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
        Cipher cipher = Cipher.getInstance("RSA");
        //初始化加密
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(content.getBytes(CHARSET));

        return Base64.encodeBase64String(result);
    }

    //=======================公钥加密，私钥解密


    /**
     * 公钥加密
     *
     * @param content
     * @param pubKey
     * @return
     */
    public static String publicEncrypt(String content, String pubKey) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        //初始化加密
        //Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //加密字符串
        byte[] result = cipher.doFinal(content.getBytes(CHARSET));
        return Base64.encodeBase64String(result);
    }
    /**
     * 公钥加密
     *
     * @param content
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String content, PublicKey publicKey) throws Exception {
        //初始化加密
        //Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //加密字符串
        byte[] result = cipher.doFinal(content.getBytes(CHARSET));
        return Base64.encodeBase64String(result);
    }


    /**
     * 私钥解密
     */
    public static String privateDecrypt(String content, String priKey) throws Exception {
        //生成公钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        //初始化解密
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //解密字符串
        byte[] result = cipher.doFinal(Base64.decodeBase64(content));
        return new String(result,CHARSET);
    }
    /**
     * 私钥解密
     */
    public static String privateDecrypt(String content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        //初始化解密
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //解密字符串
        byte[] result = cipher.doFinal(Base64.decodeBase64(content));
        return new String(result,CHARSET);
    }


    public static void main(String[] args) throws Exception {
        String content = "d7b85f6e214abcda";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAInQsmdq6LxZxtC4KLsMiQvkM9DHia0fya9gVx3R+M8WpaZjrYhJ+lQwMrooQvYSecaFqKWz9Z7KPS7Fm2k7qkzyKoAw4pmtYQLDLmBNbfmlffVFn+2KxMda/siE9sqET2udMLqmaJGNv8DjFU0Elxbua68JaJsedmEkOInfL0h7AgMBAAECgYAvkRTClSfXOrVgfO+x/9WdgRpRSU2/r2URXxMye7KA57QtBeJzU/VO8YPQtOzHugDGAIQBDWgNcohDiHjnjWxtuM5GVwkcjx9zbCu2rAmdFCM3ND+dQBYx9VKkTaRYtI+FNH/I9pS+ZoNj0N/6yGLlcjqLKt7g1VmobKybVW1g4QJBALybfH43C0C0KqUGxhNC1xrC6KNQUHf45ahdDhsrVyaRVVAC7dh1b2BCL3FngL+DFF/VdHhC1ACxjDoe4wA4r4cCQQC7DxeOdeqXRhk4SPqzIDZZp65vFD+BH/gzx4KMGg64r86jrZRqvpwNVCpRFkjB3h5LaCDiEQwx+em2TM51ehRtAkB3HkB3Om559BAG4AtTeOB//1+MXZok22a4eeUB1dKaAin+eYscLbck9UgfeH5A9hl1GUwQ9CwPMBNfVciKiEKbAkBRx6tY0Ryavj7QaLeSEIcycyjDz2pkHYH5wUPF1K6S9+mzjXPRpzu6aA2IHeH5QA6mkQOvUhyuzL/sv2+T1KeRAkEApg/sweoNtOjTLCy1yVoo9DNJJfnAV7iG8LebfxUfA+gW7B7tWo4VDUSDgBwzWSPAcgQkBgiMepqr52Iu0XQ2yA==";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ0LJnaui8WcbQuCi7DIkL5DPQx4mtH8mvYFcd0fjPFqWmY62ISfpUMDK6KEL2EnnGhails/Weyj0uxZtpO6pM8iqAMOKZrWECwy5gTW35pX31RZ/tisTHWv7IhPbKhE9rnTC6pmiRjb/A4xVNBJcW7muvCWibHnZhJDiJ3y9IewIDAQAB";

//        String encode = publicEncrypt("张三sas水电费水电费水电费水电费水电费水电费水电费方式大V销售单位为咔咔咔咔咔咔扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩扩dasdasdd",publicKey);
        String pid = publicEncrypt("360782199010171715",publicKey);
        String name = publicEncrypt("刘复源",publicKey);
        System.out.println("pid="+pid);
        System.out.println("name="+name);
        System.out.println("____________________*********************");
//        String decode = publicDecrypt(encode,publicKey);
//        System.out.println(encode+":"+decode);
        System.out.println("decrpid="+privateDecrypt(pid,privateKey));
        System.out.println("decrnam="+privateDecrypt(name,privateKey));

//        System.out.println("decrpid1="+privateDecrypt(pid,privateKey));
//        System.out.println("decrnam1="+privateDecrypt(name,privateKey));
        String ecasdas = new String(Base64.encodeBase64String("Gvdnuc1YHIDEHPR6iSb4zWg+AbmDXpsQ".getBytes()));
        System.out.println("ecasdas="+ecasdas);

    }

}
