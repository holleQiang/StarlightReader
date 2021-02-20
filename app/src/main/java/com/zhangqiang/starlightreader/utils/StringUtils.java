package com.zhangqiang.starlightreader.utils;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;

public class StringUtils {

    /**
     * 简体转繁体
     * @param string string
     * @return 繁体
     */
    public static String simple2Traditional(String string){

        try {
            return JChineseConvertor.getInstance().s2t(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 简体转繁体
     * @param string string
     * @return 简体
     */
    public static String traditional2Simple(String string){
        try {
            return JChineseConvertor.getInstance().t2s(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
