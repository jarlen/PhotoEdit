//
// Created by hjl on 2016/9/30.
//

#ifndef PHOTOEDIT_NATIVEFILTER_H
#define PHOTOEDIT_NATIVEFILTER_H
#include <jni.h>

jstring test(JNIEnv * env, jobject obj);

//绑定
JNINativeMethod gMethods[] = {
        {"test", "()Ljava/lang/String;",(void*)test}

};

#endif //PHOTOEDIT_NATIVEFILTER_H
