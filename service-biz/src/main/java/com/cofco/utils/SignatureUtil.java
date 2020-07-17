package com.cofco.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;
public class SignatureUtil {
    private static final Logger Log = LoggerFactory.getLogger(SignatureUtil.class);
    private static final String NONCE = "nonce";
    private static final String SIGNATURE = "sign";

    //有效的PKI签名
    public static boolean validPkiSign(Object obj, PublicKey publicKey) {
        try {
            Map<String, String> orderedParameters = orderedParameters(obj);
            String sign = getAndRemove(orderedParameters, SIGNATURE);
            String preFirstHash = genOrderedString(orderedParameters);
            Log.info("preFirstHash###{}", preFirstHash);
            String fHash = DigestUtils.sha256Hex(preFirstHash);
            String signDecoded = PKIUtil.decryptWithMemberPuk(sign, publicKey);
            return StringUtils.equals(fHash, signDecoded);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //有效的Reg签名
    public static boolean validRegSign(Object obj) {
        try {
            Map<String, String> orderedParameters = orderedParameters(obj);
            String sign = getAndRemove(orderedParameters, SIGNATURE);
            String nonce = orderedParameters.get(NONCE);

            String preFirstHash = genOrderedString(orderedParameters);
            String fristHash = DigestUtils.sha256Hex(preFirstHash);
            String preSecondHash = fristHash + "&nonce=" + nonce;
            String signx = DigestUtils.sha256Hex(preSecondHash);
            return StringUtils.equals(sign, signx);
        } catch (Exception e) {
            Log.error(e.toString());
            return false;
        }
    }

   /* public static String genRegSign(Object obj) {
        try {
            SortedMap<String, String> orderedParameters = orderedParameters(obj);
            return genRegSignSortedMap(orderedParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }*/
    //按照sha256生成一个摘要
    /*public static String genRegSignSortedMap(SortedMap<String, String> sortedMap) {
        String nonce = sortedMap.get(NONCE);
        String preFirstHash = genOrderedString(sortedMap);
        String firstHash = DigestUtils.sha256Hex(preFirstHash);
        String preSecondHash = firstHash + "&nonce=" + nonce;
        String signx = DigestUtils.sha256Hex(preSecondHash);
        return signx;
    }*/

    /**
     * Entity转Map
     * @param object
     * @return
     */
    public static TreeMap entityToMap(Object object){
        TreeMap<String, String> map = new TreeMap<>();
        if (object == null) {
            return null;
        }
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), String.valueOf(field.get(object)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //按照ascii升序排序生成stringA字符串(TreeMap)
    public static String genOrderedString(TreeMap<String, String> orderedParameters) {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = orderedParameters.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = String.valueOf(orderedParameters.get(key));
            //if (StringUtils.isNotBlank(value)) {
            if (value != null && !value.equals("")) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    //按照ascii升序排序生成stringA字符串(SortedMap)
    public static String genOrderedString(SortedMap<String, String> orderedParameters) {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = orderedParameters.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = String.valueOf(orderedParameters.get(key));
            //if (StringUtils.isNotBlank(value)) {
            if (value != null && !value.equals("")) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 将二进制数组用BASE64编码
     * @author handi
     * @date 2019/10/31 16:58:15
     * @param stringA 按照ascii升序排序生成的字符串
     * @return sign 数字签名
     */
    //按照sha256算法生成sign
    public static String genRegSignSortedMap(String stringA,String nonce) {
        //2、将stringA进行SHA256为stringDigestA
        String stringDigestA = DigestUtils.sha256Hex(stringA);
        //3、生成随机字符串
        //String nonce= RandomStringUtils.randomAlphanumeric(20);
        //4、stringDigestA&nonce=nonce的实际值拼接为字符串stringB
        String stringB = stringDigestA + "&nonce=" + nonce;
        //5、将stringB进行SHA256得到sign
        String sign = DigestUtils.sha256Hex(stringB);
        return sign;
    }

    public static String genOrderedString(Object obj) throws Exception {
        Map<String, String> orders = orderedParameters(obj);
        return genOrderedString(orders);
    }


    public static SortedMap<String, String> orderedParameters(Object obj) throws Exception {
        SortedMap<String, String> res = new TreeMap<>();
        Class<?> klass = obj.getClass();
        for (; klass != Object.class; klass = klass.getSuperclass()) {
            Field[] fields = klass.getDeclaredFields();
            for (Field f : fields) {
                String name = f.getName();
                String namex = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method;
                try {
                    method = klass.getMethod("get" + namex, new Class<?>[]{});
                } catch (NoSuchMethodException | SecurityException e) {
                    method = klass.getMethod("is" + namex, new Class<?>[]{});
                }

                Object value = method.invoke(obj, new Object[]{});
                if (value == null) {
                    continue;
                }
                if (value instanceof Date) {
                    //SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMATE);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    res.put(name, sdf.format(value));
                } else {
                    res.put(name, String.valueOf(value));
                }
            }
        }
        return res;
    }

    private static String getAndRemove(Map<String, String> params, String key) {
        String sign = params.get(key);
        params.remove(key);
        return sign;
    }

}
