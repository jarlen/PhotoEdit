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

import android.graphics.Bitmap;

/**
 * 画笔
 * @author jarlen
 *
 */
public class PaintBrush
{
	/**
	 * 画笔图片
	 */
	private Bitmap paintBitmap;
	
	/**
	 * 画笔粗细
	 * 
	 */
	private int paintSize;
	
	/**
	 * 画笔颜色
	 * 
	 */
	private int paintColor;
	
	/**
	 * 画笔粗细种类
	 * 1，2，3...
	 */
	private int paintSizeTypeNo;
	

	/**
	 * ********************************
	 * 
	 * *********
	 * 	   *
	 * 	   *  
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *************************************/
	
	
	
	/**
	 * 获取画笔图片
	 * @return
	 */
	public Bitmap getPaintBitmap()
	{
		return paintBitmap;
	}

	/**
	 * 设置画笔图片
	 * 
	 * @param paintBitmap
	 * 画笔图片
	 * 
	 */
	public void setPaintBitmap(Bitmap paintBitmap)
	{
		this.paintBitmap = paintBitmap;
	}

	/**
	 * 获取画笔大小
	 * 
	 * @return
	 */
	public int getPaintSize()
	{
		return paintSize;
	}

	/**
	 * 设置画笔大小
	 * 
	 * @param paintSize
	 * 画笔大小
	 * 
	 */
	public void setPaintSize(int paintSize)
	{
		if(paintSize >= paintSizeTypeNo)
		{
			this.paintSize = paintSizeTypeNo;
		}
		else if(paintSize <= 0)
		{
			this.paintSize = 1;
		}
		else
		{
			this.paintSize = paintSize;
		}
		
	}

	/**
	 * 获取画笔颜色
	 * 
	 * @return
	 */
	public int getPaintColor()
	{
		return paintColor;
	}

	/**
	 * 设置画笔颜色
	 * 
	 * @param paintColor
	 * 画笔颜色
	 * 
	 */
	public void setPaintColor(int paintColor)
	{
		this.paintColor = paintColor;
	}

	/**
	 * 获取画笔粗细种类
	 * 
	 * @return
	 * 
	 */
	public int getPaintSizeTypeNo()
	{
		return paintSizeTypeNo;
	}

	/**
	 * 设置画笔粗细种类
	 * 
	 * @param paintSizeTypeNo
	 * 画笔粗细种类
	 * 
	 */
	public void setPaintSizeTypeNo(int paintSizeTypeNo)
	{
		this.paintSizeTypeNo = paintSizeTypeNo;
	}
	
	
}
