#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <NativeFilter.h>
#include <color.h>
#include <math.h>

//指定要注册的类
#define JNIREG_CLASS "cn/jarlen/photoedit/filters/NativeFilter"

int registerNativeMethods(JNIEnv *env, const char *className,
                          JNINativeMethod *gMethods, int numMethods) {
    jclass clazz = 0;

    clazz = env->FindClass(className);
    if (clazz == 0) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

int registerNatives(JNIEnv *env) {
    if (!registerNativeMethods(env, JNIREG_CLASS, gMethods, sizeof(gMethods) / sizeof(gMethods[0])))
        return JNI_FALSE;

    return JNI_TRUE;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = 0;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);

    if (!registerNatives(env)) {
        return -1;
    }
    /* success -- return valid version number */
    result = JNI_VERSION_1_4;

    return result;
}


jstring test(JNIEnv *env, jobject obj) {
    return (*env).NewStringUTF("This is native filter !!!");
}

jintArray gray(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                 jfloat factor) {

    jint * pixels = 0;
    pixels = env->GetIntArrayElements(srcPixels, JNI_FALSE);
    if (pixels == 0) {
        return srcPixels;
    }

    int size = width * height;

    int result[size];

    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            int index = i * height + j;

            int a0 = alpha(pixels[index]);
            int r0 = red(pixels[index]);
            int g0 = green(pixels[index]);
            int b0 = blue(pixels[index]);

            int grayColor = (299 * r0 + 587 * g0 + 114 * b0) / 1000;

            result[index] = ARGB(a0, grayColor, grayColor, grayColor);
        }
    }

    jintArray resultArray = env->NewIntArray(size);
    env->SetIntArrayRegion(resultArray, 0, size, result);
    env->ReleaseIntArrayElements(srcPixels, pixels, 0);
    return resultArray;
}

jintArray mosatic(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                    jint factor) {
    jint * pixels = NULL;

    pixels = env->GetIntArrayElements(srcPixels, JNI_FALSE);
    if (pixels == NULL) {
        return srcPixels;
    }

    int size = width * height;
    int result[size];

    int a = factor;
    int b = a * height / width;

    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            int x = i - i % a + a / 2;

            x = x > width ? width : x;

            int y = j - j % b + b / 2;
            y = y > height ? height : y;

            int curentColor = pixels[x + y * width];
            result[i + j * width] = curentColor;
        }
    }
    jintArray resultArray = env->NewIntArray(size);
    env->SetIntArrayRegion(resultArray, 0, size, result);
    env->ReleaseIntArrayElements(srcPixels, pixels, 0);
    return resultArray;
}

jintArray lomo(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                 jfloat factor) {
    jint * pixels = NULL;

    pixels = env->GetIntArrayElements(srcPixels, JNI_FALSE);
    if (pixels == NULL) {
        return srcPixels;
    }

    int size = width * height;
    int result[size];

    int ratio =
            width > height ? height * 32768 / width : width * 32768 / height;
    int cx = width >> 1;
    int cy = height >> 1;
    int max = cx * cx + cy * cy;
    int min = (int) (max * (1 - 0.8f));
    int diff = max - min;

    for (int i = 0; i < height; i++) {

        for (int j = 0; j < width; j++) {
            int current_color = pixels[i * width + j];

            int r0 = red(current_color);
            int g0 = green(current_color);
            int b0 = blue(current_color);
            int a0 = alpha(current_color);

            int value = r0 < 128 ? r0 : 256 - r0;
            int newR = (value * value * value) / 64 / 256;
            newR = r0 < 128 ? newR : 255 - newR;

            value = g0 < 128 ? g0 : 256 - g0;
            int newG = (value * value) / 128;
            newG = (g0 < 128 ? newG : 255 - newG);

            int newB = b0 / 2 + 0x25;

            /*****************边缘黑暗**************/
            int dx = cx - j;
            int dy = cy - i;

            if (width > height) {
                dx = (dx * ratio) >> 15;
            } else {
                dy = (dy * ratio) >> 15;
            }

            int distSq = dx * dx + dy * dy;

            if (distSq > min) {
                int v = ((max - distSq) << 8) / diff;
                v *= v;

                int ri = (int) (newR * v) >> 16;
                int gi = (int) (newG * v) >> 16;
                int bi = (int) (newB * v) >> 16;

                newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
            }
            /**********************边缘黑暗end*****************/
            current_color = ARGB(a0, newR, newG, newB);
            result[i * width + j] = current_color;
        }
    }

    jintArray resultArray = env->NewIntArray(size);
    env->SetIntArrayRegion(resultArray, 0, size, result);
    env->ReleaseIntArrayElements(srcPixels, pixels, 0);
    return resultArray;
}

jintArray nostalgic(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                      jfloat factor) {
    jint * pixels = NULL;

    pixels = env->GetIntArrayElements(srcPixels, JNI_FALSE);
    if (pixels == NULL) {
        return srcPixels;
    }

    int size = width * height;
    int result[size];

    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            int index = i * height + j;

            int a0 = alpha(pixels[index]);
            int r0 = red(pixels[index]);
            int g0 = green(pixels[index]);
            int b0 = blue(pixels[index]);

            int r = 0.393 * r0 + 0.769 * g0 + 0.189 * b0;
            int g = 0.349 * r0 + 0.686 * g0 + 0.168 * b0;
            int b = 0.272 * r0 + 0.534 * g0 + 0.131 * b0;

            r = Min(255, Max(r, 0));
            g = Min(255, Max(g, 0));
            b = Min(255, Max(b, 0));

            result[index] = ARGB(a0, r, g, b);
        }
    }

    jintArray resultArray = env->NewIntArray(size);
    env->SetIntArrayRegion(resultArray, 0, size, result);
    env->ReleaseIntArrayElements(srcPixels, pixels, 0);
    return resultArray;
}

jintArray comics(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                   jfloat factor) {
    jint * pixels = NULL;

    pixels = env->GetIntArrayElements(srcPixels, JNI_FALSE);
    if (pixels == NULL) {
        return srcPixels;
    }

    int size = width * height;
    int result[size];

    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            int index = i * width + j;

            int a0 = alpha(pixels[index]);
            int r0 = red(pixels[index]);
            int g0 = green(pixels[index]);
            int b0 = blue(pixels[index]);

            int r = abs(g0 - b0 + g0 + r0) * r0 / 256;
            int g = abs(b0 - g0 + b0 + r0) * r0 / 256;
            int b = abs(b0 - g0 + b0 + r0) * g0 / 256;

            g = g > 255 ? 255 : (g < 0 ? 0 : g);
            r = r > 255 ? 255 : (r < 0 ? 0 : r);
            b = b > 255 ? 255 : (b < 0 ? 0 : b);

            int grayColor = (int) (299 * r + 587 * g + 114 * b) / 1000;
            grayColor = Min(255, Max(grayColor, 0));

            result[index] = ARGB(a0, grayColor, grayColor, grayColor);
        }
    }

    jintArray resultArray = env->NewIntArray(size);
    env->SetIntArrayRegion(resultArray, 0, size, result);
    env->ReleaseIntArrayElements(srcPixels, pixels, 0);
    return resultArray;
}

jintArray brown(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height,
                  jfloat factor) {
    jint * cbuf;
    cbuf = env->GetIntArrayElements(srcPixels, JNI_FALSE);

    if (cbuf == NULL) {
        return 0;
    }

    int size = width * height;
    int buffTemp[size];

    float brownMatrix[4][4] = {
            {0.3588, 0.299, 0.2392, 0},
            {0.7044, 0.587, 0.4696, 0},
            {0.1368, 0.114, 0.0912, 0},
            {0,      0,     0,      1}
    };

    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            int currentColor = cbuf[j * width + i];

            int a = alpha(currentColor);
            int r = red(currentColor);
            int g = green(currentColor);
            int b = blue(currentColor);

            int colorTemp[4];
            int resultColor[4] = {0};


            colorTemp[0] = r;
            colorTemp[1] = g;
            colorTemp[2] = b;
            colorTemp[3] = a;

            matrix1X4(colorTemp, brownMatrix, resultColor);

            int temp = ARGB(resultColor[3], resultColor[0], resultColor[1], resultColor[2]);

            buffTemp[j * width + i] = temp;

        }
    }

    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, buffTemp);
    env->ReleaseIntArrayElements(srcPixels, cbuf, 0);
    return result;
}

jintArray sketchPencil(JNIEnv *env, jobject obj, jintArray srcPixels, jint width, jint height) {

    jint * cbuf;
    cbuf = env->GetIntArrayElements(srcPixels, JNI_FALSE);
    if (cbuf == NULL) {
        return 0;
    }

    int newSize = width * height;
    jint rbuf[newSize]; // 新图像像素值

    // 先对图象的像素处理成灰度颜色后再取反

    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {

            int curr_color = cbuf[j * width + i];
            int color_row = cbuf[j * width + i + 1];
            int color_col = cbuf[(j + 1) * width + i];

            //原图像素
            int r0 = red(curr_color);
            int g0 = green(curr_color);
            int b0 = blue(curr_color);

            // 同行像素
            int r1 = red(color_row);
            int g1 = green(color_row);
            int b1 = blue(color_row);

            //同列像素
            int r2 = red(color_col);
            int g2 = green(color_col);
            int b2 = blue(color_col);

            //梯度处理，产生霓虹效果
            int r = 2 * sqrt((r0 - r1) * (r0 - r1) + (r0 - r2) * (r0 - r2));
            int g = 2 * sqrt((g0 - g1) * (g0 - g1) + (g0 - g2) * (g0 - g2));
            int b = 2 * sqrt((b0 - b1) * (b0 - b1) + (b0 - b2) * (b0 - b2));

            //反色处理
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            // 灰度化
            r = 0.299 * r + 0.587 * g + 0.114 * b;
            g = r;
            b = r;

            int a = alpha(curr_color);
            int modif_color = ARGB(a, r, g, b);
            rbuf[j * width + i] = modif_color;
        }
    }

    jintArray result = env->NewIntArray(newSize);
    env->SetIntArrayRegion(result, 0, newSize, rbuf);
    env->ReleaseIntArrayElements(srcPixels, cbuf, 0);
    return result;
}