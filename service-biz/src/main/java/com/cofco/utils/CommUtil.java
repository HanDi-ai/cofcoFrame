package com.cofco.utils;

import java.util.Random;

/**
 * 共通方法
 * @author liayng
 * @date 2019-11-07 17:25:27
 */
public class CommUtil {

    /**
     * 随机数生成
     * @param length
     * @return
     */
    public static String getNonce(int length) {
        StringBuffer pwd = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                pwd.append((char) (choice + random.nextInt(26)));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                pwd.append(random.nextInt(10));
            }
        }
        return pwd.toString();
    }
}
