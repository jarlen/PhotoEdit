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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.jarlen.photoedit.R;

/**
 * DESCRIBE: 图像变形
 * Created by jarlen on 2016/10/8.
 */

public class WarpView extends View {

    private Bitmap mBmp;
    private int[] image;
    private int first = 0;
    private int[] colorR;
    private int[] colorG;
    private int[] colorB;
    private Bitmap newBmp;
    private boolean fg = true;
    private Context context;

    private static final int DEFAULT_PAINT_FLAGS = Paint.FILTER_BITMAP_FLAG
            | Paint.DITHER_FLAG;
    Paint mPaint = new Paint(DEFAULT_PAINT_FLAGS);

    public static int HWPPQ = 110;
    public static int MMQFJ = 120;

    private Picwarp warp = new Picwarp();

    private int MODE = MMQFJ;


    private double orig_x, orig_y;
    private double mou_dx, mou_dy;
    private double max_dist, max_dist_sq;
    private int width;
    private int height;
    private int count = 0;
    private double mou_dx_norm;
    private double mou_dy_norm;

    private float scale;
    private RectF dest;
    private double move_x, move_y;
    private int dist = (int) getResources().getDimension(R.dimen.max_dist);
    private int line_height = (int) getResources().getDimension(
            R.dimen.warp_line);

    public WarpView(Context context) {
        super(context);
    }

    public WarpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        dest = new RectF(0, 0, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (fg) {
            int viewWidht = getWidth();
            int viewHeight = getHeight();
            float scale1 = (float) width / (float) viewWidht;
            float scale2 = (float) height / (float) viewHeight;
            scale = scale1 > scale2 ? scale1 : scale2;
            int xoffset = (viewWidht - (int) (width / scale)) / 2;
            int yoffset = (viewHeight - (int) (height / scale)) / 2;
            dest.set(xoffset, yoffset, (int) (width / scale) + xoffset,
                    (int) (height / scale) + yoffset);

            canvas.drawBitmap(mBmp, null, dest, mPaint);

        } else {
            canvas.drawBitmap(newBmp, null, dest, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                orig_x = event.getX();
                orig_y = event.getY();
                orig_x = (orig_x - dest.left) * scale;
                orig_y = (orig_y - dest.top) * scale;

                break;
            case MotionEvent.ACTION_MOVE:
                max_dist = dist * scale;
                if (event.getAction() != 1) {

                    move_x = event.getX();
                    move_y = event.getY();

                    move_x = (move_x - dest.left) * scale;
                    move_y = (move_y - dest.top) * scale;

                    // if(m > 0){
                    // int i2 = m + -1;
                    // orig_x = (event.getHistoricalX(i2) - dest.left)*scale;
                    // orig_y = (event.getHistoricalY(i2) - dest.top)*scale;
                    // }
                    if (move_x >= 0 && move_y >= 0) {
                        warp.warpPhotoFromC(image, height, width, max_dist,
                                orig_x, orig_y, move_x, move_y);

                        first++;

                        newBmp.setPixels(image, 0, width, 0, 0, width, height);
                        fg = false;
                    }
                }
                orig_x = move_x;
                orig_y = move_y;

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    public void setWarpBitmap(Bitmap bmp) {
        fg = true;// 重置标志
        first = 0;
        mBmp = bmp;
        width = bmp.getWidth();
        height = bmp.getHeight();

        newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        image = new int[width * height];

        mBmp.getPixels(image, 0, width, 0, 0, width, height);
        newBmp.setPixels(image, 0, width, 0, 0, width, height);
    }

    public void setMode(int mode) {
        this.MODE = mode;
    }

    /**
     * 返回处理好的图片
     *
     * @return
     */
    public Bitmap getWrapBitmap() {
        return newBmp;
    }

}
