package com.tangbing.admin.myapplication;

/**
 * Created by tangbing on 2020/5/11.
 * Describe :
 */
public class JNITest {

    static {
        System.loadLibrary("MyLibrary");
    }

    public native String getString();

}
