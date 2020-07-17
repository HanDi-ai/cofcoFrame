package com.cofco.utils;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
public class PKIUtil {
    static {
        java.security.Security.addProvider(new BouncyCastleProvider());
        //org.bouncycastle.jce.provider.BouncyCastleProvider();
    }
    static Logger logger = Logger.getLogger(PKIUtil.class);
    public static String getKeyStr(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(Base64.decode(path))))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("--") && !line.endsWith("--")) {
                    sb.append(line);
                }
                line = reader.readLine();
            }
        }
        return sb.toString();
    }

    // pem
    private static String getKeyString(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            if (line.startsWith("--") || line.endsWith("--")) {
                continue;
            }
            sb.append(line);
        }
        return sb.toString();
    }

    public static PrivateKey getPrivateKeyFromFile(String path) {
        PrivateKey key = null;
        try {
            String keyString = getKeyString(path);
            byte[] all = Base64.decode(keyString);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(all);
            key = KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return key;
    }

    public static PublicKey getPublicKeyFromFile(String path) {
        PublicKey key = null;
        try {
            String keyString = getKeyString(path);
            byte[] all = Base64.decode(keyString);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(all);
            key = KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return key;
    }

    public static PublicKey getPublicKey(String publicKeyStr) throws Exception {
        byte[] all = Base64.decode(publicKeyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(all);
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(spec);
        return key;
    }

    public static PrivateKey getPrivateKey(String privateKeyStr) throws Exception {
        byte[] all = Base64.decode(privateKeyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(all);
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(spec);
        return key;
    }

    public static String decryptWithMemberPuk (String content, PublicKey publicKey) throws Exception {
        Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //log.info("解密cipher：" + cipher);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        logger.info("解密provider: {}" +cipher.getProvider().getClass().getName());
        byte[] doFinal =  cipher.doFinal(Base64 . decode( content));
        return new String(doFinal);
    }
    public static String encryptWithOwnPrk(String content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        logger.info("加密provider: {}" +cipher.getProvider().getClass().getName());
        byte[] doFinal = cipher.doFinal( content. getBytes());

        return Base64.encode(doFinal);
    }
}
