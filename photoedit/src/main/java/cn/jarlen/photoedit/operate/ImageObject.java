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

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * @author jarlen
 */
public class ImageObject
{
	protected Point mPoint = new Point();
	protected float mRotation;
	protected float mScale = 1.0f;
	protected boolean mSelected;
	protected boolean flipVertical;
	protected boolean flipHorizontal;
	protected final int resizeBoxSize = 50;
	protected boolean isTextObject;
	protected Bitmap srcBm;
	protected Bitmap rotateBm;
	protected Bitmap deleteBm;
	Paint paint = new Paint();

	private Canvas canvas = null;

	/**
	 * 构造方法
	 */
	public ImageObject()
	{

	}
	public ImageObject(String text)
	{

	}
	/**
	 * 构造方法
	 * @param srcBm 源图片
	 * @param rotateBm 旋转图片
	 * @param deleteBm	删除图片
	 */
	public ImageObject(Bitmap srcBm, Bitmap rotateBm, Bitmap deleteBm)
	{
		this.srcBm = Bitmap.createBitmap(srcBm.getWidth(), srcBm.getHeight(),
				Config.ARGB_8888);
		canvas = new Canvas(this.srcBm);
		canvas.drawBitmap(srcBm, 0, 0, paint);
		this.rotateBm = rotateBm;
		this.deleteBm = deleteBm;
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);// 去掉边缘锯齿
		paint.setStrokeWidth(2);// 设置线宽
	}
	/**
	 * 构造方法
	 * @param srcBm	源图片
	 * @param x 图片初始化x坐标
	 * @param y	图片初始化y坐标
	 * @param rotateBm	旋转图片
	 * @param deleteBm 删除图片
	 */
	public ImageObject(Bitmap srcBm, int x, int y, Bitmap rotateBm,
			Bitmap deleteBm)
	{
		this.srcBm = Bitmap.createBitmap(srcBm.getWidth(), srcBm.getHeight(),
				Config.ARGB_8888);
		canvas = new Canvas(this.srcBm);
		canvas.drawBitmap(srcBm, 0, 0, paint);
		mPoint.x = x;
		mPoint.y = y;
		this.rotateBm = rotateBm;
		this.deleteBm = deleteBm;
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);// 去掉边缘锯齿
		paint.setStrokeWidth(2);// 设置线宽
	}

	int first = 0;// 判断是否第一次

	public void setPoint(Point mPoint)
	{
		// if (mPoint.x < getWidth()) {
		// Log.e("abc", "abc");
		// mPoint.x = getWidth();
		// }
		//
		// if (mPoint.y < getHeight()) {
		// mPoint.y = getHeight();
		// }
		// this.mPoint = mPoint;
		// if (first == 0) {
		setCenter();
		// first++;
		// }
	}
	/**
	 * 获取显示图片的宽
	 * @return
	 */
	public int getWidth()
	{
		if (srcBm != null)
			return srcBm.getWidth();
		else
			return 0;
	}
	/**
	 * 获取显示图片的高
	 * @return
	 */
	public int getHeight()
	{
		if (srcBm != null)
			return srcBm.getHeight();
		else
			return 0;
	}

	public void moveBy(int x, int y)
	{
		mPoint.x += x;
		mPoint.y += y;
		setCenter();
	}

	public void draw(Canvas canvas)
	{
		int sc = canvas.save();
		try
		{
			canvas.translate(mPoint.x, mPoint.y);
			canvas.scale((float) mScale, (float) mScale);
			int sc2 = canvas.save();
			canvas.rotate((float) mRotation);
			canvas.scale((flipHorizontal ? -1 : 1), (flipVertical ? -1 : 1));
			canvas.drawBitmap(srcBm, -getWidth() / 2, -getHeight() / 2, paint);
			canvas.restoreToCount(sc2);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		canvas.restoreToCount(sc);
	}

	/**
	 * 判断点是否在多边形内
	 * 
	 * @param pointx
	 * @param pointy
	 * @return
	 */
	public boolean contains(float pointx, float pointy)
	{
		Lasso lasso = null;
		List<PointF> listPoints = new ArrayList<PointF>();
		listPoints.add(getPointLeftTop());
		listPoints.add(getPointRightTop());
		listPoints.add(getPointRightBottom());
		listPoints.add(getPointLeftBottom());
		lasso = new Lasso(listPoints);
		return lasso.contains(pointx, pointy);
	}

	/**
	 * 获取矩形图片左上角的点
	 * 
	 * @return
	 */
	protected PointF getPointLeftTop()
	{
		PointF pointF = getPointByRotation(centerRotation - 180);
		return pointF;
	}

	/**
	 * 获取矩形图片左上角在画布中的点
	 * 
	 * @return
	 */
	protected PointF getPointLeftTopInCanvas()
	{
		PointF pointF = getPointByRotationInCanvas(centerRotation - 180);
		return pointF;
	}

	/**
	 * 获取矩形图片右上角的点
	 * 
	 * @return
	 */
	protected PointF getPointRightTop()
	{
		PointF pointF = getPointByRotation(-centerRotation);
		return pointF;
	}

	/**
	 * 获取矩形图片右上角在画布中的点
	 * 
	 * @return
	 */
	protected PointF getPointRightTopInCanvas()
	{
		PointF pointF = getPointByRotationInCanvas(-centerRotation);
		return pointF;
	}

	/**
	 * 获取矩形图片右下角的点
	 * 
	 * @return
	 */
	protected PointF getPointRightBottom()
	{
		PointF pointF = getPointByRotation(centerRotation);
		return pointF;
	}

	/**
	 * 获取矩形图片右下角在画布中的点
	 * 
	 * @return
	 */
	protected PointF getPointRightBottomInCanvas()
	{
		PointF pointF = getPointByRotationInCanvas(centerRotation);
		return pointF;
	}

	/**
	 * 获取矩形图片左下角的点
	 * 
	 * @return
	 */
	protected PointF getPointLeftBottom()
	{
		PointF pointF = getPointByRotation(-centerRotation + 180);
		return pointF;
	}

	/**
	 * 获取矩形图片左下角在画布中的点
	 * 
	 * @return
	 */
	protected PointF getPointLeftBottomInCanvas()
	{
		PointF pointF = getPointByRotationInCanvas(-centerRotation + 180);
		return pointF;
	}

	/**
	 * 获取缩放和旋转点
	 * 
	 * @return
	 */
	protected PointF getResizeAndRotatePoint()
	{
		PointF pointF = new PointF();
		double h = getHeight();
		double w = getWidth();
		double r = (float) Math.sqrt(w * w + h * h) / 2 * mScale;
		double rotatetemp = (float) Math.toDegrees(Math.atan(h / w));
		double rotate = (mRotation + rotatetemp) * Math.PI / 180;
		pointF.x = (float) (r * Math.cos(rotate));
		pointF.y = (float) (r * Math.sin(rotate));
		return pointF;
	}

	/**
	 * 判断点击是否在边角按钮上
	 * 
	 * @param x
	 *            触点的横坐标
	 * @param y
	 *            触点得纵坐标
	 * @param type
	 *            四角的位置
	 * @return
	 */
	public boolean pointOnCorner(float x, float y, int type)
	{
		PointF point = null;
		float delX = 0;
		float delY = 0;
		if (OperateConstants.LEFTTOP == type)
		{
			point = getPointLeftTop();
		} else if (OperateConstants.RIGHTBOTTOM == type)
		{
			point = getPointRightBottom();
		}
		delX = x - (point.x + rotateBm.getWidth() / 2);
		delY = y - (point.y + rotateBm.getHeight() / 2);
		float diff = (float) Math.sqrt((delX * delX + delY * delY));
		// float del = rotateBm.getWidth() / 2;
		if (Math.abs(diff) <= resizeBoxSize)
		{
			return true;
		}
		return false;
	}

	private float centerRotation;
	private float R;

	/**
	 * 计算中心点的坐标
	 */
	protected void setCenter()
	{
		double delX = getWidth() * mScale / 2;
		double delY = getHeight() * mScale / 2;
		R = (float) Math.sqrt((delX * delX + delY * delY));
		centerRotation = (float) Math.toDegrees(Math.atan(delY / delX));
	}

	/**
	 * 根据旋转角度获取定点坐标
	 * 
	 * @param rotation
	 * @return
	 */
	private PointF getPointByRotation(float rotation)
	{
		PointF pointF = new PointF();
		double rot = (mRotation + rotation) * Math.PI / 180;
		pointF.x = getPoint().x + (float) (R * Math.cos(rot));
		pointF.y = getPoint().y + (float) (R * Math.sin(rot));
		return pointF;
	}

	public PointF getPointByRotationInCanvas(float rotation)
	{
		PointF pointF = new PointF();
		double rot = (mRotation + rotation) * Math.PI / 180;
		pointF.x = (float) (R * Math.cos(rot));
		pointF.y = (float) (R * Math.sin(rot));
		return pointF;
	}

	public void setScale(float Scale)
	{
		if (getWidth() * Scale >= resizeBoxSize / 2
				&& getHeight() * Scale >= resizeBoxSize / 2)
		{
			this.mScale = Scale;
			setCenter();
		}
	}

	/**
	 * 绘画选中的图标
	 * 
	 * @param canvas
	 */
	public void drawIcon(Canvas canvas)
	{
		PointF deletePF = getPointLeftTop();
		canvas.drawBitmap(deleteBm, deletePF.x - deleteBm.getWidth() / 2,
				deletePF.y - deleteBm.getHeight() / 2, paint);
		PointF rotatePF = getPointRightBottom();
		canvas.drawBitmap(rotateBm, rotatePF.x - rotateBm.getWidth() / 2,
				rotatePF.y - rotateBm.getHeight() / 2, paint);
	}

	/**
	 * get、set方法
	 * 
	 * @return
	 */
	public boolean isSelected()
	{

		return mSelected;
	}

	public void setSelected(boolean Selected)
	{
		this.mSelected = Selected;
	}

	public boolean isFlipVertical()
	{
		return flipVertical;
	}

	public void setFlipVertical(boolean flipVertical)
	{
		this.flipVertical = flipVertical;
	}

	public boolean isFlipHorizontal()
	{
		return flipHorizontal;
	}

	public void setFlipHorizontal(boolean flipHorizontal)
	{
		this.flipHorizontal = flipHorizontal;
	}

	public Bitmap getSrcBm()
	{
		return srcBm;
	}

	public void setSrcBm(Bitmap srcBm)
	{
		this.srcBm = srcBm;
	}

	public Bitmap getRotateBm()
	{
		return rotateBm;
	}

	public void setRotateBm(Bitmap rotateBm)
	{
		this.rotateBm = rotateBm;
	}

	public Bitmap getDeleteBm()
	{
		return deleteBm;
	}

	public void setDeleteBm(Bitmap deleteBm)
	{
		this.deleteBm = deleteBm;
	}

	public Point getPosition()
	{
		return mPoint;
	}

	public void setPosition(Point Position)
	{
		this.mPoint = Position;
	}

	public Point getPoint()
	{
		return mPoint;
	}

	public float getRotation()
	{
		return mRotation;
	}

	public void setRotation(float Rotation)
	{
		this.mRotation = Rotation;
	}

	public float getScale()
	{
		return mScale;
	}

	public void setTextObject(boolean isTextObject)
	{
		this.isTextObject = isTextObject;
	}

	public boolean isTextObject()
	{
		return isTextObject;
	}

}
