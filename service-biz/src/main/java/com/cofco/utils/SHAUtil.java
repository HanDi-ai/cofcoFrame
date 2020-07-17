package com.cofco.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.encoders.Base64;

public class SHAUtil {
    /**
     * RSA工具
     * @author luke
     * 2018年9月19日
     */
        /**加密算法RSA*/
        public static final String KEY_ALGORITHM = "RSA";

        /** 填充方式*/
        public static final String KEY_ALGORITHM_PADDING = "RSA/ECB/PKCS1Padding";

        /**密钥位数 1024 2048*/
        public static final int RAS_KEY_SIZE = 2048;

        /**生成没有加过密的公私钥*/
        public static Map<String, Object> createKey(String algorithm,int keysize) throws Exception {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
            keyPairGen.initialize(keysize);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            //公私钥对象存入map中
            Map<String, Object> keyMap = new HashMap<String, Object>(2);
            keyMap.put("publicKey", publicKey);
            keyMap.put("privateKey", privateKey);
            return keyMap;
        }

        /**生成加过密的公私钥 pkcs#1格式私钥 pkcs#8格式公钥*/
        public static Map<String, Object> createEncryKeyStr(String algorithm,int keysize,String privateKeyPwd) throws Exception {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
            keyPairGen.initialize(keysize);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            String pubKeyStr = 	new String(Base64.encode(publicKey.getEncoded()));
            String pkcs8Str = new String(Base64.encode(privateKey.getEncoded()));
            System.out.println(pkcs8Str);//从输出结果可以知道是PKCS#8格式的

            //System.out.println(LukeDES3.encode3Des(privateKeyPwd, privateKey.getEncoded()));

            //私钥加密
            String privateKeyStr = SHAUtil.privateKeyPwdToPKCS1(privateKey, privateKeyPwd);//使用BC加密私钥格式会被转为PKSC#1格式

            //公私钥对象存入map中
            Map<String, Object> keyMap = new HashMap<String, Object>(2);
            keyMap.put("publicKeyStr", pubKeyStr);
            keyMap.put("privateKeyStr", privateKeyStr);
            return keyMap;
        }

        /**
         * 将私钥转为PKCS#1格式私钥（加密）
         * @param privateKey
         * @param filePasswd
         * @return
         */
        private static String privateKeyPwdToPKCS1(PrivateKey privateKey,String filePasswd){
            Security.addProvider(new BouncyCastleProvider());
            StringWriter sw = new StringWriter();
            PEMWriter writer = new PEMWriter(sw);
            try {
                writer.writeObject(privateKey, "DESEDE", filePasswd.toCharArray(),
                        new SecureRandom());
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    if(writer !=null){
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sw.toString();
        }

        /**
         * 将私钥转为PKCS#1格式私钥（没有加密）
         * @param privateKey
         * @return
         */
        private static String privateKeyNoPwdToPKCS1(PrivateKey privateKey){
            Security.addProvider(new BouncyCastleProvider());
            StringWriter sw = new StringWriter();
            PEMWriter writer = new PEMWriter(sw);
            try {
                writer.writeObject(privateKey);
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    if(writer !=null){
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sw.toString();
        }

        /**
         * 将私钥字符串生成文件
         * @param privateKeyStr
         * @param filePath
         */
        public static void createPrivateKeyFile(String privateKeyStr,String filePath){
            try {
                File file = new File(filePath);
                PrintStream ps = new PrintStream(new FileOutputStream(file));
                ps.print(privateKeyStr);
                ps.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * 从pkcs#1私钥文件 得到RSA公私钥对象
         * @param filePath RSA私钥文件路径 (PEM文件已加密)
         * @param filePasswd 私钥加密秘钥
         * @return 返回RSA公私钥对象
         */
        public static Map<String,Object> getRSAPrivateKeyEncrypted(String filePath,final String filePasswd) {
            FileInputStream fis = null;
            ByteArrayInputStream bais = null;
            Map<String,Object> map = new HashMap<String, Object>();
            try {
                fis = new FileInputStream(new File(filePath));
                byte[] fileContent = new byte[fis.available()];
                fis.read(fileContent);

                Security.addProvider(new BouncyCastleProvider());
                bais = new ByteArrayInputStream(fileContent);
                PEMReader reader = new PEMReader(new InputStreamReader(bais),
                        new PasswordFinder() {
                            @Override
                            public char[] getPassword() {
                                return filePasswd.toCharArray();
                            }
                        });
                //判断文件在加密情况下，用户是否输入了密码
                reader.mark(1);
                //System.out.println(reader.markSupported());
                reader.readLine();
                String isEncrytedStr = reader.readLine();
                if(isEncrytedStr.endsWith("ENCRYPTED") && (filePasswd == null || filePasswd.equals(""))){
                    map.put("noPasswd", "私钥文件已加密，需私钥密码");
                    return map;
                }
                if(isEncrytedStr.endsWith("ENCRYPTED")){ //文件类型
                    map.put("type", "ENCRYPTED");
                }else{
                    map.put("type", "NOENCRYPTED");
                }
                reader.reset();

                KeyPair keyPair = (KeyPair) reader.readObject();
                reader.close();

                //解密后获取公钥和私钥
                PublicKey pubk = keyPair.getPublic();//pkcs#8格式公钥
                PrivateKey prik = keyPair.getPrivate();//pkcs#8格式私钥  //JCERSAPrivateCrtKey
                //System.out.println(prik.getFormat());

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                KeySpec keySpec = new X509EncodedKeySpec(pubk.getEncoded());
                KeySpec keySpec2 = new PKCS8EncodedKeySpec(prik.getEncoded());
                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
                RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec2);

                System.out.println(new String(Base64.encode(privateKey.getEncoded())));//从输出结果可以知道是PKCS#8格式的

                map.put("publicKey", publicKey);
                map.put("privateKey", privateKey);

                return map;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return map;
            }catch (Exception e) {
                e.printStackTrace();
                return map;
            }finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        public static void main(String[] args) {
            try {
                //生成加密的公私钥
                String  filePasswd = "55566645432";
                Map<String, Object> encryKeyStrMap = SHAUtil.createEncryKeyStr(SHAUtil.KEY_ALGORITHM, SHAUtil.RAS_KEY_SIZE, filePasswd);
                System.out.println(encryKeyStrMap.get("publicKeyStr"));
                System.out.println("---------------");
                System.out.println(encryKeyStrMap.get("privateKeyStr"));
                //私钥生成文件
//                String filePath = "C:\\Users\\NP0612\\Desktop\\test_priv.pem";
//                SHAUtil.createPrivateKeyFile(encryKeyStrMap.get("privateKeyStr").toString(), filePath);
//                //从私钥文件获取公私钥对象
//                SHAUtil.getRSAPrivateKeyEncrypted(filePath, filePasswd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}
