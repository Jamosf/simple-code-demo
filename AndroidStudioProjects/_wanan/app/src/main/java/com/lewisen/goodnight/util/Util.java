package com.lewisen.goodnight.util;

import android.util.Log;

/**
 * 工具类
 * Created by Lewisen on 2015/9/4.
 */
public class Util {
    private static final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

    public static void printInfo(String str) {
        Log.d("DEBUG", str);
    }

    /**
     * 选取适合显示的段落
     *
     * @param text 输入的文本
     * @param sp   选择的最小的长度
     * @return
     */
    public static String getDisText(String text, int sp) {
        String str = text.substring(0, (text.length() < 301 ? text.length() : 300))
                .replace("@@@", "").replace("###", "");

        String[] strings = str.split("\r\n");
        int len = 0;
        for (int i = 0; i < strings.length; i++) {
            len = len + strings[i].length() + 2;
            if (len > sp) break;
        }
        return str.substring(0, len - 2);
    }

    /**
     * 时间显示格式化
     *
     * @param date 输入时间 格式必须如下 2015.01.01
     * @return 输出 格式如下 Jan 01
     */
    public static String dateFormat(String date) {
        String[] strs = date.split("\\.");
        if (strs.length == 3) {
            try {
                int month = Integer.parseInt(strs[1]);
                int day = Integer.parseInt(strs[2]);
                return MONTH[month - 1] + " " + day;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return date;
        }
    }
}
