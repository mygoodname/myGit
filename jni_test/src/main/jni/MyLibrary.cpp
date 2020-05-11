//
// Created by admin on 2020/5/11.
//
#include "com_tangbing_admin_myapplication_JNITest.h"

JNIEXPORT jstring JNICALL Java_com_tangbing_admin_myapplication_JNITest_getString
  (JNIEnv *jenv, jobject){
  return (*jenv).NewStringUTF("This is mylibrary !!!");
  }
