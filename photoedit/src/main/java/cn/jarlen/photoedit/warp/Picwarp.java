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
package cn.jarlen.photoedit.warp;

/**
 * DESCRIBE: 图像变形
 * Created by jarlen on 2016/10/8.
 */

public class Picwarp {
    static {
        System.loadLibrary("picwarp");
    }

    public native int initArray();

    public native int[] warpPhotoFromC(int[] image, int height, int width,
                                       double max_dist, double orig_x, double orig_y, double cur_x,
                                       double cur_y);
}
