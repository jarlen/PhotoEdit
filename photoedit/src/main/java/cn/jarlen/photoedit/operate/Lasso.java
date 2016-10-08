package cn.jarlen.photoedit.operate;

import java.util.List;

import android.graphics.PointF;
import android.util.Log;

/**
 * 判断某个点是否在多边形区域内
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
