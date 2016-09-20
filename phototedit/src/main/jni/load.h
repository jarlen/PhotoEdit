//
// Created by hjl on 2016/9/20.
//

#ifndef PHOTOEDIT_LOAD_H
#define PHOTOEDIT_LOAD_H

JNIEXPORT jintArray JNICALL Java_cn_jarlen_phototedit_filter_NativeFilter_ToGray
        (JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height, jfloat factor);

JNIEXPORT jintArray JNICALL Java_cn_jarlen_phototedit_filter_NativeFilter_ToMosatic
        (JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height, jint factor);


JNIEXPORT jintArray JNICALL Java_cn_jarlen_phototedit_filter_NativeFilter_ToBrown
        (JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height, jfloat factor);


JNINativeMethod gMethods[] = {
        { "ToGray", "([IIIF)[I", (void*) ToGray },
        {"ToMosatic", "([IIII)[I", (void*) ToMosatic },
        { "ToBrown", "([IIIF)[I",(void*) ToBrown }
};

#endif //PHOTOEDIT_LOAD_H
