package com.wyf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomId {

    /**
     * 根据当前时间生成yyyyMMddHHmmss
     * @param sdf
     * @return 当前时间的yyyyMMddHHmmss格式数字
     */
    private static String getDate (String sdf) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
        return simpleDateFormat.format(date);
    }

    /**
     * 生成3个10以内的随机数
     * @param num
     * @return
     */
    private static String getRandomNum (int num) {
        String a = "";
        for (int i = 0 ; i < num ; i ++) {
            a += (int) (10 * Math.random());	/*10 * Math.random()的含义就是取10以内的随机数*/
        }
        return a;
    }

    /**
     * 整合时间和随机数
     * @return 时间和随机数的整体
     */
    public static String generatorId () {
        String sdf = "yyyyMMddHHmmss";
        int num = 3;
        return RandomId.getDate(sdf) + RandomId.getRandomNum(num);
    }
}
