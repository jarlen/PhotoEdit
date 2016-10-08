//
// Created by jarlen on 2016/9/30.
//

#ifndef PHOTOEDIT_NATIVEFILTER_H
#define PHOTOEDIT_NATIVEFILTER_H

#include <jni.h>

jstring test(JNIEnv *env, jobject obj);

jintArray gray(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                 jfloat factor);

jintArray mosatic(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                    jint factor);

jintArray lomo(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                 jfloat factor);

jintArray nostalgic(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                 jfloat factor);

jintArray comics(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                      jfloat factor);

jintArray brown(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                   jfloat factor);

jintArray sketchPencil(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height);

//绑定
JNINativeMethod gMethods[] = {
        {"test",      "()Ljava/lang/String;", (void *) test},
        {"gray",    "([IIIF)[I",            (void *) gray},
        {"mosatic", "([IIII)[I",            (void *) mosatic},
        {"lomo",    "([IIIF)[I",            (void *) lomo},
        {"nostalgic","([IIIF)[I",           (void *) nostalgic},
        {"comics",    "([IIIF)[I",            (void *) comics},
        {"brown",    "([IIIF)[I",            (void *) brown},
        {"sketchPencil",    "([IIIF)[I",            (void *) sketchPencil}

};

#endif //PHOTOEDIT_NATIVEFILTER_H
