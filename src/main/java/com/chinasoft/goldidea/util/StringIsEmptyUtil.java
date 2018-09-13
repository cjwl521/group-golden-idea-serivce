package com.chinasoft.goldidea.util;

import org.springframework.util.StringUtils;

/**
 * 判空类
 */
public class StringIsEmptyUtil {
    public static Boolean isNotEmpty(String object){
        return !StringUtils.isEmpty(object);
    }

    public static Boolean isEmpty(String object){
        return StringUtils.isEmpty(object);
    }
}
