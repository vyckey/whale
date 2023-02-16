package com.hoily.service.whale.infrastructure.common.utils;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/16 11:58
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static String trim(String string, char c) {
        int len = string.length();
        char[] chars = string.toCharArray();
        int st = 0;
        while ((st < len) && (chars[st] == c)) {
            st++;
        }
        while ((st < len) && (chars[len - 1] == c)) {
            len--;
        }
        return ((st > 0) || (len < chars.length)) ? string.substring(st, len) : string;
    }
}
