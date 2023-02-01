package com.dns.buggycrypto;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;


public class CryptoClass {

    static {
        try {
            System.loadLibrary("getkey");
        } catch (UnsatisfiedLinkError ule) {
            Log.e("HelloC", "WARNING: Could not load native library: " + ule.getMessage());
        }
    }

    //	The super secret key used by the encryption function
 //   String key = "This is the super secret key 123";
    String key = stringKeyFromJNI().trim();

    String simple_key = "This is the super secret key 123";


    String obfuscated_key = stringObfuscatedKeyFromJNI().trim();





    //	The initialization vector used by the encryption function
    byte[] ivBytes = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    String plainText;
    byte[] cipherData;
    String base64Text;
    String cipherText;


    /*
    The function that handles the aes256 encryption.
    ivBytes: Initialization vector used by the encryption function
    keyBytes: Key used as input by the encryption function
    textBytes: Plaintext input to the encryption function
    */
    public static byte[] aes256encrypt(byte[] ivBytes, byte[] keyBytes, byte[] textBytes)
            throws UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes);
    }

    /*
    The function that handles the aes256 decryption.
    ivBytes: Initialization vector used by the decryption function
    keyBytes: Key used as input by the decryption function
    textBytes: Ciphertext input to the decryption function
    */
    public static byte[] aes256decrypt(byte[] ivBytes, byte[] keyBytes, byte[] textBytes)
            throws UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes);

    }

    /*
    The function that uses the aes256 decryption function
    theString: Ciphertext input to the decryption function
    plainText: Plaintext output of the encryption operation
    */
    public String aesDeccryptedString(String theString) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO Auto-generated method stub
        byte[] keyBytes = key.getBytes("UTF-8");
        cipherData = CryptoClass.aes256decrypt(ivBytes, keyBytes, Base64.decode(theString.getBytes("UTF-8"), Base64.DEFAULT));
        plainText = new String(cipherData, "UTF-8");
        return plainText;
    }

    /*
    The function that uses the aes256 encryption function
    theString: Plaintext input to the encryption function
    cipherText: Ciphertext output of the encryption operation
    */
    public String aesEncryptedString(String theString) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO Auto-generated method stub
        byte[] keyBytes = key.getBytes("UTF-8");
        plainText = theString;
        cipherData = CryptoClass.aes256encrypt(ivBytes, keyBytes, plainText.getBytes("UTF-8"));
        cipherText = Base64.encodeToString(cipherData, Base64.DEFAULT);
        return cipherText;
    }


    public String simpleAesEncryptedString(String theString) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO Auto-generated method stub
        byte[] keyBytes = simple_key.getBytes("UTF-8");
        plainText = theString;
        cipherData = CryptoClass.aes256encrypt(ivBytes, keyBytes, plainText.getBytes("UTF-8"));
        cipherText = Base64.encodeToString(cipherData, Base64.DEFAULT);
        return cipherText;
    }


    public String obfuscatedAesEncryptedString(String theString) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO Auto-generated method stub

        byte[] keyBytes = obfuscated_key.getBytes("UTF-8");
        plainText = theString;
        cipherData = CryptoClass.aes256encrypt(ivBytes, keyBytes, plainText.getBytes("UTF-8"));
        cipherText = Base64.encodeToString(cipherData, Base64.DEFAULT);
       // System.out.println("OKOKOKOKOKOKOKOKOKOKOKOK"+tempVar);

//        String tempVar = tempFunc("someinputfromjava", 6);
//        System.out.println("tempFunc OP = "+tempVar);
        return cipherText;
    }


    public String base64JNIEncryptedString(String theString) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // TODO Auto-generated method stub

        String cipherData = tempFunc(theString, 6);

////        byte[] keyBytes = obfuscated_key.getBytes("UTF-8");
//        plainText = theString;
//        cipherData = CryptoClass.aes256encrypt(ivBytes, keyBytes, plainText.getBytes("UTF-8"));
//        cipherText = Base64.encodeToString(cipherData, Base64.DEFAULT);
//        // System.out.println("OKOKOKOKOKOKOKOKOKOKOKOK"+tempVar);

//        String tempVar = tempFunc("someinputfromjava", 6);
//        System.out.println("tempFunc OP = "+tempVar);
        return cipherData;
    }


    public native String  stringKeyFromJNI();

    public native String  stringObfuscatedKeyFromJNI();

//    public native String  tempFunc("");

    public native String tempFunc(String data,int size);




}