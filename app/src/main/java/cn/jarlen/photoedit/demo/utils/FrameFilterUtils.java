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
package cn.jarlen.photoedit.demo.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;

public class FrameFilterUtils
{

	static int alpha(int color)
	{
		return (color & 0xFF000000) >> 24;
	}
	static int red(int color)
	{
		return (color & 0xFF0000) >> 16;
	}
	static int green(int color)
	{
		return (color & 0xFF00) >> 8;
	}
	static int blue(int color)
	{
		return color & 0xFF;
	}
	static int ARGB(int alpha, int red, int green, int blue)
	{
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	int RGB(int red, int green, int blue)
	{
		return 255 << 24 | red << 16 | green << 8 | blue;
	}

	float Max(float x, float y)
	{
		return x > y ? x : y;
	}

	float Min(float x, float y)
	{
		return x > y ? y : x;
	}

	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth, int newHeight)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	/**
	 * PS 滤色(BLEND_SCREEN)
	 * 
	 * @param baseColor
	 * @param overlayColor
	 * @return
	 * 
	 *         漏光效果
	 */
	public static int getLayerBlendingScreen(int baseColor, int overlayColor,
			double factor)
	{

		int baseColor_a = alpha(baseColor);
		int baseColor_r = red(baseColor);
		int baseColor_g = green(baseColor);
		int baseColor_b = blue(baseColor);

		int overlayColor_a = alpha(overlayColor);
		int overlayColor_r = red(overlayColor);
		int overlayColor_g = green(overlayColor);
		int overlayColor_b = blue(overlayColor);

		int a, r = 0, g = 0, b = 0;

		r = (int) (255 - (255 - baseColor_r) * (255 - overlayColor_r * factor)
				/ 255);
		g = (int) (255 - (255 - baseColor_g) * (255 - overlayColor_g * factor)
				/ 255);
		b = (int) (255 - (255 - baseColor_b) * (255 - overlayColor_b * factor)
				/ 255);

		return ARGB(baseColor_a, r, g, b);
	}

	/**
	 * 正片叠底(BLEND_MULTIPLY)
	 * 
	 * @param baseColor
	 * @param overlayColor
	 * @param factor
	 * @return
	 */

	public static int getLayerBlendingMultiply(int baseColor, int overlayColor,
			double factor)
	{

		int baseColor_a = alpha(baseColor);
		int baseColor_r = red(baseColor);
		int baseColor_g = green(baseColor);
		int baseColor_b = blue(baseColor);

		int overlayColor_a = alpha(overlayColor);
		int overlayColor_r = red(overlayColor);
		int overlayColor_g = green(overlayColor);
		int overlayColor_b = blue(overlayColor);

		int a = 0, r = 0, g = 0, b = 0;

		a = baseColor_a;
		r = baseColor_r * overlayColor_r / 255;
		g = baseColor_g * overlayColor_g / 255;
		b = baseColor_b * overlayColor_b / 255;

		return ARGB(a, r, g, b);
	}

	/**
	 * 柔光
	 * 
	 * @param colorA
	 * @param colorB
	 * @return 油画,a1map
	 */
	public static int getLayerBlendingSoftLight(int colorA, int colorB)
	{
		int c = 0;

		// int colorB_a = alpha(colorB);
		int colorB_r = red(colorB);
		int colorB_g = green(colorB);
		int colorB_b = blue(colorB);

		int colorB_gray = (299 * colorB_r + 587 * colorB_g + 114 * colorB_b) / 1000;

		int colorA_a = alpha(colorA);
		int colorA_r = red(colorA);
		int colorA_g = green(colorA);
		int colorA_b = blue(colorA);

		int a, r, g, b;

		if (colorB_gray > 128)
		{
			r = (int) (colorA_r * (255 - colorB_r) / 128 + Math
					.sqrt(colorA_r / 255.0) * (2 * colorB_r - 255));
			g = (int) (colorA_g * (255 - colorB_g) / 128 + Math
					.sqrt(colorA_g / 255.0) * (2 * colorB_g - 255));
			b = (int) (colorA_b * (255 - colorB_b) / 128 + Math
					.sqrt(colorA_b / 255.0) * (2 * colorB_b - 255));

		} else
		{
			r = (int) (colorA_r * colorB_r / 128.0 + Math.pow(colorA_r / 255.0,
					2) * (255 - 2 * colorB_r));
			g = (int) (colorA_g * colorB_g / 128.0 + Math.pow(colorA_g / 255.0,
					2) * (255 - 2 * colorB_g));
			b = (int) (colorA_b * colorB_b / 128.0 + Math.pow(colorA_b / 255.0,
					2) * (255 - 2 * colorB_b));

		}
		return ARGB(colorA_a, r, g, b);
	}

	public static int getLayerBlendingLighten(int baseColor, int overlayColor)
	{
		int baseColor_a = alpha(baseColor);
		int baseColor_r = red(baseColor);
		int baseColor_g = green(baseColor);
		int baseColor_b = blue(baseColor);

		int baseColorGray = (299 * baseColor_r + 587 * baseColor_g + 114 * baseColor_b) / 1000;

		int overlayColor_a = alpha(overlayColor);
		int overlayColor_r = red(overlayColor);
		int overlayColor_g = green(overlayColor);
		int overlayColor_b = blue(overlayColor);

		int overlayColorGray = (299 * overlayColor_r + 587 * overlayColor_g + 114 * overlayColor_b) / 1000;

		int a, r = 0, g = 0, b = 0;

		if (baseColorGray >= overlayColorGray)
		{
			r = baseColor_r;
			g = baseColor_g;
			b = baseColor_b;
		} else
		{
			r = overlayColor_r;
			g = overlayColor_g;
			b = overlayColor_b;
		}

		return ARGB(baseColor_a, r, g, b);

	}
	
	public static Bitmap getTestFilter(Bitmap base,Bitmap overlay)
	{
		int width = base.getWidth();
		int height = overlay.getHeight();
		
		
		int[] basePixels = new int[width * height];
		int[] overlayPixels = new int[width * height];
		
		base.getPixels(basePixels, 0, width, 0, 0, width, height);
		overlay.getPixels(overlayPixels, 0, width, 0, 0, width, height);
		int[] result = new int[width * height];
		
		for(int i = 0; i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				int BaseColor = basePixels[i * height + j];
				int overlayColor = overlayPixels[i * height + j];
				
				int color = getLayerBlendingSoftLight(BaseColor, overlayColor);
				result[i * height + j] = color;
			}
		}
		
		Bitmap tempBitmap = Bitmap.createBitmap(result, width, height,
				Config.ARGB_8888);
		return tempBitmap;
	}
}
