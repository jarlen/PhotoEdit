//
// Created by jarlen on 2016/10/8.
//

#ifndef PHOTOEDIT_PICWARP_H
#define PHOTOEDIT_PICWARP_H
#include <jni.h>

jint initArray(JNIEnv *env, jobject obj);

jintArray warpPhotoFromC(JNIEnv* env, jobject obj, jintArray imagearr, jint height, jint width,
                         jdouble r, jdouble orig_x, jdouble orig_y, jdouble cur_x, jdouble cur_y);


//绑定
JNINativeMethod gMethods[] = {
        {"initArray","()I", (void *) initArray},
        {"warpPhotoFromC","([IIIDDDDD)[I",(void *) warpPhotoFromC}

};

#endif //PHOTOEDIT_PICWARP_H
