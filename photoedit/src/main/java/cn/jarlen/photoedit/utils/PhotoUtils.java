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
