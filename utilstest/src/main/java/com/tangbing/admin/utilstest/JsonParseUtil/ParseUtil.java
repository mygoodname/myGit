package com.tangbing.admin.utilstest.JsonParseUtil;

import com.alibaba.fastjson.JSON;


import java.lang.reflect.Type;

/**
 * Created by admin on 2019/1/23.
 */

public class ParseUtil {
    private <T> T parse(String text, Type type) {
        T t = null;
        try {
                t= JSON.parseObject(text, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
