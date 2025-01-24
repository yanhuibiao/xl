package com.xl.common.utils;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class AESUtil {
    private static final String PASSWORD="secret-key";
    private static final String SALT="960808";
    private static TextEncryptor textEncryptor = Encryptors.text(PASSWORD,SALT);

    public static String encrypt(String text){
        return textEncryptor.encrypt(text);
    }

    public static String decrypt(String text){
        return textEncryptor.decrypt(text);
    }
}
