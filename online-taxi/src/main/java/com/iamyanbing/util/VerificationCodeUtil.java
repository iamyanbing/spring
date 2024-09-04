package com.iamyanbing.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class VerificationCodeUtil {

    /**
     * @param size 指定验证码包含的字母个数
     */
    public static void obtain(int size) {
        String sourceChar = "abcdefghigklmnopqrstuvwxyz";
        char[] m = sourceChar.toCharArray();
        String resultString = "";
        for (int i = 0; i < size; i++) {
            char temp = m[new Random().nextInt(26)];

            resultString = resultString + temp;

        }
        log.info(resultString);
    }

    /**
     * @param size 指定验证码包含的字母个数
     */
    public static void obtainByMath(int size) {
        // 生成验证码
        double mathRandom = (Math.random() * 9 + 1) * (Math.pow(10, size - 1));
        log.info("{}", mathRandom);
        int resultInt = (int) mathRandom;
        log.info("{}", resultInt);
    }

    public static void main(String[] args) {
        VerificationCodeUtil.obtain(6);
        VerificationCodeUtil.obtainByMath(8);
    }
}
