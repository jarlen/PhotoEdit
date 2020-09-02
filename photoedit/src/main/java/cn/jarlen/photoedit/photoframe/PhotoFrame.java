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
package cn.jarlen.photoedit.photoframe;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;


/**
 * 
 * @author jarlen
 * 
 * 使用方法:
 * 
 * 1. 创建对象
 * 		PhotoFrame  mImageFrame = new PhotoFrame(this,mBitmap);
 * 2. 设置类型
 * 
 * mImageFrame.setFrameType(PhotoFrame.FRAME_SMALL);
 * 
 * 3. 初始化相册资源
 * 
 * mImageFrame.setFrameResources()
 * 
 * 4. 执行合成
 * mTmpBmp = mImageFrame.combineFrameRes();
 * 
 * 5. 保存
 *
 */

public class PhotoFrame
{
	public static final int FRAME_BIG = 0x1;
	public static final int FRAME_SMALL = FRAME_BIG + 1;

	private Context mContext;

	/**
	 * 源图片
	 */
	private Bitmap mBitmap;

	private int FRAME_TYPE = FRAME_SMALL;

	private int mFrameRes = 0;
	private int[] mFrameListRes = null;

	private String mFramePathRes = null;
	private ArrayList<String> mFramePathListRes = null;

	public PhotoFrame(Context context, Bitmap bm)
	{
		mContext = context;
		mBitmap = bm;
	}

	public void setFrameType(int type)
	{
		this.FRAME_TYPE = type;
	}

	/**
	 * 添加相框资源
	 * 
	 * @param frame_around_left_top
	 *            :左上
	 * @param frame_around_left
	 *            :左
	 * @param frame_around_left_bottom
	 *            :左下
	 * @param frame_around_bottom
	 *            :下
	 * @param frame_around_right_bottom
	 *            :右下
	 * @param frame_around_right
	 *            :右
	 * @param frame_around_right_top
	 *            :右上
	 * @param frame_around_top
	 *            :上
	 */
	public void setFrameResources(int frame_around_left_top,
			int frame_around_left, int frame_around_left_bottom,
			int frame_around_bottom, int frame_around_right_bottom,
			int frame_around_right, int frame_around_right_top,
			int frame_around_top)
	{

		mFrameListRes = new int[]{frame_around_left_top, frame_around_left,
				frame_around_left_bottom, frame_around_bottom,
				frame_around_right_bottom, frame_around_right,
				frame_around_right_top, frame_around_top};
	}

	/**
	 * 添加相框资源
	 * 
	 * @param res
	 * 相框资源ID
	 * 
	 */
	public void setFrameResources(int res)
	{
		mFrameRes = res;
	}

	/**
	 * 添加相框资源
	 * @param resPath
	 * 相框资源路径
	 * 
	 */
	public void setFramePath(String resPath)
	{
		mFramePathRes = resPath;
	}
	
	
	/**
	 * 添加相框资源
	 * @param mList
	 * 小资源图片路径列表(说明: 图片顺序为:左上，左，左下，下，右下，右，右上，上)
	 * 
	 */
	public void setFrameListPath(ArrayList<String> mList)
	{
		mFramePathListRes = mList;
	}
	
	
	/**
	 * 资源ID的相框合成
	 * @return
	 */
	public Bitmap combineFrameRes()
	{
		Bitmap bitmap = null;

		if (this.FRAME_TYPE == FRAME_BIG)
		{
			bitmap = combinateFrame(mFrameRes);
		} else if (this.FRAME_TYPE == FRAME_SMALL)
		{
			bitmap = combinateFrame(mFrameListRes);
		}
		return bitmap;
	}

	/**
	 * 
	 * @return
	 * 路径图片的合成相框
	 * 
	 */
	public Bitmap combineFramePathRes()
	{
		Bitmap bitmap = null;

		if (this.FRAME_TYPE == FRAME_BIG)
		{
			bitmap = combinateFrame(mFramePathRes);
		} else if(this.FRAME_TYPE == FRAME_SMALL)
		{
			bitmap = combinateFrame(mFramePathListRes);
		}

		return bitmap;
	}

	/**
	 * 添加边框
	 * 
	 * @param res
	 *            边框资源
	 * @return
	 */
	private Bitmap combinateFrame(int res)
	{
		Bitmap bitmap = decodeBitmap(res);
		
		Drawable[] array = new Drawable[2];
		array[0] = new BitmapDrawable(mContext.getResources(), mBitmap);
		Bitmap b = resize(bitmap, this.mBitmap.getWidth(),
				this.mBitmap.getHeight());
		array[1] = new BitmapDrawable(mContext.getResources(), b);
		LayerDrawable layer = new LayerDrawable(array);
		return drawableToBitmap(layer);
	}

	/**
	 * 添加边框
	 * 
	 * @param res
	 *            边框资源
	 * @return
	 */
	private Bitmap combinateFrame(String res)
	{
		Bitmap bitmap = decodeBitmap(res);
		Drawable[] array = new Drawable[2];
		array[0] =  new BitmapDrawable(mContext.getResources(), mBitmap);
		Bitmap b = resize(bitmap, this.mBitmap.getWidth(),
				this.mBitmap.getHeight());
		array[1] =  new BitmapDrawable(mContext.getResources(), b);
		LayerDrawable layer = new LayerDrawable(array);
		return drawableToBitmap(layer);
	}

	/**
	 * 将Drawable转换成Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitmap(Drawable drawable)
	{
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE
						? Config.ARGB_8888
						: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 将R.drawable.*转换成Bitmap
	 * 
	 * @param res
	 * @return
	 */
	private Bitmap decodeBitmap(int res)
	{
		return BitmapFactory.decodeResource(mContext.getResources(), res);
	}

	/**
	 * 将图片路径String 转化为bitmap
	 * 
	 * @param res
	 * @return
	 */
	private Bitmap decodeBitmap(String res)
	{
		return BitmapFactory.decodeFile(res);
	}

	/**
	 * 图片缩放
	 * 
	 * @param bm
	 * @param w
	 * @param h
	 * @return
	 */
	public Bitmap resize(Bitmap bm, int w, int h)
	{
		Bitmap BitmapOrg = bm;

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	
	/**
	 * 
	 * @param res
	 * 边框资源ID数组
	 * 
	 * @return
	 * 合成
	 */
	private Bitmap combinateFrame(int[] res)
	{
		Bitmap bmp = decodeBitmap(res[0]);
		// 边框的宽高
		final int smallW = bmp.getWidth();
		final int smallH = bmp.getHeight();

		// 原图片的宽高
		final int bigW = this.mBitmap.getWidth();
		final int bigH = this.mBitmap.getHeight();

		int wCount = (int) Math.ceil(bigW * 1.0 / smallW);
		int hCount = (int) Math.ceil(bigH * 1.0 / smallH);

		// 组合后图片的宽高
		int newW = (wCount + 2) * smallW;
		int newH = (hCount + 2) * smallH;

		// 重新定义大小
		Bitmap newBitmap = Bitmap.createBitmap(newW, newH, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		Paint p = new Paint();
		p.setColor(Color.TRANSPARENT);
		canvas.drawRect(new Rect(0, 0, newW, newH), p);

		Rect rect = new Rect(smallW, smallH, newW - smallW, newH - smallH);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		canvas.drawRect(rect, paint);

		// 绘原图
		canvas.drawBitmap(this.mBitmap,
				(newW - bigW - 2 * smallW) / 2 + smallW,
				(newH - bigH - 2 * smallH) / 2 + smallH, null);
		// 绘边框
		// 绘四个角
		int startW = newW - smallW;
		int startH = newH - smallH;
		Bitmap leftTopBm = decodeBitmap(res[0]); // 左上角
		Bitmap leftBottomBm = decodeBitmap(res[2]); // 左下角
		Bitmap rightBottomBm = decodeBitmap(res[4]); // 右下角
		Bitmap rightTopBm = decodeBitmap(res[6]); // 右上角

		canvas.drawBitmap(leftTopBm, 0, 0, null);
		canvas.drawBitmap(leftBottomBm, 0, startH, null);
		canvas.drawBitmap(rightBottomBm, startW, startH, null);
		canvas.drawBitmap(rightTopBm, startW, 0, null);

		leftTopBm.recycle();
		leftTopBm = null;
		leftBottomBm.recycle();
		leftBottomBm = null;
		rightBottomBm.recycle();
		rightBottomBm = null;
		rightTopBm.recycle();
		rightTopBm = null;

		// 绘左右边框
		Bitmap leftBm = decodeBitmap(res[1]);
		Bitmap rightBm = decodeBitmap(res[5]);
		for (int i = 0, length = hCount; i < length; i++)
		{
			int h = smallH * (i + 1);
			canvas.drawBitmap(leftBm, 0, h, null);
			canvas.drawBitmap(rightBm, startW, h, null);
		}

		leftBm.recycle();
		leftBm = null;
		rightBm.recycle();
		rightBm = null;

		// 绘上下边框
		Bitmap bottomBm = decodeBitmap(res[3]);
		Bitmap topBm = decodeBitmap(res[7]);
		for (int i = 0, length = wCount; i < length; i++)
		{
			int w = smallW * (i + 1);
			canvas.drawBitmap(bottomBm, w, startH, null);
			canvas.drawBitmap(topBm, w, 0, null);
		}

		bottomBm.recycle();
		bottomBm = null;
		topBm.recycle();
		topBm = null;

		canvas.save();
		canvas.restore();

		return newBitmap;
	}

	private Bitmap combinateFrame(ArrayList<String> res)
	{
		Bitmap bmp = decodeBitmap(res.get(0));
		// 边框的宽高
		final int smallW = bmp.getWidth();
		final int smallH = bmp.getHeight();

		// 原图片的宽高
		final int bigW = this.mBitmap.getWidth();
		final int bigH = this.mBitmap.getHeight();

		int wCount = (int) Math.ceil(bigW * 1.0 / smallW);
		int hCount = (int) Math.ceil(bigH * 1.0 / smallH);

		// 组合后图片的宽高
		int newW = (wCount + 2) * smallW;
		int newH = (hCount + 2) * smallH;

		// 重新定义大小
		Bitmap newBitmap = Bitmap.createBitmap(newW, newH, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		Paint p = new Paint();
		p.setColor(Color.TRANSPARENT);
		canvas.drawRect(new Rect(0, 0, newW, newH), p);

		Rect rect = new Rect(smallW, smallH, newW - smallW, newH - smallH);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		canvas.drawRect(rect, paint);

		// 绘原图
		canvas.drawBitmap(this.mBitmap,
				(newW - bigW - 2 * smallW) / 2 + smallW,
				(newH - bigH - 2 * smallH) / 2 + smallH, null);
		// 绘边框
		// 绘四个角
		int startW = newW - smallW;
		int startH = newH - smallH;
		Bitmap leftTopBm = decodeBitmap(res.get(0)); // 左上角
		Bitmap leftBottomBm = decodeBitmap(res.get(2)); // 左下角
		Bitmap rightBottomBm = decodeBitmap(res.get(4)); // 右下角
		Bitmap rightTopBm = decodeBitmap(res.get(6)); // 右上角

		canvas.drawBitmap(leftTopBm, 0, 0, null);
		canvas.drawBitmap(leftBottomBm, 0, startH, null);
		canvas.drawBitmap(rightBottomBm, startW, startH, null);
		canvas.drawBitmap(rightTopBm, startW, 0, null);

		leftTopBm.recycle();
		leftTopBm = null;
		leftBottomBm.recycle();
		leftBottomBm = null;
		rightBottomBm.recycle();
		rightBottomBm = null;
		rightTopBm.recycle();
		rightTopBm = null;

		// 绘左右边框
		Bitmap leftBm = decodeBitmap(res.get(1));
		Bitmap rightBm = decodeBitmap(res.get(5));
		for (int i = 0, length = hCount; i < length; i++)
		{
			int h = smallH * (i + 1);
			canvas.drawBitmap(leftBm, 0, h, null);
			canvas.drawBitmap(rightBm, startW, h, null);
		}

		leftBm.recycle();
		leftBm = null;
		rightBm.recycle();
		rightBm = null;

		// 绘上下边框
		Bitmap bottomBm = decodeBitmap(res.get(3));
		Bitmap topBm = decodeBitmap(res.get(7));
		for (int i = 0, length = wCount; i < length; i++)
		{
			int w = smallW * (i + 1);
			canvas.drawBitmap(bottomBm, w, startH, null);
			canvas.drawBitmap(topBm, w, 0, null);
		}

		bottomBm.recycle();
		bottomBm = null;
		topBm.recycle();
		topBm = null;

		canvas.save();
		canvas.restore();

		return newBitmap;
	}

}
