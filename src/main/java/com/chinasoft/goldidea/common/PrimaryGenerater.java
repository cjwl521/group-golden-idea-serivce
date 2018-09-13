package com.chinasoft.goldidea.common;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author: Created by Jarries
 * @description:
 * @date: 2018/9/5
 */
@Slf4j
public class PrimaryGenerater {

    private static final String SERIAL_NUMBER = "XXXX"; // 流水号格式
    private static PrimaryGenerater primaryGenerater = null;

    private PrimaryGenerater () {
    }

    /**
     * 取得PrimaryGenerater的单例实现
     *
     * @return
     */
    public static PrimaryGenerater getInstance () {
        if (primaryGenerater == null) {
            synchronized (PrimaryGenerater.class) {
                if (primaryGenerater == null) {
                    primaryGenerater = new PrimaryGenerater ();
                }
            }
        }
        return primaryGenerater;
    }

    private static Random rand;

    static {
        rand = new Random ();
    }

    /**
     * 生成一批用户id
     * <br>生成规则：
     * 字母加数字的固定5位，前三位为小写字母，后两位为数字
     *
     * @param oldUserIds 系统中原有的用户id列表，避免重复
     * @return
     */
    public String getUserIds (List <String> oldUserIds) {
        String ids = "";
        StringBuffer sb = new StringBuffer ();
        for (int j = 1; j <= 5; j++) {
            if (j <= 3) {//前三位获取字母
                sb.append (getLetter ());
            } else {//后两位用数字
                sb.append (getNum ());
            }
        }
        String userName = sb.toString ();
        if (oldUserIds.contains (userName) || ids.contains (userName)) {
            ids = this.getUserIds (oldUserIds);
        } else {
            ids = userName;
        }
        return ids;
    }

    /**
     * 生成一批密码
     * <br>生成规则：
     * 大写字母+小写字母+数字
     * @param wordNum 要生成的密码长度是多少
     * @return
     */
    public String getPasswords (Integer wordNum) {
        int total = wordNum;//密码总位数
        StringBuffer sb = new StringBuffer ();
        int upperNum = getRadomInt (1, total - 2);//大写字母位数，保留至少两位，用来放小写和数字
        int lowerNum = getRadomInt (1, total - upperNum - 1);//小写字母位数，为总数减去大写字母占用的数量，再为数字区域保留至少1
        //随机获取到每个类型的位置index
        Map <Integer, String> indexMap = new HashMap <Integer, String> ();
        while (indexMap.size () < upperNum) {
            //确定大写字母的索引号
            int rint = getRadomInt (0, total - 1);
            if (indexMap.get (rint) == null) {
                indexMap.put (rint, "upper");
            }
        }
        while (indexMap.size () < upperNum + lowerNum) {
            //确定小写字母的索引号
            int rint = getRadomInt (0, total - 1);
            if (indexMap.get (rint) == null) {
                indexMap.put (rint, "lower");
            }
        }
        while (indexMap.size () < total) {
            //确定数字的索引号
            int rint = getRadomInt (0, total - 1);
            if (indexMap.get (rint) == null) {
                indexMap.put (rint, "nnum");
            }
        }
        //组装密码
        for (int i = 0; i < total; i++) {
            if ("upper".equals (indexMap.get (i))) {
                sb.append (getUpper ());
            } else if ("lower".equals (indexMap.get (i))) {
                sb.append (getLetter ());
            } else {
                sb.append (getNum ());
            }
        }
        return sb.toString ();
    }

    /**
     * 随机获取一个小写字母
     */
    public static char getLetter () {
        char c = (char) getRadomInt (97, 122);
        return c;
    }

    /**
     * 随机获取一个大写字母
     */
    public static char getUpper () {
        char c = (char) getRadomInt (65, 90);
        return c;
    }

    /**
     * 随机获取一个0-9的数字
     *
     * @return
     */
    public static int getNum () {
        return getRadomInt (0, 9);
    }

    /**
     * 获取一个范围内的随机数字
     *
     * @return
     */
    public static int getRadomInt (int min, int max) {
        return rand.nextInt (max - min + 1) + min;
    }


}