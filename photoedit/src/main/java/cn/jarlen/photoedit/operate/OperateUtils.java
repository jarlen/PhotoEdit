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
package cn.jarlen.photoedit.operate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import cn.jarlen.photoedit.R;

/**
 * 添加文字图片工具类
 */
public class OperateUtils
{
	private Activity activity;
	private int screenWidth;// 手机屏幕的宽（像素）
	private int screenHeight;// 手机屏幕的高（像素）

	public static final int LEFTTOP = 1;
	public static final int RIGHTTOP = 2;
	public static final int LEFTBOTTOM = 3;
	public static final int RIGHTBOTTOM = 4;
	public static final int CENTER = 5;

	public OperateUtils(Activity activity)
	{
		this.activity = activity;
		if (screenWidth == 0)
		{
			DisplayMetrics metric = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
			screenWidth = metric.widthPixels; // 屏幕宽度（像素）
			screenHeight = metric.widthPixels; // 屏幕宽度（像素）
		}
	}

	/**
	 * 根据路径获取图片并且压缩，适应view
	 * 
	 * @param filePath
	 *            图片路径
	 * @param contentView
	 *            适应的view
	 * @return Bitmap 压缩后的图片
	 */
	public Bitmap compressionFiller(String filePath, View contentView)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);
		int layoutHeight = contentView.getHeight();
		float scale = 0f;
		int bitmapHeight = bitmap.getHeight();
		int bitmapWidth = bitmap.getWidth();
		scale = bitmapHeight > bitmapWidth
				? layoutHeight / (bitmapHeight * 1f)
				: screenWidth / (bitmapWidth * 1f);
		Bitmap resizeBmp;
		if (scale != 0)
		{
			int bitmapheight = bitmap.getHeight();
			int bitmapwidth = bitmap.getWidth();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale); // 长和宽放大缩小的比例
			resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
					bitmapheight, matrix, true);
		} else
		{
			resizeBmp = bitmap;
		}
		return resizeBmp;
	}

	/**
	 * 根据压缩图片并且适应view
	 * 
	 * @param bitmap
	 *            压缩图片
	 * @param contentView
	 *            适应的view
	 * @return 压缩后的图片
	 */
	public Bitmap compressionFiller(Bitmap bitmap, View contentView)
	{
		int layoutHeight = contentView.getHeight();
		float scale = 0f;
		int bitmapHeight = bitmap.getHeight();
		int bitmapWidth = bitmap.getWidth();
		scale = bitmapHeight > bitmapWidth
				? layoutHeight / (bitmapHeight * 1f)
				: screenWidth / (bitmapWidth * 1f);
		Bitmap resizeBmp;
		if (scale != 0)
		{
			int bitmapheight = bitmap.getHeight();
			int bitmapwidth = bitmap.getWidth();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale); // 长和宽放大缩小的比例
			resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
					bitmapheight, matrix, true);
		} else
		{
			resizeBmp = bitmap;
		}
		return resizeBmp;
	}

	/**
	 * 添加文字方法
	 * 
	 * @param text
	 *            所添加的文字
	 * @return TextObject 返回的字体图片对象
	 */
	public TextObject getTextObject(String text)
	{
		TextObject textObj = null;
		if (TextUtils.isEmpty(text))
		{
			Toast.makeText(activity, "请添加文字", Toast.LENGTH_SHORT).show();
			return null;
		}

		Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.rotate);
		Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.delete);

		textObj = new TextObject(activity, text, 150, 150, rotateBm, deleteBm);
		textObj.setTextObject(true);
		return textObj;
	}
	/**
	 * 添加图片的方法
	 * 
	 * @param text
	 *            文本内容
	 * @param operateView
	 *            容器View对象
	 * @param quadrant
	 *            需要图片显示的区域 （1、左上方，2、右上方，3、左下方，4、右下方，5、中心）
	 * @param x
	 *            离边界x坐标
	 * @param y
	 *            离边界y坐标
	 * @return
	 */
	public TextObject getTextObject(String text, OperateView operateView,
			int quadrant, int x, int y)
	{
		TextObject textObj = null;
		if (TextUtils.isEmpty(text))
		{
			Toast.makeText(activity, "请添加文字", Toast.LENGTH_SHORT).show();
			return null;
		}
		int width = operateView.getWidth();
		int height = operateView.getHeight();
		switch (quadrant)
		{
			case LEFTTOP :
				break;
			case RIGHTTOP :
				x = width - x;
				break;
			case LEFTBOTTOM :
				y = height - y;
				break;
			case RIGHTBOTTOM :
				x = width - x;
				y = height - y;
				break;
			case CENTER :
				x = width / 2;
				y = height / 2;
				break;
			default :
				break;
		}
		Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.rotate);
		Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.delete);
		textObj = new TextObject(activity, text, x, y, rotateBm, deleteBm);
		textObj.setTextObject(true);
		return textObj;
	}

	/**
	 * 添加图片方法
	 * 
	 * @param srcBmp
	 *            被操作的图片
	 * @return
	 */

	public ImageObject getImageObject(Bitmap srcBmp)
	{
		Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.rotate);
		Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.delete);
		ImageObject imgObject = new ImageObject(srcBmp, rotateBm, deleteBm);
		Point point = new Point(20, 20);
		imgObject.setPoint(point);
		return imgObject;
	}

	/**
	 * 添加图片方法
	 * 
	 * @param srcBmp
	 *            被操作的图片
	 * @param operateView
	 *            容器View对象
	 * @param quadrant
	 *            需要图片显示的区域 （1、左上方，2、右上方，3、左下方，4、右下方，5、中心）
	 * @param x
	 *            离边界x坐标
	 * @param y
	 *            离边界y坐标
	 * @return
	 */

	public ImageObject getImageObject(Bitmap srcBmp, OperateView operateView,
			int quadrant, int x, int y)
	{
		Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.rotate);
		Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.delete);
		int width = operateView.getWidth();
		int height = operateView.getHeight();
//		int srcWidth = srcBmp.getWidth();
//		int srcHeight = srcBmp.getHeight();
//		if (height > width)
//		{
//			if (srcHeight > srcWidth)
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, height / 3 * srcWidth
//						/ srcHeight, height / 3);
//			} else
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, width / 3, width / 3
//						* srcHeight / srcWidth);
//			}
//		} else
//		{
//			if (srcHeight > srcWidth)
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, height / 2 * srcWidth
//						/ srcHeight, height / 2);
//			} else
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, width / 3, width / 3
//						* srcHeight / srcWidth);
//			}
//		}
		switch (quadrant)
		{
			case LEFTTOP :
				break;
			case RIGHTTOP :
				x = width - x;
				break;
			case LEFTBOTTOM :
				y = height - y;
				break;
			case RIGHTBOTTOM :
				x = width - x;
				y = height - y;
				break;
			case CENTER :
				x = width / 2;
				y = height / 2;
				break;
			default :
				break;
		}
		ImageObject imgObject = new ImageObject(srcBmp, x, y, rotateBm,
				deleteBm);
		Point point = new Point(20, 20);
		imgObject.setPoint(point);
		return imgObject;
	}

}
