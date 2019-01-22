package com.tangbing.admin.utilstest;

/**
 * Created by admin on 2019/1/22.
 */

public class ObjectToInterger {
    //byte,short,int,char
    public static int objectToInt(Object obj){
        int i=-100;
        String str="";
        if(obj!=null){
            String string=obj.toString().trim();
            if(!isEmpty(string)){
                if(string.contains(".")){
                    str = string.substring(0, string.indexOf("."));
                    i=Integer.valueOf(str);
                }else {
                    i=Integer.valueOf(string);
                }
            }
        }
        return i;
    }
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0 || s.equals("null");
    }
}
