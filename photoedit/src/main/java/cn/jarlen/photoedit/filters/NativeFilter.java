/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.jarlen.photoedit.filters;

/**
 * DESCRIBE:
 * Created by jarlen on 2016/9/28.
 */
public class NativeFilter {
    static {
        System.loadLibrary("nativefilter");
    }

    public native String test();


    /**
     * 灰色
     * @param pixels 图像像素点集
     * @param width  图像像素点宽度
     * @param height 图像像素点高度
     * @param factor 图像滤镜变化程度( 0 < factor < 1)
     * @return
     */
    public native int[] gray(int[] pixels, int width, int height, float factor);


    /**
     *马赛克
     * @param pixels
     *            图像像素点集
     * @param width
     *            图像像素点宽度
     * @param height
     *            图像像素点高度
     * @param factor
     *            马赛克程度( 0 < factor < 30)，数值范围按算法效率而定，越小，效率越低。
     * @return
     */
    public native int[] mosatic(int[] pixels, int width, int height,
                                  int factor);

    /**
     * LOMO
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return
     */
    public native int[] lomo(int[] pixels, int width, int height, float factor);


    /**
     *怀旧效果
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return 怀旧效果
     */
    public native int[] nostalgic(int[] pixels, int width, int height,
                                    float factor);

    /**
     *漫画效果
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return 漫画效果
     */
    public native int[] comics(int[] pixels, int width, int height,
                                 float factor);

    /**
     *流年风格
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return
     */
    public native int[] brown(int[] pixels, int width, int height,
                                float factor);

    /**
     * 素描效果---铅笔画
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return
     */
    public native int[] sketchPencil(int[] pixels, int width, int height,
                                       float factor);
}
