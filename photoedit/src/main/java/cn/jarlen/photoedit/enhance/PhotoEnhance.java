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
package cn.jarlen.photoedit.enhance;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * @author jarlen
 */
public class PhotoEnhance
{

	public final int Enhance_Saturation = 0;
	public final int Enhance_Brightness = 1;
	public final int Enhance_Contrast = 2;

	private Bitmap mBitmap;

	private float saturationNum = 1.0f;
	private float brightNum = 0.0f;
	private float contrastNum = 1.0f;

	public PhotoEnhance()
	{

	}

	public PhotoEnhance(Bitmap bitmap)
	{
		this.mBitmap = bitmap;
	}

	public float getSaturation()
	{
		return saturationNum;
	}

	/**
	 * 设置饱和度 ( 0 ~ 2)
	 * 
	 * @param saturationNum
	 *            (范围 :0 ~ 255)
	 * 
	 */
	public void setSaturation(int saturationNum)
	{
		this.saturationNum = (float) (saturationNum * 1.0f / 128);
	}

	public float getBrightness()
	{
		return brightNum;
	}

	/**
	 * 设置亮度 (-128 ~ 128 )
	 * 
	 * @param brightNum
	 *            (范围：0 ~ 255)
	 * 
	 */
	public void setBrightness(int brightNum)
	{
		this.brightNum = brightNum - 128;
	}

	public float getContrast()
	{
		return contrastNum;
	}

	/**
	 * 设置对比度 (0.5 ~ 1.5)
	 * 
	 * @param contrastNum
	 *            (范围 : 0 ~ 255)
	 * 
	 */
	public void setContrast(int contrastNum)
	{
		this.contrastNum = (float) ((contrastNum / 2 + 64) / 128.0);
	}

	private ColorMatrix mAllMatrix = null;
	private ColorMatrix saturationMatrix = null;
	private ColorMatrix contrastMatrix = null;
	private ColorMatrix brightnessMatrix = null;

	public Bitmap handleImage(int type)
	{

		Bitmap bmp = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		if (mAllMatrix == null)
		{
			mAllMatrix = new ColorMatrix();
		}

		/* 饱和度矩阵 */
		if (saturationMatrix == null)
		{
			saturationMatrix = new ColorMatrix();
		}

		/* 对比度矩阵 */
		if (contrastMatrix == null)
		{
			contrastMatrix = new ColorMatrix();
		}

		/* 亮度矩阵 */
		if (brightnessMatrix == null)
		{
			brightnessMatrix = new ColorMatrix();
		}

		switch (type)
		{
			case Enhance_Saturation :
				saturationMatrix.reset();
				saturationMatrix.setSaturation(saturationNum);
				break;

			case Enhance_Brightness :
				brightnessMatrix.reset();
				brightnessMatrix.set(new float[]{1, 0, 0, 0, brightNum, 0, 1,
						0, 0, brightNum, 0, 0, 1, 0, brightNum, 0, 0, 0, 1, 0});
				break;
			case Enhance_Contrast :

				/* 在亮度不变的情况下，提高对比度必定要降低亮度 */

				float regulateBright = 0;
				regulateBright = (1 - contrastNum) * 128;

				contrastMatrix.reset();
				contrastMatrix.set(new float[]{contrastNum, 0, 0, 0,
						regulateBright, 0, contrastNum, 0, 0, regulateBright,
						0, 0, contrastNum, 0, regulateBright, 0, 0, 0, 1, 0});
				break;

			default :
				break;
		}

		mAllMatrix.reset();
		mAllMatrix.postConcat(saturationMatrix);
		mAllMatrix.postConcat(brightnessMatrix);
		mAllMatrix.postConcat(contrastMatrix);

		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
		canvas.drawBitmap(mBitmap, 0, 0, paint);
		return bmp;

	}

}
