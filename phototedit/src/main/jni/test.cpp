//
// Created by hjl on 2016/8/10.
//
#include "cn_jarlen_phototedit_filter_NativeFilter.h"

JNIEXPORT jstring JNICALL Java_cn_jarlen_phototedit_filter_NativeFilter_test
        (JNIEnv *env, jobject obj){
    return env->NewStringUTF("This just a test for Android Studio NDK JNI developer!");
}