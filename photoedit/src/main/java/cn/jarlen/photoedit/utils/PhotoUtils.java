package cn.jarlen.photoedit.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 图片基本操作
 * @author jarlen
 *
 */

public class PhotoUtils
{

	/**
	 * 图片旋转
	 * @param bit
	 * 旋转原图像
	 * 
	 * @param degrees
	 * 旋转度数
	 * 
	 * @return
	 * 旋转之后的图像
	 * 
	 */
	public static Bitmap rotateImage(Bitmap bit, int degrees)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
				bit.getHeight(), matrix, true);
		return tempBitmap;
	}
	
	/**
	 * 翻转图像
	 * 
	 * @param bit
	 * 翻转原图像
	 * 
	 * @param x
	 * 翻转X轴
	 * 
	 * @param y
	 * 翻转Y轴
	 * 
	 * @return
	 * 翻转之后的图像
	 * 
	 * 说明:
	 * (1,-1)上下翻转
	 * (-1,1)左右翻转
	 * 
	 */
	public static Bitmap reverseImage(Bitmap bit,int x,int y)
	{
		Matrix matrix = new Matrix();
		matrix.postScale(x, y);
		
		Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
				bit.getHeight(), matrix, true);
		return tempBitmap;
	}
}
