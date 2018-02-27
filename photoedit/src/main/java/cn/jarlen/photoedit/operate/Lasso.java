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

import java.util.List;

import android.graphics.PointF;
import android.util.Log;


/**
 * 判断某个点是否在多边形区域内
 * @author jarlen
 * 
 */
public class Lasso
{
	private float[] mPolyX, mPolyY;
	private int mPolySize;

	/**
	 * 构造方法
	 * 
	 * @param
	 *            
	 */
	public Lasso(List<PointF> pointFs)
	{
		this.mPolySize = pointFs.size();

		this.mPolyX = new float[this.mPolySize];
		this.mPolyY = new float[this.mPolySize];

		for (int i = 0; i < this.mPolySize; i++)
		{
			this.mPolyX[i] = pointFs.get(i).x;
			this.mPolyY[i] = pointFs.get(i).y;
		}

		Log.d("lasso", "lasso size:" + mPolySize);
	}

	/**
	 * 判断多边形是否包含点
	 * 
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 * @return true 
	 */
	public boolean contains(float x, float y)
	{
		boolean result = false;

		for (int i = 0, j = mPolySize - 1; i < mPolySize; j = i++)
		{
			if ((mPolyY[i] < y && mPolyY[j] >= y)
					|| (mPolyY[j] < y && mPolyY[i] >= y))
			{
				if (mPolyX[i] + (y - mPolyY[i]) / (mPolyY[j] - mPolyY[i])
						* (mPolyX[j] - mPolyX[i]) < x)
				{
					result = !result;
				}
			}
		}
		return result;
	}
}
