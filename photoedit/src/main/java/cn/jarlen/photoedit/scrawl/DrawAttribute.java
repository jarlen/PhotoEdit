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
package cn.jarlen.photoedit.scrawl;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

/**
 * @author jarlen
 */
public class DrawAttribute
{
	public static enum Corner
	{
		LEFTTOP, RIGHTTOP, LEFTBOTTOM, RIGHTBOTTOM, ERROR
	};
	public static enum DrawStatus
	{
		PEN_NORMAL,PEN_WATER, PEN_CRAYON, PEN_COLOR_BIG, PEN_ERASER,PEN_STAMP
	};

	public final static int backgroundOnClickColor = 0xfff08d1e;
	public static int screenHeight;
	public static int screenWidth;
	public static Paint paint = new Paint();

	public static Bitmap getImageFromAssetsFile(Context context,
			String fileName, boolean isBackground)
	{
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (isBackground)
			image = Bitmap.createScaledBitmap(image, DrawAttribute.screenWidth,
					DrawAttribute.screenHeight, false);
		return image;

	}
}
